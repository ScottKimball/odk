package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.odk.tasks.FieldTypeConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserODK implements JsonParser {

    @Override
    public MotechEvent createEventFromJson(String json, FormDefinition formDefinition, String configurationName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();

        OdkJsonFormPublication publication = mapper.readValue(json,OdkJsonFormPublication.class );
        Map<String, Object> data = publication.getData()[0];
        for (FormField formField : formDefinition.getFormFields()) {

            Object value = data.get(formField.getName());
            if (value != null) {
                value = formatValue(formField.getType(), value);
                params.put(formField.getName(),value );
            }
        }

        String subject = EventSubjects.RECEIVED_FORM + "." +  configurationName + "." + formDefinition.getTitle();
        return new MotechEvent(subject, params);
    }

    private Object formatValue(String type, Object value) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return formatArray((List<String>)value);

            case FieldTypeConstants.BINARY:
                return formatUrl((Map<String, String>) value);

            default:
                return value;
        }
    }

    private String formatList(Object value) {
        String s = (String) value;
        return s.replace(";", "\n");
    }

    private String formatArray(List<String> value) {
        StringBuilder builder = new StringBuilder();
        builder.append(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            builder.append("\n");
            builder.append(value.get(i));
        }
        return builder.toString();
    }

    private String formatUrl(Map<String,String> value) {
        return value.get("url");
    }
}

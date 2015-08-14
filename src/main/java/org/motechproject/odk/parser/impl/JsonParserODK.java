package org.motechproject.odk.parser.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.JsonParserUtils;
import org.motechproject.odk.tasks.FieldTypeConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserODK implements JsonParser {

    private String json;
    private FormDefinition formDefinition;
    private Configuration configuration;

    public JsonParserODK(String json, FormDefinition formDefinition, Configuration configuration) {
        this.json = json;
        this.formDefinition = formDefinition;
        this.configuration = configuration;
    }

    @Override
    public MotechEvent createEventFromJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        OdkJsonFormPublication publication = mapper.readValue(json,OdkJsonFormPublication.class );
        Map<String, Object> data = publication.getData()[0];
        for (FormElement formElement : formDefinition.getFormElements()) {

            Object value = data.get(formElement.getName());
            if (value != null) {
                value = formatValue(formElement.getType(), value);
                params.put(formElement.getName(),value );
            }
        }

        String subject = EventSubjects.RECEIVED_FORM + "." +  configuration.getName() + "." + formDefinition.getTitle();
        return new MotechEvent(subject, params);

    }

    private Object formatValue(String type, Object value) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return JsonParserUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((Map<String, String>) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return JsonParserUtils.formatAsJson(value);

            default:
                return value;
        }
    }

    private String formatUrl(Map<String,String> value) {
        return value.get("url");
    }


}

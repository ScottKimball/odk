package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.odk.tasks.FieldTypeConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserOna implements JsonParser {

    private static final String ATTACHMENTS = "_attachments";
    private static final String FILENAME = "filename";
    private static final String DOWNLOAD_URL = "download_url";

    private List<Map<String, String>> attachments;
    private String json;
    private FormDefinition formDefinition;
    private Configuration configuration;

    public JsonParserOna(String json, FormDefinition formDefinition, Configuration configuration) {
        this.json = json;
        this.formDefinition = formDefinition;
        this.configuration = configuration;
    }

    @Override
    public MotechEvent createEventFromJson() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data = mapper.readValue(json,new TypeReference<HashMap<String,Object>>() {} );
        attachments =(List<Map<String,String>>) data.get(ATTACHMENTS);

        Map<String, Object> params = new HashMap<>();
        for (FormField formField : formDefinition.getFormFields()) {

            Object value = data.get(formField.getName());
            if (value != null) {
                value = formatValue(formField.getType(), value, formField);
                params.put(formField.getName(),value );
            }
        }

        String subject = EventSubjects.RECEIVED_FORM + "." +  configuration.getName() + "." + formDefinition.getTitle();
        return new MotechEvent(subject, params);

    }

    private Object formatValue(String type, Object value, FormField formField) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return JsonParserUtils.formatStringList(value);

            case FieldTypeConstants.STRING_ARRAY:
                return JsonParserUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.DOUBLE_ARRAY:
                return JsonParserUtils.formatDoubleArray((List<Double>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((String) value);

            default:
                return value;
        }
    }

    private Object formatUrl(String value) {

        for (Map<String,String> attachment : attachments) {
            String filename = attachment.get(FILENAME);
            filename = filename.substring(filename.lastIndexOf('/') + 1);

            if (filename.equals(value)) {
                return configuration.getUrl() + attachment.get(DOWNLOAD_URL);
            }
        }
        throw new RuntimeException("Error constructing media url:" + value);
    }

}

package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
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
        for (FormElement formElement : formDefinition.getFormElements()) {

            String name = formElement.getName();
            Object value = data.get(name);
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
                return JsonParserUtils.formatStringList(value);

            case FieldTypeConstants.STRING_ARRAY:
                return JsonParserUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.DOUBLE_ARRAY:
                return JsonParserUtils.formatDoubleArray((List<Double>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((String) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return JsonParserUtils.formatAsJson(value);

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

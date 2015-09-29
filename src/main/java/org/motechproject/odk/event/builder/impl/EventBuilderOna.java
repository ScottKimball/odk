package org.motechproject.odk.event.builder.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormValue;
import org.motechproject.odk.event.builder.AbstractEventBuilder;
import org.motechproject.odk.event.builder.EventBuilderUtils;
import org.motechproject.odk.event.builder.EventBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBuilderOna extends AbstractEventBuilder implements EventBuilder {

    private static final String ATTACHMENTS = "_attachments";
    private static final String FILENAME = "filename";
    private static final String DOWNLOAD_URL = "download_url";

    private List<Map<String, String>> attachments;



    @Override
    public List<MotechEvent> createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws Exception {
        Map<String,Object> data = new ObjectMapper().readValue(json,new TypeReference<HashMap<String,Object>>() {} );
        attachments =(List<Map<String,String>>) data.get(ATTACHMENTS);
        return super.createEvents(json, formDefinition, configuration);
    }


    protected Object formatValue(String type, Object value) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return EventBuilderUtils.formatStringList(value);

            case FieldTypeConstants.STRING_ARRAY:
                return EventBuilderUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.DOUBLE_ARRAY:
                return EventBuilderUtils.formatDoubleArray((List<Double>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((String) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return EventBuilderUtils.formatAsJson(value);

            default:
                return value;
        }
    }

    private Object formatUrl(String value) {

        for (Map<String,String> attachment : attachments) {
            String filename = attachment.get(FILENAME);
            filename = filename.substring(filename.lastIndexOf('/') + 1);

            if (filename.equals(value)) {
                return attachment.get(DOWNLOAD_URL);
            }
        }
        throw new RuntimeException("Error constructing media url:" + value);
    }

    @Override
    protected Map<String, Object> getData(String json) throws Exception{
        return new ObjectMapper().readValue(json,new TypeReference<HashMap<String,Object>>() {} );
    }
}

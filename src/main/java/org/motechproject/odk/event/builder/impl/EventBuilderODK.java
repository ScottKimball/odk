package org.motechproject.odk.event.builder.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.event.builder.AbstractEventBuilder;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.event.builder.EventBuilderUtils;

import java.util.List;
import java.util.Map;

public class EventBuilderODK extends AbstractEventBuilder implements EventBuilder {

    private static final String URL = "url";


    protected Object formatValue(String type, Object value) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return EventBuilderUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((Map<String, String>) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return EventBuilderUtils.formatAsJson(value);

            case FieldTypeConstants.DATE_TIME:
                return EventBuilderUtils.formatDateTime((String) value);

            case FieldTypeConstants.STRING_ARRAY:
                return EventBuilderUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.DOUBLE_ARRAY:
                return EventBuilderUtils.formatDoubleArray((List<Double>) value);

            default:
                return value;
        }
    }

    private String formatUrl(Map<String, String> value) {
        if (value == null) {
            return null;
        }
        return value.get(URL);
    }

    @Override
    protected Map<String, Object> getData(String json) throws Exception {
        OdkJsonFormPublication publication = new ObjectMapper().readValue(json, OdkJsonFormPublication.class);
        return publication.getData()[0];
    }
}


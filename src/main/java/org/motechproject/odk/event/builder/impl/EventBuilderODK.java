package org.motechproject.odk.event.builder.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.event.builder.AbstractEventBuilder;
import org.motechproject.odk.event.builder.EventBuilderUtils;
import org.motechproject.odk.event.builder.EventBuilder;

import java.util.List;
import java.util.Map;

public class EventBuilderODK extends AbstractEventBuilder implements EventBuilder {

    private static final String URL = "url";


    @Override
    public MotechEvent createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws Exception {
        return super.createEvents(json,formDefinition,configuration);
    }

    protected Object formatValue(String type, Object value) {

        switch (type) {
            case FieldTypeConstants.SELECT:
                return EventBuilderUtils.formatStringArray((List<String>) value);

            case FieldTypeConstants.BINARY:
                return formatUrl((Map<String, String>) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return EventBuilderUtils.formatAsJson(value);

            default:
                return value;
        }
    }

    private String formatUrl(Map<String,String> value) {
        return value.get(URL);
    }

    @Override
    protected Map<String, Object> getData(String json) throws Exception {
        OdkJsonFormPublication publication = new ObjectMapper().readValue(json,OdkJsonFormPublication.class );
        return  publication.getData()[0];
    }
}

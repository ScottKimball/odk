package org.motechproject.odk.parser.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.parser.AbstractJsonParser;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.JsonParserUtils;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserODK extends AbstractJsonParser implements JsonParser {

    private static final String URL = "url";

    protected Object formatValue(String type, Object value) {

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
        return value.get(URL);
    }


}

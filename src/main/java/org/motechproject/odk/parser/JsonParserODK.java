package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.OdkJsonFormPublication;

import java.util.HashMap;
import java.util.Map;

public class JsonParserODK implements JsonParser {

    @Override
    public MotechEvent createEventFromJson(String json, FormDefinition formDefinition) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        OdkJsonFormPublication publication = mapper.readValue(json,OdkJsonFormPublication.class );

        String s = "blah";
        return null;
    }
}

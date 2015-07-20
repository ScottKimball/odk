package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;

import java.util.HashMap;
import java.util.Map;

public class JsonParserODK implements JsonParser {

    @Override
    public MotechEvent createEventFromJson(String json, FormDefinition formDefinition) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String,String>>() {};
        Map<String, String> formMap = mapper.readValue(json, typeRef);

        String s = "blah";
        return null;
    }
}

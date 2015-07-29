package org.motechproject.odk.parser;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;

public class JsonParserFactory {

    public JsonParser getParser (String json, FormDefinition formDefinition, Configuration configuration) {
        switch (configuration.getType()) {
            case ODK:
                return new JsonParserODK(json,formDefinition,configuration);
            case ONA:
                return new JsonParserOna(json,formDefinition,configuration);
            default:
                return null;
        }
    }
}

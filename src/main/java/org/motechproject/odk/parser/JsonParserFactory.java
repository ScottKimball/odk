package org.motechproject.odk.parser;

import org.motechproject.odk.domain.ConfigurationType;

public class JsonParserFactory {

    public JsonParser getParser (ConfigurationType type) {
        switch (type) {
            case ODK:
                return new JsonParserODK();
            default:
                return null;
        }
    }
}

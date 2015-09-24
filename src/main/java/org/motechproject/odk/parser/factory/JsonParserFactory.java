package org.motechproject.odk.parser.factory;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.impl.JsonParserODK;
import org.motechproject.odk.parser.impl.JsonParserOna;

public class JsonParserFactory {

    public JsonParser getParser (ConfigurationType type) {
        switch (type) {
            case ODK:
                return new JsonParserODK();
            case ONA:
                return new JsonParserOna();
            case KOBO:
                return new JsonParserOna();
            default:
                return null;
        }
    }
}

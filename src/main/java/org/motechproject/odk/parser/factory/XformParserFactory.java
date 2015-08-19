package org.motechproject.odk.parser.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.odk.parser.impl.XformParserKobo;

public class XformParserFactory {

    public XformParser getParser(ConfigurationType type) {

        switch (type) {
            case ONA:
                return new XformParserODK();
            case ODK:
                return new XformParserODK();
            case KOBO:
                return new XformParserKobo();
            default:
                return null;
        }
    }
}

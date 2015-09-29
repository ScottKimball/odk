package org.motechproject.odk.event.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.event.builder.impl.EventBuilderODK;
import org.motechproject.odk.event.builder.impl.EventBuilderOna;

public class FormEventBuilderFactory {

    public EventBuilder getBuilder(ConfigurationType type) {
        switch (type) {
            case ODK:
                return new EventBuilderODK();
            case ONA:
                return new EventBuilderOna();
            case KOBO:
                return new EventBuilderOna();
            default:
                return null;
        }
    }
}

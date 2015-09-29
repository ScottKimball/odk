package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;

public interface EventBuilder {

    MotechEvent createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws Exception;

}

package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;

import java.util.List;

public interface EventBuilder {

    List<MotechEvent> createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws Exception;

}

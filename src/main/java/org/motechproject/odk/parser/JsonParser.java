package org.motechproject.odk.parser;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;

public interface JsonParser {

    void parse(String json, EventRelay eventRelay, FormDefinition formDefinition, Configuration configuration) throws Exception;

}

package org.motechproject.odk.parser;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;

public interface JsonParser {

    MotechEvent createEventFromJson(String json, FormDefinition formDefinition, String configurationName) throws Exception;
}

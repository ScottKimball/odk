package org.motechproject.odk.parser;

import org.motechproject.odk.domain.FormDefinition;

public interface XformParser {

    FormDefinition parse(String xForm, String configurationName);
}

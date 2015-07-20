package org.motechproject.odk.service;

import org.motechproject.odk.domain.FormDefinition;

public interface FormDefinitionService {

    FormDefinition findByTitle(String title);
    void create (FormDefinition formDefinition);
    void deleteAll();
    void deleteFormDefinitionsByConfigurationName(String configName);
}

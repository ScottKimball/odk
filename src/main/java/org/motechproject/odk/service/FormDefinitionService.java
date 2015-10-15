package org.motechproject.odk.service;

import org.motechproject.odk.domain.FormDefinition;

import java.util.List;

public interface FormDefinitionService {

    FormDefinition findByTitle(String title);
    void create (FormDefinition formDefinition);
    void deleteAll();
    void deleteFormDefinitionsByConfigurationName(String configName);
    List<FormDefinition> findAll();
    FormDefinition findByConfigurationNameAndTitle(String configurationName, String title);
    List<FormDefinition> findAllByConfigName(String configName);
    FormDefinition findById(long id);
}

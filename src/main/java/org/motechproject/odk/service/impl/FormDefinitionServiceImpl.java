package org.motechproject.odk.service.impl;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.repository.FormDefinitionDataService;
import org.motechproject.odk.service.FormDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormDefinitionServiceImpl implements FormDefinitionService {

    @Autowired
    FormDefinitionDataService formDefinitionDataService;

    @Override
    public FormDefinition findByTitle(String title) {
        return formDefinitionDataService.byTitle(title);
    }

    @Override
    public void create(FormDefinition formDefinition) {
        formDefinitionDataService.create(formDefinition);
    }

    @Override
    public void deleteAll() {
        formDefinitionDataService.deleteAll();
    }

    @Override
    public void deleteFormDefinitionsByConfigurationName(String configName) {
        List<FormDefinition> formDefinitions =  formDefinitionDataService.byConfigurationName(configName);
        for (FormDefinition formDefinition : formDefinitions) {
            formDefinitionDataService.delete(formDefinition);
        }
    }

    @Override
    public List<FormDefinition> findAll() {
        return formDefinitionDataService.retrieveAll();
    }

    @Override
    public FormDefinition findByConfigurationNameAndTitle(String configurationName, String title) {
        return formDefinitionDataService.byConfigurationNameAndTitle(configurationName,title);
    }
}

package org.motechproject.odk.web;


import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.service.FormDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/formDefinitions")
public class FormDefinitionController {

    @Autowired
    FormDefinitionService formDefinitionService;


    @RequestMapping(value = "/{configName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<FormDefinition> getFormDefsByConfigName(@PathVariable("configName") String configName) {
        List<FormDefinition> formDefinitions = formDefinitionService.findAllByConfigName(configName);
        return formDefinitions;
    }

    @RequestMapping("/formdefinition/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FormDefinition getFormDefById(@PathVariable("id") long id) {
        return formDefinitionService.findById(id);
    }

}

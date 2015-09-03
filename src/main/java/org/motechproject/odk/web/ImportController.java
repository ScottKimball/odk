package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.factory.FormDefinitionImportServiceFactory;
import org.motechproject.odk.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ImportController {

    @Autowired
    FormDefinitionImportServiceFactory formDefinitionImportServiceFactory;

    @Autowired
    SettingsService settingsService;

    @RequestMapping(value = "/import/{config}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean syncForms (@PathVariable("config") String config) {
        Configuration configuration = settingsService.getConfigByName(config);
        return formDefinitionImportServiceFactory.getService(configuration.getType()).importForms(configuration);
    }
}

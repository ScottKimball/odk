package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.factory.FormDefinitionImportServiceFactory;
import org.motechproject.odk.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SyncController {

    @Autowired
    FormDefinitionImportServiceFactory formDefinitionImportServiceFactory;

    @Autowired
    ConfigurationService configurationService;

    @RequestMapping(value = "/sync/{config}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void syncForms (@PathVariable("config") String config) {

        Configuration configuration = configurationService.getConfigByName(config);
        boolean success = formDefinitionImportServiceFactory.getService(configuration.getType()).importForms(configuration);

    }
}

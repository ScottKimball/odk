package org.motechproject.odk.service.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceODK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FormDefinitionImportServiceFactory {

    @Autowired
    FormDefinitionImportServiceODK formDefinitionImportServiceODK;


    public FormDefinitionImportService getService (ConfigurationType type) {

        switch (type) {
            case ODK:
                return formDefinitionImportServiceODK;

            default:
                return null;

        }
    }
}

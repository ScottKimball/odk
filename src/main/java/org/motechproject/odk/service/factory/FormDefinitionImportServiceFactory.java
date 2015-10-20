package org.motechproject.odk.service.factory;

import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceKobo;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceODK;
import org.motechproject.odk.service.impl.FormDefinitionImportServiceOna;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FormDefinitionImportServiceFactory {

    @Autowired
    private FormDefinitionImportServiceODK formDefinitionImportServiceODK;

    @Autowired
    private FormDefinitionImportServiceOna formDefinitionImportServiceOna;

    @Autowired
    private FormDefinitionImportServiceKobo formDefinitionImportServiceKobo;


    public FormDefinitionImportService getService(ConfigurationType type) {

        switch (type) {
            case ODK:
                return formDefinitionImportServiceODK;

            case ONA:
                return formDefinitionImportServiceOna;

            case KOBO:
                return formDefinitionImportServiceKobo;

            default:
                return null;

        }
    }
}

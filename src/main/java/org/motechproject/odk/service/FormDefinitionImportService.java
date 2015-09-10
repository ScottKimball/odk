package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ImportStatus;

public interface FormDefinitionImportService {

    ImportStatus importForms(Configuration config);
}

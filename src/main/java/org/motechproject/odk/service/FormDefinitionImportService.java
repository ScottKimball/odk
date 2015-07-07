package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;

public interface FormDefinitionImportService {

    boolean importForms(Configuration config);
}

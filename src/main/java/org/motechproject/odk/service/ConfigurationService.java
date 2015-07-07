package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;

public interface ConfigurationService {

    Configuration getConfigByName (String name);
}

package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;

import java.util.List;

public interface SettingsService {

    Configuration getConfigByName (String name);
    void addOrUpdateConfiguration(Configuration configuration);
    void removeConfiguration (Configuration configuration);
    List<Configuration> getAllConfigs();
    void addConfigs();
}

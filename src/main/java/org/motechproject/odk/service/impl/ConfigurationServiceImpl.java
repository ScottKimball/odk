package org.motechproject.odk.service.impl;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.service.ConfigurationService;
import org.springframework.stereotype.Service;


@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final String ODK_URL = "http://motech-test01.rcg.usm.maine.edu:8080/ODKAggregate";
    private static final String ONA_URL = "http://ona.io/scott";

    /*Hard wired for now*/
    private static final String CONFIG_NAME_ODK = "odk";
    private static final String CONFIG_NAME_ONA = "ona";
    private static final Configuration ODK_CONFIG =  new Configuration(ODK_URL, "username","password", CONFIG_NAME_ODK, ConfigurationType.ODK);
    private static final Configuration ONA_CONFIG = new Configuration(ONA_URL,"username","password", CONFIG_NAME_ONA, ConfigurationType.ONA);


    @Override
    public Configuration getConfigByName(String name) {

        if (name.equals(CONFIG_NAME_ODK)) {
            return ODK_CONFIG;
        } else {
            return ONA_CONFIG;
        }
    }
}

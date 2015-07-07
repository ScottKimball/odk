package org.motechproject.odk.service.impl;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.ConfigurationService;
import org.springframework.stereotype.Service;


@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final String ODK_FORMLIST_URL = "http://motech-test01.rcg.usm.maine.edu:8080/ODKAggregate/formList";

    /*Hard wired for now*/
    private static final String ODK = "odk";
    private static final Configuration ODK_CONFIG =  new Configuration(ODK_FORMLIST_URL, "username","password",ODK);
    private static final Configuration ONA_CONFIG = new Configuration(null,null,null,null);


    @Override
    public Configuration getConfigByName(String name) {

        if (name.equals(ODK)) {
            return ODK_CONFIG;
        } else {
            return ONA_CONFIG;
        }
    }
}

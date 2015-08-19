package org.motechproject.odk.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.domain.Settings;
import org.motechproject.odk.service.SettingsService;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Service("odkSettingsService")
public class SettingsServiceImpl implements SettingsService {

    private static final String ODK_URL = "http://motech-test01.rcg.usm.maine.edu:8080";
    private static final String ONA_URL = "http://ona.io";
    private static final String KOBO_URL = "https://kc.kobotoolbox.org";

    private static final String CONFIG_FILE_NAME = "settings.json";

    /*Hard wired for now*/
    private static final String CONFIG_NAME_ODK = "odk";
    private static final String CONFIG_NAME_ONA = "ona";
    private static final String CONFIG_NAME_KOBO = "kobo";
    private static final Configuration ODK_CONFIG =  new Configuration(ODK_URL, "username","password", CONFIG_NAME_ODK, ConfigurationType.ODK, "/ODKAggregate");
    private static final Configuration ONA_CONFIG = new Configuration(ONA_URL,"username","password", CONFIG_NAME_ONA, ConfigurationType.ONA,"/scott" );
    private static final Configuration KOBO_CONFIG = new Configuration(KOBO_URL,"scott","motech", CONFIG_NAME_KOBO, ConfigurationType.KOBO,"");


    private SettingsFacade settingsFacade;
    private Settings settings;

    @Autowired
    public SettingsServiceImpl(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadSettings();
    }

    @Override
    public Configuration getConfigByName(String name) {

        for (Configuration configuration : settings.getConfigurations()) {
            if (configuration.getName().equals(name)) {
                return configuration;
            }
        }

        return null;
    }

    @Override
    public void addOrUpdateConfiguration(Configuration configuration) {
        Configuration exists = getConfigByName(configuration.getName());
        if (exists == null) {
            settings.getConfigurations().add(configuration);
        } else {
            settings.getConfigurations().remove(exists);
            settings.getConfigurations().add(configuration);
        }
        updateSettings();
    }

    @Override
    public void removeConfiguration(Configuration configuration) {
        settings.getConfigurations().remove(configuration);
        updateSettings();
    }

    @Override
    public List<Configuration> getAllConfigs() {
        return settings.getConfigurations();
    }

    private synchronized void loadSettings() {
        try (InputStream is = settingsFacade.getRawConfig(CONFIG_FILE_NAME)) {

            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();

            settings = gson.fromJson(jsonText, Settings.class);
            if (settings == null) {
                settings = new Settings();
            }
        }
        catch (Exception e) {
            throw new JsonIOException("Malformed " + CONFIG_FILE_NAME + " file? " + e.toString(), e);
        }
    }

    private void updateSettings() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(settings, Settings.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(CONFIG_FILE_NAME, resource);
        loadSettings();
    }

    @Override
    public void addConfigs() {
        addOrUpdateConfiguration(ODK_CONFIG);
        addOrUpdateConfiguration(ONA_CONFIG);
        addOrUpdateConfiguration(KOBO_CONFIG);
    }
}

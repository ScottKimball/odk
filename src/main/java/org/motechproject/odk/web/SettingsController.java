package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping(value = "/configs")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Configuration> getConfigs() {
        return settingsService.getAllConfigs();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addOrUpdateConfig(@RequestBody Configuration configuration) {
        settingsService.addOrUpdateConfiguration(configuration);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteConfig(@PathVariable("configName") String configName) {
        settingsService.removeConfiguration(configName);
    }
}

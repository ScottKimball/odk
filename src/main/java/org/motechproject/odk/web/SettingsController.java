package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    SettingsService settingsService;


    @RequestMapping(value = "/configs", method = RequestMethod.GET)
    @ResponseBody
    public List<Configuration> getConfigs() {
        return settingsService.getAllConfigs();
    }

    @RequestMapping(value = "/configs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addOrUpdateConfig(@RequestBody Configuration configuration) {
        settingsService.addOrUpdateConfiguration(configuration);
    }

    @RequestMapping(value = "/configs/all", method = RequestMethod.GET)
    public void addConfigs () {
        settingsService.addConfigs();
    }


}

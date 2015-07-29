package org.motechproject.odk.web;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.event.EventParameters;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.JsonParserFactory;
import org.motechproject.odk.service.ConfigurationService;
import org.motechproject.odk.service.FormDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/forms")
public class FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FormDefinitionService formDefinitionService;

    @Autowired
    private EventRelay eventRelay;


    @RequestMapping(value = "/{config}/{form}" ,method = RequestMethod.POST )
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@PathVariable("config") String config, @PathVariable("form") String form, @RequestBody String body) {
        LOGGER.debug("Received form: " + form + " Configuration: " + config );

        Configuration configuration = configurationService.getConfigByName(config);
        FormDefinition formDefinition = formDefinitionService.findByConfigurationNameAndTitle(config,form);

        if (configuration == null) {
            LOGGER.error("Configuration " + config + " does not exist");
            publishFailureEvent("Configuration " + config + " does not exist",null,config,form, body);

        } else if  (formDefinition == null) {
            LOGGER.error("Form " + form + " does not exist");
            publishFailureEvent("Form " + form + " does not exist",null,config,form, body);

        } else {
            publishEvent(body,configuration,formDefinition);
        }
    }

    private void publishEvent (String body, Configuration configuration, FormDefinition formDefinition) {
        JsonParser parser = new JsonParserFactory().getParser(body,formDefinition,configuration);

        try {
            MotechEvent event = parser.createEventFromJson();
            eventRelay.sendEventMessage(event);

        } catch (Exception e) {
            LOGGER.error("Publishing form reciept failure event:\n" + e.toString());
            publishFailureEvent("Error parsing JSON form data", e.toString(), configuration.getName(), formDefinition.getTitle(), body);
        }
    }

    private void publishFailureEvent (String message, String error, String configName, String formTitle, String body) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.CONFIGURATION_NAME, configName);
        params.put(EventParameters.FORM_TITLE, formTitle);
        params.put(EventParameters.MESSAGE, message);
        params.put(EventParameters.EXCEPTION, error);
        params.put(EventParameters.JSON_CONTENT,body);
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.FORM_FAIL,params));
    }

}

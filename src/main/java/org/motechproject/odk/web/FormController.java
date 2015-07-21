package org.motechproject.odk.web;


import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
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

@Controller
@RequestMapping(value = "/forms")
public class FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    FormDefinitionService formDefinitionService;

    @RequestMapping(value = "/{config}/{form}" ,method = RequestMethod.POST )
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@PathVariable("config") String config, @PathVariable("form") String form, @RequestBody String body) {
        LOGGER.debug("Recieved form: " + form + " Configuration: " + config );

        Configuration configuration = configurationService.getConfigByName(config);
        FormDefinition formDefinition = formDefinitionService.findByTitle(form);

        if (configuration == null) {
            LOGGER.error("Configuration " + config + " does not exist");
            //TODO publish form fail event

        } else if  (formDefinition == null) {
            LOGGER.error("Form " + form + " does not exist");
            //TODO publish form fail event

        } else {
            publishEvent(body,configuration,formDefinition);
        }

    }

    private void publishEvent (String body, Configuration configuration, FormDefinition formDefinition) {
        JsonParser parser = new JsonParserFactory().getParser(configuration.getType());
        try {
            MotechEvent event = parser.createEventFromJson(body, formDefinition, configuration.getName());

        } catch (Exception e) {
            //TODO publish form fail event
        }
    }

}

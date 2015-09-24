package org.motechproject.odk.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.OdkJsonFormPublication;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractJsonParser implements JsonParser {


    @Override
    public void parse(String json, EventRelay eventRelay, FormDefinition formDefinition, Configuration configuration) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        OdkJsonFormPublication publication = mapper.readValue(json,OdkJsonFormPublication.class );
        Map<String, Object> data = publication.getData()[0];
        for (FormElement formElement : formDefinition.getFormElements()) {

            Object value = data.get(formElement.getName());
            if (value != null) {
                value = formatValue(formElement.getType(), value);
                params.put(formElement.getName(), value);
            }
        }

        params.put(EventParameters.FORM_TITLE,formDefinition.getTitle());
        params.put(EventParameters.CONFIGURATION_NAME, configuration.getName());

        String subject = EventSubjects.RECEIVED_FORM + "." +  configuration.getName() + "." + formDefinition.getTitle();
        MotechEvent event = new MotechEvent(subject, params);
        eventRelay.sendEventMessage(event);
    }

    protected abstract Object formatValue(String type, Object value);


}

package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEventBuilder implements FormEventBuilder {


    @Override
    public MotechEvent build(String json, FormDefinition formDefinition, Configuration configuration) throws Exception {
        Map<String, Object> params = new HashMap<>();

        Map<String, Object> data = getData(json);
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
        return new MotechEvent(subject, params);
    }

    protected abstract Object formatValue(String type, Object value);
    protected abstract Map<String,Object> getData (String json) throws Exception;


}

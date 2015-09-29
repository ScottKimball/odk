package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEventBuilder implements EventBuilder {


    @Override
    public List<MotechEvent> createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws Exception {

        Map<String, Object> data = getData(json);
        List<MotechEvent> events = createRepeatGroupEvents(data, formDefinition, configuration);
        events.add(createFormEvent(data,formDefinition,configuration));

        return events;
    }

    private MotechEvent createFormEvent(Map<String, Object> data, FormDefinition formDefinition, Configuration configuration) {
        Map<String, Object> params = new HashMap<>();
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

    private List<MotechEvent> createRepeatGroupEvents(Map<String, Object> data, FormDefinition formDefinition, Configuration configuration) {

        List<FormValue> rootScope = getRootScope(data,formDefinition);
        return new ArrayList<>();
    }

    protected List<FormValue> getRootScope(Map<String,Object> data, FormDefinition formDefinition) {
        List<FormValue> formValues = new ArrayList<>();

        return formValues;
    }
    protected abstract Object formatValue(String type, Object value);
    protected abstract Map<String,Object> getData (String json) throws Exception;


}

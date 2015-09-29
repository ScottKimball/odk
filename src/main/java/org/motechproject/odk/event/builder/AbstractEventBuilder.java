package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;


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
                params.put(formElement.getName(),formatValue(formElement.getType(), value));
            }
        }

        params.put(EventParameters.FORM_TITLE, formDefinition.getTitle());
        params.put(EventParameters.CONFIGURATION_NAME, configuration.getName());

        String subject = EventSubjects.RECEIVED_FORM + "." +  configuration.getName() + "." + formDefinition.getTitle();
        return new MotechEvent(subject, params);
    }


    private List<MotechEvent> createRepeatGroupEvents(Map<String, Object> data, FormDefinition formDefinition, Configuration configuration) {
        List<MotechEvent> events = new ArrayList<>();

        Map<String,Object> rootScopeValues = getRootScope(data, formDefinition);
        List<FormElement> repeatGroups = getNonNestedRepeatGroups(formDefinition);

        for(FormElement repeatGroup : repeatGroups) {
            List<Map<String, Object>> repeatGroupInstances = (List<Map<String,Object>>) data.get(repeatGroup.getName());
            for (Map<String,Object> repeatGroupInstance : repeatGroupInstances ) {
                createRepeatGroupEvents(repeatGroup, events, formDefinition, repeatGroupInstance, rootScopeValues, configuration.getName());
            }
        }
        return events;
    }

    private Map<String,Object> getRootScope(Map<String,Object> data, FormDefinition formDefinition) {
        Map<String,Object> values = new HashMap<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isRepeatGroup() && !formElement.isPartOfRepeatGroup()) {

                Object value = data.get(formElement.getName());
                if (value != null) {
                    values.put(formElement.getName(), formatValue(formElement.getType(), value));
                }
            }
        }
        return values;
    }

    private List<FormElement> getNonNestedRepeatGroups(FormDefinition formDefinition) {
        List<FormElement> formElements = new ArrayList<>();
        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isPartOfRepeatGroup() && formElement.isRepeatGroup()) {
                formElements.add(formElement);
            }
        }
        return formElements;
    }

    private void createRepeatGroupEvents(FormElement repeatGroup, List<MotechEvent> events, FormDefinition formDefinition, Map<String, Object> data,
                                         Map<String, Object> scope, String configName) {
        
        List<FormElement> childRepeatGroups = new ArrayList<>();

        for (FormElement child : repeatGroup.getChildren()) {
            if (child.isRepeatGroup()) {
                childRepeatGroups.add(child);
            } else {
                Object value = data.get(child.getName());
                scope.put(child.getName(), formatValue(child.getType(), value));
            }
        }

        Map<String,Object> params = new HashMap<>();
        params.putAll(scope);
        String subject = EventSubjects.REPEAT_GROUP + "." + configName + "." + formDefinition.getTitle() + "." + repeatGroup.getName();
        events.add(new MotechEvent(subject, params));

        for(FormElement childRepeatGroup : childRepeatGroups) {

            List<Map<String, Object>> childRepeatGroupInstances = (List<Map<String,Object>>)data.get(childRepeatGroup.getName());

            for (Map<String,Object> childInstance : childRepeatGroupInstances) {
                createRepeatGroupEvents(childRepeatGroup, events, formDefinition, childInstance, scope, configName);
            }

        }
    }

    protected abstract Object formatValue(String type, Object value);
    protected abstract Map<String,Object> getData (String json) throws Exception;


}

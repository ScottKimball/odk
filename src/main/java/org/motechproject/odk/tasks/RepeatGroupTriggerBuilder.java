package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

public class RepeatGroupTriggerBuilder {

    private List<FormDefinition> formDefinitions;


    public RepeatGroupTriggerBuilder(List<FormDefinition> formDefinitions) {
        this.formDefinitions = formDefinitions;
    }

    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggerEventRequests = new ArrayList<>();

        for (FormDefinition formDefinition : formDefinitions) {
            triggerEventRequests.addAll(buildTriggersForFormDef(formDefinition));
        }
        return triggerEventRequests;
    }

    private List<TriggerEventRequest> buildTriggersForFormDef(FormDefinition formDefinition) {
        List<TriggerEventRequest> triggerEventRequests = new ArrayList<>();
        List<FormElement> rootScope = getRootScope(formDefinition);

        for(FormElement formElement : formDefinition.getFormElements()) {
            if(formElement.isRepeatGroup()) {
                triggerEventRequests.add(buildTriggerForRepeatGroup(formElement, rootScope, formDefinition.getTitle(), formDefinition.getConfigurationName()));
            }
        }
        return triggerEventRequests;
    }

    private List<FormElement> getRootScope(FormDefinition formDefinition) {
        List<FormElement> rootScope = new ArrayList<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if(!formElement.isRepeatGroup() && !formElement.isPartOfRepeatGroup()) {
                rootScope.add(formElement);
            }
        }
        return rootScope;
    }


    private TriggerEventRequest buildTriggerForRepeatGroup(FormElement repeatGroup, List<FormElement> rootScope, String title, String configName) {
        List<FormElement> scope = addFieldsToScope(repeatGroup,rootScope);
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        for(FormElement field : scope) {
            eventParameterRequests.add(new EventParameterRequest(field.getLabel(),field.getName(),TypeMapper.getType(field.getType())));
        }

        return new TriggerEventRequest(formatDisplayName(repeatGroup,title,configName),formatEventSubject(repeatGroup, title, configName), null,eventParameterRequests);
    }

    private List<FormElement> addFieldsToScope(FormElement formElement, List<FormElement> rootScope) {
        List<FormElement> scope = new ArrayList<>();
        scope.addAll(rootScope);

        while(formElement != null) {
            if(formElement.hasChildren()) {
                for(FormElement child : formElement.getChildren()) {
                    if(!child.isRepeatGroup()) {
                        scope.add(child);
                    }
                }
            }
            formElement = formElement.getParent();
        }
        return scope;
    }

    private String formatDisplayName(FormElement repeatGroup, String title, String configName) {
        return DisplayNames.REPEAT_GROUP + "[Configuration: " + configName + "][Title: " + title + "][Repeat Group: " + repeatGroup.getLabel() + "]";
    }

    private String formatEventSubject(FormElement repeatGroup, String title, String configName) {
        return EventSubjects.REPEAT_GROUP + "." + configName + "." + title + "." + repeatGroup.getName();
    }
}

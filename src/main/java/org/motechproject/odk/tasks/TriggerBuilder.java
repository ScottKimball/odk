package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.TasksDataTypes;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerBuilder {

    private List<FormDefinition> formDefinitions;
    private static final Map<String, String> TYPE_MAP = new HashMap<String,String>(){{
        put(FieldTypeConstants.STRING, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.DATE_TIME,TasksDataTypes.DATE);
        put(FieldTypeConstants.BOOLEAN, TasksDataTypes.BOOLEAN);
        put(FieldTypeConstants.INT, TasksDataTypes.INTEGER);
        put(FieldTypeConstants.DECIMAL, TasksDataTypes.DOUBLE);
        put(FieldTypeConstants.DATE, TasksDataTypes.DATE);
        put(FieldTypeConstants.TIME,TasksDataTypes.TIME);
        put(FieldTypeConstants.SELECT, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.SELECT_1, TasksDataTypes.LIST);
        put(FieldTypeConstants.GEOPOINT, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.GEOTRACE, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.GEOSHAPE, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.DOUBLE_ARRAY, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.STRING_ARRAY, TasksDataTypes.UNICODE);
        put(FieldTypeConstants.REPEAT_GROUP, TasksDataTypes.UNICODE);
    }};

    public TriggerBuilder( List<FormDefinition> formDefinitions) {
        this.formDefinitions = formDefinitions;
    }

    public List<TriggerEventRequest> buildTriggers () {
        List<TriggerEventRequest> triggerEventRequests = new ArrayList<>();

        for (FormDefinition formDefinition : formDefinitions) {
            List<EventParameterRequest> eventParameterRequests = buildEventParameterRequests(formDefinition);
            TriggerEventRequest eventRequest = new TriggerEventRequest("[" + formDefinition.getConfigurationName() + "] " + DisplayNames.TRIGGER_DISPLAY_NAME + " [" + formDefinition.getTitle() + "]",
                    EventSubjects.RECEIVED_FORM + "." +  formDefinition.getConfigurationName() + "." + formDefinition.getTitle(),null,eventParameterRequests);
            triggerEventRequests.add(eventRequest);
        }

        triggerEventRequests.add(buildFailureEventTrigger());
        return triggerEventRequests;
    }


    private List<EventParameterRequest> buildEventParameterRequests(FormDefinition formDefinition) {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        for (FormElement formElement : formDefinition.getFormElements()) {
            String type = TYPE_MAP.get(formElement.getType());

            if (type != null) {
                EventParameterRequest request = new EventParameterRequest(formElement.getLabel(), formElement.getName(),type);
                eventParameterRequests.add(request);
            }
        }
        eventParameterRequests.addAll(addTitleAndConfigFields(formDefinition));
        return eventParameterRequests;
    }

    private List<EventParameterRequest> addTitleAndConfigFields(FormDefinition formDefinition) {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.FORM_TITLE, EventParameters.FORM_TITLE,TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.CONFIGURATION_NAME,EventParameters.CONFIGURATION_NAME,TasksDataTypes.UNICODE));
        return eventParameterRequests;
    }


    private TriggerEventRequest buildFailureEventTrigger() {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.CONFIGURATION_NAME,EventParameters.CONFIGURATION_NAME,TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.EXCEPTION,EventParameters.EXCEPTION,TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.FORM_TITLE,EventParameters.FORM_TITLE,TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.MESSAGE,EventParameters.MESSAGE,TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.JSON_CONTENT,EventParameters.JSON_CONTENT, TasksDataTypes.UNICODE));

        return new TriggerEventRequest(DisplayNames.FORM_FAIL,EventSubjects.FORM_FAIL,null,eventParameterRequests);
    }
}

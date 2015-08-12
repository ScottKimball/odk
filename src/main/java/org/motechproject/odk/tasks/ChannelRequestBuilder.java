package org.motechproject.odk.tasks;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.event.EventParameters;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelRequestBuilder {
    private BundleContext bundleContext;
    private List<FormDefinition> formDefinitions;



    private static final String UNICODE = "UNICODE";
    private static final String DATE = "DATE";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String INTEGER = "INTEGER";
    private static final String DOUBLE = "DOUBLE";
    private static final String TIME = "TIME";
    private static final String LIST = "LIST";

    private static final Map<String, String> TYPE_MAP = new HashMap<String,String>(){{
        put(FieldTypeConstants.STRING, UNICODE);
        put(FieldTypeConstants.DATE_TIME,DATE);
        put(FieldTypeConstants.BOOLEAN, BOOLEAN);
        put(FieldTypeConstants.INT, INTEGER);
        put(FieldTypeConstants.DECIMAL, DOUBLE);
        put(FieldTypeConstants.DATE, DATE);
        put(FieldTypeConstants.TIME,TIME);
        put(FieldTypeConstants.SELECT, UNICODE);
        put(FieldTypeConstants.SELECT_1, LIST);
        put(FieldTypeConstants.GEOPOINT, UNICODE);
        put(FieldTypeConstants.GEOTRACE, UNICODE);
        put(FieldTypeConstants.GEOSHAPE, UNICODE);
        put(FieldTypeConstants.DOUBLE_ARRAY, UNICODE);
        put(FieldTypeConstants.STRING_ARRAY, UNICODE);
        put(FieldTypeConstants.REPEAT_GROUP, UNICODE);


    }};

    public ChannelRequestBuilder(BundleContext bundleContext, List<FormDefinition> formDefinitions) {
        this.bundleContext = bundleContext;
        this.formDefinitions = formDefinitions;
    }

    public ChannelRequest build () {
        List<TriggerEventRequest> triggers = buildTriggers();
        triggers.add(buildFailureEventTrigger());

        return new ChannelRequest(DisplayNames.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, triggers, new ArrayList<ActionEventRequest>());
    }

    private List<TriggerEventRequest> buildTriggers () {
        List<TriggerEventRequest> triggerEventRequests = new ArrayList<>();

        for (FormDefinition formDefinition : formDefinitions) {
            List<EventParameterRequest> eventParameterRequests = buildEventParameterRequests(formDefinition);
            TriggerEventRequest eventRequest = new TriggerEventRequest(DisplayNames.TRIGGER_DISPLAY_NAME + " [" + formDefinition.getTitle() + "]",
                    EventSubjects.RECEIVED_FORM + "." +  formDefinition.getConfigurationName() + "." + formDefinition.getTitle(),null,eventParameterRequests);
            triggerEventRequests.add(eventRequest);
        }
        return triggerEventRequests;
    }

    private List<EventParameterRequest> buildEventParameterRequests(FormDefinition formDefinition) {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        for (FormElement formElement : formDefinition.getFormElements()) {
            String type = TYPE_MAP.get(formElement.getType());

            if (type != null) {
                EventParameterRequest request = new EventParameterRequest(formElement.getName(), formElement.getName(),type);
                eventParameterRequests.add(request);
            }
        }
        return eventParameterRequests;
    }

    private TriggerEventRequest buildFailureEventTrigger() {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.CONFIGURATION_NAME,EventParameters.CONFIGURATION_NAME,UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.EXCEPTION,EventParameters.EXCEPTION,UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.FORM_TITLE,EventParameters.FORM_TITLE,UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.MESSAGE,EventParameters.MESSAGE,UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.JSON_CONTENT,EventParameters.JSON_CONTENT, UNICODE));

        return new TriggerEventRequest(DisplayNames.FORM_FAIL,EventSubjects.FORM_FAIL,null,eventParameterRequests);
    }

}

package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.constant.TasksDataTypes;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ActionBuilder {

    private List<FormDefinition> formDefinitions;
    private int count;

    public ActionBuilder(List<FormDefinition> formDefinitions) {
        this.count = 0;
        this.formDefinitions = formDefinitions;
    }

    public List<ActionEventRequest> build() {
        List<ActionEventRequest> actionEventRequests = new ArrayList<>();

        for(FormDefinition formDefinition : formDefinitions) {

            SortedSet<ActionParameterRequest> actionParameterRequests = createParameterRequestsForFormDef(formDefinition);
            ActionEventRequestBuilder builder = new ActionEventRequestBuilder();
            builder
                    .setDisplayName(DisplayNames.SAVE_FORM_INSTANCE + " [Configuration : " + formDefinition.getConfigurationName() + "]" + "[Title: " + formDefinition.getTitle() + "]")
                    .setActionParameters(actionParameterRequests)
                    .setSubject(EventSubjects.PERSIST_FORM_INSTANCE)
                    .setName(formDefinition.getConfigurationName() + "_"  + formDefinition.getTitle() + "_" + EventSubjects.PERSIST_FORM_INSTANCE);
            actionEventRequests.add(builder.createActionEventRequest());
        }

        return actionEventRequests;
    }

    private SortedSet<ActionParameterRequest> createParameterRequestsForFormDef(FormDefinition formDefinition) {
        ;
        SortedSet<ActionParameterRequest> actionParameterRequests = createRequiredFields();
        List<FormElement> formElements = formDefinition.getFormElements();
        ActionParameterRequestBuilder builder;
        for(FormElement formElement : formElements) {
            builder = new ActionParameterRequestBuilder();
            builder
                    .setDisplayName(formElement.getLabel())
                    .setKey(formElement.getName())
                    .setOrder(count++)
                    .setType(TypeMapper.getType(formElement.getType()));
            actionParameterRequests.add(builder.createActionParameterRequest());
        }

        return actionParameterRequests;
    }

    private SortedSet<ActionParameterRequest> createRequiredFields() {
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<ActionParameterRequest>();
        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.FORM_TITLE)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.FORM_TITLE)
                .setOrder(count++)
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setOrder(count++)
                .setType(TasksDataTypes.UNICODE)
                .setKey(EventParameters.CONFIGURATION_NAME)
                .setRequired(true);

        actionParameterRequests.add(builder.createActionParameterRequest());

        return actionParameterRequests;
    }
}

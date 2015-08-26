package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.constant.TasksDataTypes;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
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



    public ChannelRequestBuilder(BundleContext bundleContext, List<FormDefinition> formDefinitions) {
        this.bundleContext = bundleContext;
        this.formDefinitions = formDefinitions;
    }

    public ChannelRequest build () {
        TriggerBuilder triggerBuilder = new TriggerBuilder(formDefinitions);
        List<TriggerEventRequest> triggers = triggerBuilder.buildTriggers();
        ActionBuilder actionBuilder = new ActionBuilder(formDefinitions);
        List<ActionEventRequest> actions = actionBuilder.build();
        return new ChannelRequest(DisplayNames.CHANNEL_DISPLAY_NAME, bundleContext.getBundle().getSymbolicName(),
                bundleContext.getBundle().getVersion().toString(), null, triggers,actions);
    }
}

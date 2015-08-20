package org.motechproject.odk.event;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElementValue;
import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.FormInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EventHandler {

    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    FormInstanceService formInstanceService;


    @MotechListener(subjects = EventSubjects.PERSIST_FORM_INSTANCE)
    public void handlePersistForm(MotechEvent event) {
        Map<String,Object> params = event.getParameters();
        String title = (String) params.get(EventParameters.FORM_TITLE);
        String configName = (String) params.get(EventParameters.CONFIGURATION_NAME);
        FormDefinition formDefinition = formDefinitionService.findByConfigurationNameAndTitle(configName,title);
        if (formDefinition == null) {
            publishPersistFailEvent(event);
        }

        List<FormElementValue> formElementValues = new ArrayList<>();

        /*iterate over form definition form elements; get value off params; construct formElementValue; add to list */


        FormInstance formInstance = new FormInstance(title);
        formInstance.setFormElementValues(formElementValues);


    }

    private void publishPersistFailEvent(MotechEvent event) {

    }
}

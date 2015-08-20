package org.motechproject.odk.event;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.odk.service.FormDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventHandler {

    @Autowired
    FormDefinitionService formDefinitionService;


    @MotechListener(subjects = EventSubjects.PERSIST_FORM)
    public void handlePersistForm(MotechEvent event) {
        Map<String,Object> params = event.getParameters();

    }
}

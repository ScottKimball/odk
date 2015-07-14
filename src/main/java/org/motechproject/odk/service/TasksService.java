package org.motechproject.odk.service;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;

import java.util.List;

public interface TasksService {

    void updateTasksChannel(List<FormDefinition> formDefinitions, Configuration configuration);
}

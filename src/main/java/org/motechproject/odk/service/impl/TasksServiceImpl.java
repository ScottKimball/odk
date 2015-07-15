package org.motechproject.odk.service.impl;

import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.tasks.ChannelRequestBuilder;
import org.motechproject.tasks.service.ChannelService;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksServiceImpl implements TasksService {

    private ChannelService channelService;
    private BundleContext bundleContext;

    @Autowired
    public TasksServiceImpl(ChannelService channelService, BundleContext bundleContext) {
        this.channelService = channelService;
        this.bundleContext = bundleContext;
    }

    @Override
    public void updateTasksChannel(List<FormDefinition> formDefinitions, Configuration configuration) {
        ChannelRequestBuilder channelRequestBuilder = new ChannelRequestBuilder(bundleContext, formDefinitions, configuration);
        channelService.registerChannel(channelRequestBuilder.build());
    }
}

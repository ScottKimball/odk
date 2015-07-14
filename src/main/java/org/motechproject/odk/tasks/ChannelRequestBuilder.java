package org.motechproject.odk.tasks;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.tasks.contract.ChannelRequest;
import org.osgi.framework.BundleContext;

import java.util.List;

public class ChannelRequestBuilder {
    private BundleContext bundleContext;
    private List<FormDefinition> formDefinitions;


    public ChannelRequestBuilder(BundleContext bundleContext, List<FormDefinition> formDefinitions) {
        this.bundleContext = bundleContext;
        this.formDefinitions = formDefinitions;
    }

    public ChannelRequest build () {
        return null;
    }
}

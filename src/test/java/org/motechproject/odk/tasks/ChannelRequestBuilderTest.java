package org.motechproject.odk.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.tasks.ChannelRequestBuilder;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRequestBuilderTest {

    @Mock
    private BundleContext bundleContext;

    @Mock
    private Bundle bundle;

    @Mock
    private Version version;

    @Before
    public void setup () {
        when(bundleContext.getBundle()).thenReturn(bundle);
        when(bundle.getVersion()).thenReturn(version);
        when(bundle.getSymbolicName()).thenReturn("BundleSymbolicName");
        when(version.toString()).thenReturn("bundleVersion");
    }

    @Test
    public void testMapEventTypes () {
        List<FormDefinition> formDefinitionList = new ArrayList<>();
        FormDefinition definition = new FormDefinition();
        definition.setTitle("formTitle");
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("stringfield", "string"));
        formFields.add(new FormField("intfield", "int"));
        formFields.add(new FormField("selectfield", "select"));
        formFields.add(new FormField("select1field", "select1"));
        formFields.add(new FormField("datefield", "date"));
        formFields.add(new FormField("datetimefield", "dateTime"));
        formFields.add(new FormField("decimalfield", "decimal"));
        formFields.add(new FormField("timefield", "time"));
        formFields.add(new FormField("binaryField", "binary"));

        definition.setFormFields(formFields);
        formDefinitionList.add(definition);
        Configuration configuration = new Configuration(null,null,null,"configuration",null);
        ChannelRequestBuilder builder = new ChannelRequestBuilder(bundleContext,formDefinitionList,configuration);
        ChannelRequest request = builder.build();
        assertNotNull(request);
        List<EventParameterRequest> eventParameters = request.getTriggerTaskEvents().get(0).getEventParameters();
        assertEquals(eventParameters.get(0).getType(), "UNICODE");
        assertEquals(eventParameters.get(1).getType(), "INTEGER");
        assertEquals(eventParameters.get(2).getType(), "UNICODE");
        assertEquals(eventParameters.get(3).getType(), "LIST");
        assertEquals(eventParameters.get(4).getType(), "DATE");
        assertEquals(eventParameters.get(5).getType(), "DATE");
        assertEquals(eventParameters.get(6).getType(), "DOUBLE");
        assertEquals(eventParameters.get(7).getType(), "TIME");
        assertEquals(eventParameters.size(),8);
    }
}

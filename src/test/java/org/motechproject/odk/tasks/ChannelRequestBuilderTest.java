package org.motechproject.odk.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
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
        List<FormElement> formElements = new ArrayList<>();
        formElements.add(new FormElement("stringfield", "string"));
        formElements.add(new FormElement("intfield", "int"));
        formElements.add(new FormElement("selectfield", "select"));
        formElements.add(new FormElement("select1field", "select1"));
        formElements.add(new FormElement("datefield", "date"));
        formElements.add(new FormElement("datetimefield", "dateTime"));
        formElements.add(new FormElement("decimalfield", "decimal"));
        formElements.add(new FormElement("timefield", "time"));
        formElements.add(new FormElement("binaryField", "binary"));

        definition.setFormElements(formElements);
        formDefinitionList.add(definition);
        ChannelRequestBuilder builder = new ChannelRequestBuilder(bundleContext,formDefinitionList);
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

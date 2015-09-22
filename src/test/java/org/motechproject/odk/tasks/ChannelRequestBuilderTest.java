package org.motechproject.odk.tasks;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.impl.XformParserODK;
import org.motechproject.tasks.contract.ChannelRequest;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import java.io.File;
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
        formElements.add(new FormElement("stringfield","stringfield", "string"));
        formElements.add(new FormElement("intfield","intfield", "int"));
        formElements.add(new FormElement("selectfield", "selectfield","select"));
        formElements.add(new FormElement("select1field","select1field", "select1"));
        formElements.add(new FormElement("datefield","datefield", "date"));
        formElements.add(new FormElement("datetimefield","datetimefield", "dateTime"));
        formElements.add(new FormElement("decimalfield","decimalfield", "decimal"));
        formElements.add(new FormElement("timefield","timefield", "time"));
        formElements.add(new FormElement("binaryField","binaryField", "binary"));

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

    @Test
    public void testNestedRepeatGroups() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/nested_repeat.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        List<FormDefinition> formDefinitions = new ArrayList<>();
        formDefinitions.add(formDefinition);

        ChannelRequestBuilder builder = new ChannelRequestBuilder(bundleContext, formDefinitions);
        builder.build();
    }
}

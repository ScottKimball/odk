package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.impl.XformParserODK;

import java.io.File;

public class XformParserODKTest {

    @Test
    public void testRepeatWithCount() throws Exception {

        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/tutorialRepeatWithCount.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

    }

    @Test
    public void testWidgets() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/widgets.xml").getFile());

        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

    }

    @Test
    public void testTestDoc() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/test.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

    }

    @Test
    public void testDemoForm() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/demoForm.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

    }

    @Test
    public void testNestedRepeatGroups() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/nested_repeat.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
    }
}

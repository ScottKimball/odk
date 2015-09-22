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
    public void testRepeatWithCount () throws Exception {

        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/tutorialRepeatWithCount.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());

    }

    @Test
    public void testWidgets () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/widgets.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }

    }

    @Test
    public void testTestDoc () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/test.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }
    }

    @Test
    public void testDemoForm () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/demoForm.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }
    }

    @Test
    public void testNestedRepeatGroups() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/nested_repeat.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserODK().parse(xml, configuration.getName());
    }
}

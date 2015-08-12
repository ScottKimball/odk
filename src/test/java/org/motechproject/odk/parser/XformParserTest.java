package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class XformParserTest {

    @Test
    public void testRepeatWithCount () throws Exception {

        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/tutorialRepeatWithCount.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = XformParser.parse(xml, configuration.getName());

    }

    @Test
    public void testWidgets () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/widgets.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = XformParser.parse(xml, configuration.getName());
        for (FormField formField : formDefinition.getFormFields()) {
            System.out.println(formField.getName() + "\t" + formField.getType());
        }

    }

    @Test
    public void testTestDoc () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/test.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = XformParser.parse(xml, configuration.getName());
        for (FormField formField : formDefinition.getFormFields()) {
            System.out.println(formField.getName() + "\t" + formField.getType());
        }
    }

    @Test
    public void testDemoForm () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/demoForm.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = XformParser.parse(xml, configuration.getName());
        for (FormField formField : formDefinition.getFormFields()) {
            System.out.println(formField.getName() + "\t" + formField.getType());
        }
    }
}

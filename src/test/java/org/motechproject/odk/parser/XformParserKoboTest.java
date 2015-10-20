package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.impl.XformParserKobo;

import java.io.File;

public class XformParserKoboTest {

    @Test
    public void testKoboTestForm() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File(getClass().getResource("/kobotestform.xml").getFile());
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserKobo().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }
    }

    @Test
    public void testKoboTestForm2() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");


        File f = new File(getClass().getResource("/koboform2.xml").getFile());
        String xml = FileUtils.readFileToString(f);

        xml = xml.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("<root><?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "")
                .replace("\n</root>", "")
                .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "");

        FormDefinition formDefinition = new XformParserKobo().parse(xml, configuration.getName());

    }
}

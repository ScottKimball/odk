package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.apache.xerces.parsers.DOMParser;
import org.junit.Test;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.parser.impl.XformParserKobo;
import org.w3c.dom.Document;

import java.io.File;

public class XformParserKoboTest {

    @Test
    public void testKoboTestForm () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/kobotestform.xml");
        String xml = FileUtils.readFileToString(f);
        FormDefinition formDefinition = new XformParserKobo().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }
    }

    @Test
    public void testKoboTestForm2 () throws Exception {
        Configuration configuration = new Configuration();
        configuration.setName("configName");

        File f = new File("odk/src/test/resources/koboform2.xml");
        String xml = FileUtils.readFileToString(f);
      //  DOMParser parser = new DOMParser();
       // parser.parse(xml);
       // Document document = parser.getDocument();
        xml = xml.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("<root><?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "")
                .replace("\n</root>" , "")
                .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "");

        FormDefinition formDefinition = new XformParserKobo().parse(xml, configuration.getName());
        for (FormElement formElement : formDefinition.getFormElements()) {
            System.out.println(formElement.getName() + "\t" + formElement.getType());
        }
    }
}

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
}

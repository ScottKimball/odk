package org.motechprojext.odk.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.JsonParserODK;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonParserODKTest {

    @Test
    public void testParseJson() throws Exception{
        File f = new File("odk/src/test/resources/test-widgets.json");
        String json = FileUtils.readFileToString(f);

        JsonParser parser = new JsonParserODK();
        FormDefinition formDefinition = createFormDefinition();

        MotechEvent motechEvent = parser.createEventFromJson(json,formDefinition);

    }

    private FormDefinition createFormDefinition () {
        FormDefinition formDefinition = new FormDefinition("Widgets");
        formDefinition.setConfigurationName("odk");
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("my_string", "string"));
        formFields.add(new FormField("my_long_text", "string"));
        formFields.add(new FormField("my_int", "int"));
        formDefinition.setFormFields(formFields);
        return formDefinition;




    }
}

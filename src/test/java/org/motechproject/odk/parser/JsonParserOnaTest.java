package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.ConfigurationType;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonParserOnaTest {

    @Test
    public void testParseJson() throws Exception{
        File f = new File("odk/src/test/resources/test-widgets-ona.json");
        String json = FileUtils.readFileToString(f);

        FormDefinition formDefinition = createFormDefinition();
        Configuration configuration = new Configuration("http://domainname.com", "username", "password", "configname", ConfigurationType.ONA, "namespace");
        JsonParser parser = new JsonParserOna(json, formDefinition, configuration);
        MotechEvent motechEvent = parser.createEventFromJson();
    }


    private FormDefinition createFormDefinition () {
        FormDefinition formDefinition = new FormDefinition("Widgets");
        formDefinition.setTitle("formTitle");
        formDefinition.setConfigurationName("odk");
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("text_widgets/my_string", "string"));
        formFields.add(new FormField("text_widgets/my_long_text", "string"));
        formFields.add(new FormField("number_widgets/my_int", "int"));
        formFields.add(new FormField("cascading_widgets/group1/country", "select1"));
        formFields.add(new FormField("select_widgets/select_horizontal", "select"));
        formFields.add(new FormField("geopoint_widgets/my_geoshape", "geoshape"));
        formFields.add(new FormField("media_widgets/my_image", "binary"));
        formDefinition.setFormFields(formFields);
        return formDefinition;
    }
}

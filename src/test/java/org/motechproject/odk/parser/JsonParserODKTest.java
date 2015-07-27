package org.motechproject.odk.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.event.EventSubjects;
import org.motechproject.odk.parser.JsonParser;
import org.motechproject.odk.parser.JsonParserODK;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonParserODKTest {

    @Test
    public void testParseJson() throws Exception{
        File f = new File("odk/src/test/resources/test-widgets.json");
        String json = FileUtils.readFileToString(f);

        JsonParser parser = new JsonParserODK();
        FormDefinition formDefinition = createFormDefinition();

        MotechEvent motechEvent = parser.createEventFromJson(json, formDefinition, "configName");
        assertNotNull(motechEvent);
        assertEquals(motechEvent.getSubject(), EventSubjects.RECEIVED_FORM + ".configName." + formDefinition.getTitle());
        Map<String,Object> params = motechEvent.getParameters();
        assertEquals(params.get("my_string"), "string");
        assertEquals(params.get("my_long_text"), "long\ntext");
        assertEquals(params.get("my_int"),6);
        assertEquals(params.get("select_horizontal"), "3\n5\n8");
        assertEquals(params.get("my_geoshape"), "7.9377 -11.5845 0.0 0.0" +
                " 7.9324 -11.5902 0.0 0.0" +
                " 7.927 -11.5857 0.0 0.0" +
                " 7.925 -11.578 0.0 0.0" +
                " 7.9267 -11.5722 0.0 0.0" +
                " 7.9325 -11.5708 0.0 0.0" +
                " 7.9372 -11.5737 0.0 0.0" +
                " 7.9393 -11.579 0.0 0.0" +
                " 7.9377 -11.5845 0.0 0.0");
        assertEquals(params.get("my_image"), "http://motech-test01.rcg.usm.maine.edu:8080/ODKAggregate/view/binaryData" +
                "?blobKey=widgets%5B%40version%3Dnull+and+%40uiVersion%3Dnull%5D%2Fwidgets%5B%40key%3Duuid%3Affb7423d-" +
                "2f1c-47c1-ab03-e9f97be15293%5D%2Fmedia_widgets%3Amy_image" );

    }

    private FormDefinition createFormDefinition () {
        FormDefinition formDefinition = new FormDefinition("Widgets");
        formDefinition.setTitle("formTitle");
        formDefinition.setConfigurationName("odk");
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("my_string", "string"));
        formFields.add(new FormField("my_long_text", "string"));
        formFields.add(new FormField("my_int", "int"));
        formFields.add(new FormField("country", "select1"));
        formFields.add(new FormField("select_horizontal", "select"));
        formFields.add(new FormField("my_geoshape", "geoshape"));
        formFields.add(new FormField("my_image", "binary"));
        formDefinition.setFormFields(formFields);
        return formDefinition;




    }
}

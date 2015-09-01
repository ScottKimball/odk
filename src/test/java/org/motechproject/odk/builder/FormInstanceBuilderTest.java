package org.motechproject.odk.builder;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.domain.builder.FormInstanceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormInstanceBuilderTest {


    private static final String CONFIG_NAME = "configname";
    private static final String TITLE = "title";
    private static final String CHILD_1 = "child1";
    private static final String CHILD_2 = "child2";
    private static final String INNER_GROUP = "innerGroup";
    private static final String CHILD_OUTER_GROUP = "childOuterGroup";
    private static final String FORM_ELEMENT_OUTER_GROUP = "formElementOuterGroup";
    private static final String FORM_ELEMENT_STRING = "formElementString";

    @Test
    public void testBuildFormInstanceWithRepeatGroups() throws Exception {

        FormDefinition formDefinition = buildFormDefWithGroups();
        Map<String, Object> params = buildParams();
        FormInstanceBuilder builder = new FormInstanceBuilder(formDefinition,params,"instanceID");
        FormInstance formInstance = builder.build();
        System.out.println();


    }

    private FormDefinition buildFormDefWithGroups() {
        FormDefinition formDefinition = new FormDefinition();

        formDefinition.setConfigurationName(CONFIG_NAME);
        formDefinition.setTitle(TITLE);

        FormElement formElementChild1 = new FormElement();
        formElementChild1.setName(CHILD_1);
        formElementChild1.setLabel("child 1");
        formElementChild1.setType(FieldTypeConstants.STRING);

        FormElement formElementChild2 = new FormElement();
        formElementChild2.setName(CHILD_2);
        formElementChild2.setLabel("child 2");
        formElementChild2.setType(FieldTypeConstants.STRING);

        FormElement formElementInnerGroup = new FormElement();
        formElementInnerGroup.setName(INNER_GROUP);
        formElementInnerGroup.setType("Inner Group");
        formElementInnerGroup.setType(FieldTypeConstants.REPEAT_GROUP);
        List<FormElement> formElements = new ArrayList<>();
        formElements.add(formElementChild1);
        formElements.add(formElementChild2);
        formElementInnerGroup.setChildren(formElements);

        List<FormElement> outerGroupFormElements = new ArrayList<>();

        FormElement childOuterGroup = new FormElement();
        childOuterGroup.setName(CHILD_OUTER_GROUP);
        childOuterGroup.setType("Child Outer Group");
        childOuterGroup.setType(FieldTypeConstants.STRING);

        FormElement formElementOuterGroup = new FormElement();
        formElementOuterGroup.setName(FORM_ELEMENT_OUTER_GROUP);
        formElementOuterGroup.setLabel("Form Element Outer Group");
        formElementOuterGroup.setType(FieldTypeConstants.REPEAT_GROUP);

        outerGroupFormElements.add(childOuterGroup);
        outerGroupFormElements.add(formElementInnerGroup);
        formElementOuterGroup.setChildren(outerGroupFormElements);

        List<FormElement> formDefElements = new ArrayList<>();

        FormElement formElementString = new FormElement();
        formElementString.setName(FORM_ELEMENT_STRING);
        formElementString.setLabel("form element String");
        formElementString.setType(FieldTypeConstants.STRING);
        formDefElements.add(formElementString);
        formDefElements.add(formElementOuterGroup);

        formDefinition.setFormElements(formDefElements);

        return formDefinition;

    }

    private Map<String,Object> buildParams() throws Exception{
        Map<String, Object> innerGroup = new HashMap<>();
        innerGroup.put(CHILD_1, "child 1");
        innerGroup.put(CHILD_2, "child 2");

        Map<String, Object> formElementOuterGroup = new HashMap<>();
        formElementOuterGroup.put(INNER_GROUP, innerGroup);
        formElementOuterGroup.put(CHILD_OUTER_GROUP, "child outer group");

        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.CONFIGURATION_NAME, CONFIG_NAME);
        params.put(EventParameters.FORM_TITLE, TITLE);
        String json = new ObjectMapper().writeValueAsString(formElementOuterGroup);
        params.put(FORM_ELEMENT_OUTER_GROUP, json);
        params.put(FORM_ELEMENT_STRING, "string value");

        return params;
    }
}

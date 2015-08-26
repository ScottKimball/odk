package org.motechproject.odk.builder;

import org.junit.Test;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;

import java.util.ArrayList;
import java.util.List;

public class FormInstanceBuilderTest {

    @Test
    public void testBuildFormInstanceWithRepeatGroups() throws Exception {



    }

    private FormDefinition buildFormDefWithGroups() {
        FormDefinition formDefinition = new FormDefinition();

        formDefinition.setConfigurationName("configname");
        formDefinition.setTitle("title");

        FormElement formElementChild1 = new FormElement();
        formElementChild1.setName("child1");
        formElementChild1.setLabel("child 1");
        formElementChild1.setType(FieldTypeConstants.STRING);

        FormElement formElementChild2 = new FormElement();
        formElementChild2.setName("child2");
        formElementChild2.setLabel("child 2");
        formElementChild2.setType(FieldTypeConstants.STRING);

        FormElement formElementInnerGroup = new FormElement();
        formElementInnerGroup.setName("innerGroup");
        formElementInnerGroup.setType("Inner Group");
        formElementInnerGroup.setType(FieldTypeConstants.REPEAT_GROUP);
        List<FormElement> formElements = new ArrayList<>();
        formElements.add(formElementChild1);
        formElements.add(formElementChild2);
        formElementInnerGroup.setChildren(formElements);

        List<FormElement> outerGroupFormElements = new ArrayList<>();

        FormElement childOuterGroup = new FormElement();
        childOuterGroup.setName("childOuterGroup");
        childOuterGroup.setType("Child Outer Group");
        childOuterGroup.setType(FieldTypeConstants.STRING);

        FormElement formElementOuterGroup = new FormElement();
        formElementOuterGroup.setName("formElementOuterGroup");
        formElementOuterGroup.setLabel("Form Element Outer Group");
        formElementOuterGroup.setType(FieldTypeConstants.REPEAT_GROUP);

        outerGroupFormElements.add(childOuterGroup);
        outerGroupFormElements.add(formElementInnerGroup);
        formElementOuterGroup.setChildren(outerGroupFormElements);

        List<FormElement> formDefElements = new ArrayList<>();
        formDefElements.add(formElementOuterGroup);

        formDefinition.setFormElements(formDefElements);

        return formDefinition;





    }
}

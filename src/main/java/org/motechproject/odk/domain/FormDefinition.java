package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FormDefinition {

    @Field
    private String title;

    @Field
    private List<FormElement> formElements;

    @Field
    private String configurationName;

    @Field(type = "TEXT")
    private String xForm;

    public FormDefinition(String configurationName) {
        this.configurationName = configurationName;
    }

    public FormDefinition() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormElement> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<FormElement> formElements) {
        this.formElements = formElements;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getxForm() {
        return xForm;
    }

    public void setxForm(String xForm) {
        this.xForm = xForm;
    }
}

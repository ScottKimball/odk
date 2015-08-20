package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FormInstance {

    @Field
    private String title;

    @Field
    private List<FormElementValue> formElementValues;

    public FormInstance(String title) {
        this.title = title;
    }

    public FormInstance() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormElementValue> getFormElementValues() {
        return formElementValues;
    }

    public void setFormElementValues(List<FormElementValue> formElementValues) {
        this.formElementValues = formElementValues;
    }
}

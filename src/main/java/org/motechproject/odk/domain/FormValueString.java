package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class FormValueString extends FormValue {

    @Field
    private String value;


    public FormValueString(String name, String label, String type, String value) {
        super(name, label, type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
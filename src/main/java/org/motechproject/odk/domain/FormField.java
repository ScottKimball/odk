package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class FormField {

    @Field
    private String name;

    @Field
    private String type;

    public FormField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public FormField() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

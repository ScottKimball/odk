package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FormField {

    @Field
    private String name;

    @Field
    private String type;

    @Field
    private List<FormField> children;


    public FormField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean hasChildren () {
        return children != null;
    }

    public FormField(String name) {
        this.name = name;
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


    public List<FormField> getChildren() {
        return children;
    }

    public void setChildren(List<FormField> children) {
        this.children = children;
    }

}

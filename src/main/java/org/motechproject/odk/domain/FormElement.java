package org.motechproject.odk.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity
public class FormElement {

    @Field
    private String name;

    @Field
    private String type;

    @Field
    private List<FormElement> children;

    @Field
    private FormElement parent;


    public FormElement(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean hasChildren () {
        return children != null;
    }

    public FormElement(String name) {
        this.name = name;
    }

    public FormElement() {
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


    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public FormElement getParent() {
        return parent;
    }

    public void setParent(FormElement parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return getParent() != null;
    }
}
package org.motechproject.odk.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.odk.constant.FieldTypeConstants;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchPlan;
import javax.jdo.annotations.Persistent;
import java.util.List;

@Entity
public class FormElement {

    @Field
    private String name;

    @Field
    private String label;

    @Field
    private String type;

    @Field
    private List<FormElement> children;

    @Field
    private boolean partOfRepeatGroup;

    private FormElement parent;



    public FormElement(String name, String label, String type, List<FormElement> children, boolean partOfRepeatGroup, FormElement parent) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.children = children;
        this.partOfRepeatGroup = partOfRepeatGroup;
        this.parent = parent;
    }

    public FormElement () {}

    public boolean hasChildren () {
        return children != null;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isPartOfRepeatGroup() {
        return partOfRepeatGroup;
    }

    public boolean isRepeatGroup() {
        return getType().equals(FieldTypeConstants.REPEAT_GROUP);
    }


    public void setPartOfRepeatGroup(boolean partOfRepeatGroup) {
        this.partOfRepeatGroup = partOfRepeatGroup;
    }
}

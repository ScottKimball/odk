package org.motechproject.odk.domain.builder;

import org.motechproject.odk.domain.FormElement;

import java.util.List;

public class FormElementBuilder {

    private String name;
    private String label;
    private String type;
    private List<FormElement> children;
    private boolean partOfRepeatGroup;
    private FormElement parent;


    public FormElementBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public FormElementBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public FormElementBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public FormElementBuilder setChildren(List<FormElement> children) {
        this.children = children;
        return this;
    }

    public FormElementBuilder setPartOfRepeatGroup(boolean partOfRepeatGroup) {
        this.partOfRepeatGroup = partOfRepeatGroup;
        return this;
    }

    public FormElementBuilder setParent(FormElement parent) {
        this.parent = parent;
        return this;
    }

    public FormElement createFormElement() {
        return new FormElement(name,label,type,children,partOfRepeatGroup, parent);
    }
}

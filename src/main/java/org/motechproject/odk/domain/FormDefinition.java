package org.motechproject.odk.domain;

import java.util.List;

public class FormDefinition {

    private String title;
    private List<FormField> formFields;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }

    public static class FormField {
        private String name;
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
}

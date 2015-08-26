package org.motechproject.odk.event;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormValue;
import org.motechproject.odk.domain.FormValueDouble;
import org.motechproject.odk.domain.FormValueInteger;
import org.motechproject.odk.domain.FormValueString;
import org.motechproject.odk.domain.FormInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormInstanceBuilder {

    private String title;
    private String configName;
    private FormDefinition formDefinition;
    private Map<String, Object> params;


    public FormInstanceBuilder(String title, String configName, FormDefinition formDefinition, Map<String, Object> params) {
        this.title = title;
        this.configName = configName;
        this.formDefinition = formDefinition;
        this.params = params;
    }

    public FormInstance build() {
        List<FormValue> formValues = new ArrayList<>();
        List<FormElement> formElements = formDefinition.getFormElements();

        for (FormElement formElement : formElements) {
            Object value = params.get(formElement.getName());

            if (value != null) {
                formValues.add(buildFormElementValueByType(formElement,value));
            }
        }

        FormInstance formInstance = new FormInstance(title, configName);
        formInstance.setFormValues(formValues);
        return formInstance;
    }

    private FormValue buildFormElementValueByType(FormElement formElement, Object value) {

        switch(formElement.getType()) {

            /*
            case FieldTypeConstants.DATE_TIME:
                return null; //TODO

            case FieldTypeConstants.BOOLEAN:
                return null; //TODO

            case FieldTypeConstants.DATE:
                return null;

            case FieldTypeConstants.TIME:
                return null;

            */

            case FieldTypeConstants.REPEAT_GROUP :
                return buildGroup(formElement, value);
            case FieldTypeConstants.INT:
                return new FormValueInteger(formElement.getName(),formElement.getLabel(), formElement.getType(),(Integer) value);

            case FieldTypeConstants.DECIMAL:
                return new FormValueDouble(formElement.getName(),formElement.getLabel(),formElement.getType(), (Double) value);

            default:
                return new FormValueString(formElement.getName(),formElement.getLabel(), formElement.getType(),(String) value);
        }


    }

    private FormValue buildGroup (FormElement formElement, Object value)  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> data = mapper.readValue((String) value,new TypeReference<HashMap<String,Object>>() {} );

        } catch (Exception e) {

        }

        return null;
    }


}

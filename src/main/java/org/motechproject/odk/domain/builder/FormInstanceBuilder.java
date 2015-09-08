package org.motechproject.odk.domain.builder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.FormValue;
import org.motechproject.odk.domain.FormValueDouble;
import org.motechproject.odk.domain.FormValueGroup;
import org.motechproject.odk.domain.FormValueInteger;
import org.motechproject.odk.domain.FormValueString;
import org.motechproject.odk.domain.FormInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormInstanceBuilder {

    private FormDefinition formDefinition;
    private Map<String, Object> params;
    private String instanceId;


    public FormInstanceBuilder(FormDefinition formDefinition, Map<String, Object> params, String instanceId) {
        this.formDefinition = formDefinition;
        this.params = params;
        this.instanceId = instanceId;
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

        FormInstance formInstance = new FormInstance(formDefinition.getTitle(), formDefinition.getConfigurationName(), instanceId);
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
                return null;//TODO

            case FieldTypeConstants.TIME:
                return null;//TODO

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

    private FormValueGroup buildGroup (FormElement formElement, Object value)  {
        List<Map<String,Object>> data;

        if (value instanceof String) {
            data = jsonToMap((String)value);

        } else {
            data = (List<Map<String,Object>>) value;
        }

        List<FormValue> children = new ArrayList<>();
        for(Map<String,Object> map : data) {

            for (Map.Entry pair: map.entrySet()) {
                FormElement element = findFormElementByName(pair.getKey().toString());
                Object pairValue = pair.getValue();
                children.add(buildFormElementValueByType(element,pairValue));
            }

        }

        return new FormValueGroup(formElement.getName(),formElement.getLabel(),formElement.getType(),children);
    }

    private List<Map<String,Object>> jsonToMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue((String) json, new TypeReference<List<Map<String,Object>>>() {});

        } catch (Exception e) {
            return null;
        }
    }

    private FormElement findFormElementByName(String name) {
        for (FormElement formElement : formDefinition.getFormElements()) {
           FormElement element =  recursivelyFindFormElementByName(formElement,name);

            if (element != null) {
                return element;
            }
        }

        return null;
    }

    private FormElement recursivelyFindFormElementByName(FormElement formElement, String name) {

        if (formElement.getName().equals(name)) {
            return formElement;

        } else if (formElement.hasChildren()) {

            for (FormElement child : formElement.getChildren()) {
                FormElement element = recursivelyFindFormElementByName(child, name);

                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }


}
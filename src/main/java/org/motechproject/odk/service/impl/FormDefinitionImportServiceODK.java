package org.motechproject.odk.service.impl;

import org.motechproject.odk.constant.OnaConstants;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.builder.FormElementBuilder;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import org.motechproject.odk.constant.ODKConstants;


@Service
public class FormDefinitionImportServiceODK extends AbstractFormDefinitionImportService implements FormDefinitionImportService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FormDefinitionImportServiceODK.class);

    private static final String LATITUDE = ":Latitude";
    private static final String LONGITUDE = ":Longitude";
    private static final String ALTITUDE = ":Altitude";
    private static final String ACCURACY = ":Accuracy";

    @Autowired
    public FormDefinitionImportServiceODK(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        super(httpClientBuilderFactory, tasksService, formDefinitionService);
    }

    protected List<String> parseToUrlList(String responseBody) throws XPathException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        InputSource inputSource = new InputSource(new ByteArrayInputStream(responseBody.getBytes()));
        NodeList nodeList = (NodeList) xPath.compile("/forms/form").evaluate(inputSource, XPathConstants.NODESET);
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            String url = namedNodeMap.getNamedItem("url").getNodeValue();
            urls.add(url);
        }

        return urls;
    }

    @Override
    protected void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions) {

        for (FormDefinition formDefinition: formDefinitions) {
            List<FormElement> formElements = formDefinition.getFormElements();
            modifyFormElements(formElements);

            formElements.add(new FormElementBuilder().setName(ODKConstants.META_MODEL_VERSION)
                    .setLabel(ODKConstants.META_MODEL_VERSION).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(ODKConstants.META_UI_VERSION)
                    .setLabel(ODKConstants.META_UI_VERSION).setType(FieldTypeConstants.STRING).createFormElement());
            formElements.add(new FormElementBuilder().setName(ODKConstants.META_SUBMISSION_DATE)
                    .setLabel(ODKConstants.META_SUBMISSION_DATE).setType(FieldTypeConstants.DATE_TIME).createFormElement());
            formElements.add(new FormElementBuilder().setName(ODKConstants.META_IS_COMPLETE)
                    .setLabel(ODKConstants.META_IS_COMPLETE).setType(FieldTypeConstants.BOOLEAN).createFormElement());
            formElements.add(new FormElementBuilder().setName(ODKConstants.META_DATE_MARKED_AS_COMPLETE)
                    .setLabel(ODKConstants.META_DATE_MARKED_AS_COMPLETE).setType(FieldTypeConstants.DATE_TIME).createFormElement());

        }
    }

    private void modifyFormElements(List<FormElement> formElements) {
        List<FormElement> additionalFields = new ArrayList<>();
        for (FormElement formElement : formElements) {
            String[] array = formElement.getName().split("/");
            formElement.setName(array[array.length - 1]);

            if (formElement.getType().equalsIgnoreCase(FieldTypeConstants.GEOPOINT)) {
                additionalFields.addAll(addGeopointFields(formElement));
            }

            if (formElement.hasChildren()) {
                modifyFormElements(formElement.getChildren());
            }

        }
        formElements.addAll(additionalFields);
    }

    private List<FormElement> addGeopointFields (FormElement formElement) {
        List<FormElement> formElements = new ArrayList<>();
        String name = formElement.getName();
        String type = formElement.getType();

        formElement.setName(name + LATITUDE);
        formElement.setLabel(formElement.getName());
        formElements.add(formElement);
        formElements.add(new FormElementBuilder().setName(name + LONGITUDE).setLabel(name + LONGITUDE).setType(type).createFormElement());
        formElements.add(new FormElementBuilder().setName(name + ALTITUDE).setLabel(name + ALTITUDE).setType(type).createFormElement());
        formElements.add(new FormElementBuilder().setName(name + ACCURACY).setLabel(name + ACCURACY).setType(type).createFormElement());

        return formElements;
    }
}

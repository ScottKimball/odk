package org.motechproject.odk.service.impl;

import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.motechproject.odk.tasks.OnaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FormDefinitionImportServiceOna extends AbstractFormDefinitionImportService implements FormDefinitionImportService {

    @Autowired
    public FormDefinitionImportServiceOna(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        super(httpClientBuilderFactory, tasksService, formDefinitionService);
    }


    @Override
    protected void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions) {

        for (FormDefinition formDefinition: formDefinitions) {
            List<FormElement> formElements = formDefinition.getFormElements();

            for (FormElement formElement : formElements) {
                String formFieldName = formElement.getName();
                String name = formFieldName.substring(formFieldName.indexOf("/", 1) + 1, formFieldName.length()); // removes form title from URI
                formElement.setName(name);

            }

            formElements.add(new FormElement(OnaConstants.NOTES, FieldTypeConstants.STRING_ARRAY));
            formElements.add(new FormElement(OnaConstants.TAGS, FieldTypeConstants.SELECT));
            formElements.add(new FormElement(OnaConstants.XFORM_ID_STRING, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.META_INSTANCE_ID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.UUID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.STATUS, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.FORMHUB_UUID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.ID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.SUBMISSION_TIME, FieldTypeConstants.DATE_TIME));
            formElements.add(new FormElement(OnaConstants.VERSION, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.GEOLOCATION, FieldTypeConstants.DOUBLE_ARRAY));
            formElements.add(new FormElement(OnaConstants.SUBMITTED_BY, FieldTypeConstants.STRING));
        }
    }

    protected List<String> parseToUrlList(String responseBody) throws XPathException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        Map<String,String> namespaceMap = new HashMap<String, String>();
        namespaceMap.put("x", "http://openrosa.org/xforms/xformsList");
        namespaces.setBindings(namespaceMap);
        xPath.setNamespaceContext(namespaces);

        InputSource inputSource = new InputSource(new ByteArrayInputStream(responseBody.getBytes()));
        NodeList nodeList = (NodeList) xPath.compile("/x:xforms/x:xform/x:downloadUrl").evaluate(inputSource, XPathConstants.NODESET);
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i).getFirstChild();
            urls.add(node.getNodeValue());
        }

        return urls;
    }

}

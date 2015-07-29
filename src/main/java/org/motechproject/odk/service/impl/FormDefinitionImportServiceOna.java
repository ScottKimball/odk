package org.motechproject.odk.service.impl;

import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
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
            List<FormField> formFields = formDefinition.getFormFields();

            for (FormField formField : formFields) {
                String name = formField.getName().replace("/" + formDefinition.getTitle().toLowerCase() + "/","");
                formField.setName(name);

            }

            formFields.add(new FormField(OnaConstants.NOTES, FieldTypeConstants.STRING_ARRAY));
            formFields.add(new FormField(OnaConstants.TAGS, FieldTypeConstants.SELECT));
            formFields.add(new FormField(OnaConstants.XFORM_ID_STRING, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.META_INSTANCE_ID, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.UUID, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.STATUS, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.FORMHUB_UUID, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.ID, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.SUBMISSION_TIME, FieldTypeConstants.DATE_TIME));
            formFields.add(new FormField(OnaConstants.VERSION, FieldTypeConstants.STRING));
            formFields.add(new FormField(OnaConstants.GEOLOCATION, FieldTypeConstants.DOUBLE_ARRAY));
            formFields.add(new FormField(OnaConstants.SUBMITTED_BY, FieldTypeConstants.STRING));
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

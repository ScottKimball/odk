package org.motechproject.odk.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.client.HttpClient;
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
import org.motechproject.odk.tasks.ODKConstants;


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

        List<FormField> additionalFields = new ArrayList<>();
        for (FormDefinition formDefinition: formDefinitions) {
            List<FormField> formFields = formDefinition.getFormFields();

            for (FormField formField : formFields) {
                String[] array = formField.getName().split("/");
                formField.setName(array[array.length - 1]);

                switch (formField.getType()) {
                    case FieldTypeConstants.GEOPOINT :
                        additionalFields.addAll(addGeopointFields(formField));
                        break;

                    default:
                        break;
                }
            }

            formFields.addAll(additionalFields);
            formFields.add(new FormField(ODKConstants.META_INSTANCE_ID, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_MODEL_VERSION, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_UI_VERSION, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_SUBMISSION_DATE, FieldTypeConstants.DATE_TIME));
            formFields.add(new FormField(ODKConstants.META_IS_COMPLETE, FieldTypeConstants.BOOLEAN));
            formFields.add(new FormField(ODKConstants.META_DATE_MARKED_AS_COMPLETE, FieldTypeConstants.DATE_TIME));
        }
    }

    private List<FormField> addGeopointFields (FormField formField) {
        List<FormField> formFields = new ArrayList<>();
        String name = formField.getName();
        String type = formField.getType();

        formField.setName(name + LATITUDE);
        formFields.add(formField);
        formFields.add(new FormField(name + LONGITUDE, type ));
        formFields.add(new FormField(name + ALTITUDE, type ));
        formFields.add(new FormField(name + ACCURACY, type ));
        return formFields;
    }
}

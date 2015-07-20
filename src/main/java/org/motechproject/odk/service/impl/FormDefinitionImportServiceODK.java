package org.motechproject.odk.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
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

    private HttpClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(FormDefinitionImportServiceODK.class);


    @Autowired
    public FormDefinitionImportServiceODK(HttpClientBuilderFactory httpClientBuilderFactory) {
        this.client = httpClientBuilderFactory.newBuilder().build();
    }

    protected List<String> getFormUrls(Configuration configuration) throws Exception{
        HttpGet request = new HttpGet(configuration.getUrl());
        request.addHeader("accept", MediaType.APPLICATION_XML_VALUE);
        HttpResponse response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return parseToUrlList(responseBody);
    }

    protected List<String> getXmlFormDefinitions(List<String> formUrls) throws Exception{
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url);
            request.addHeader("accept", MediaType.APPLICATION_XML_VALUE);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            formDefinitions.add(responseBody);
        }

        return formDefinitions;
    }

    private List<String> parseToUrlList(String responseBody) throws XPathException {

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
            for (FormField formField : formDefinition.getFormFields()) {
                String[] array = formField.getName().split("/");
                formField.setName(array[array.length - 1]);
            }

            List<FormField> formFields = formDefinition.getFormFields();
            formFields.add(new FormField(ODKConstants.META_INSTANCE_ID, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_MODEL_VERSION, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_UI_VERSION, FieldTypeConstants.STRING));
            formFields.add(new FormField(ODKConstants.META_SUBMISSION_DATE, FieldTypeConstants.DATE_TIME));
            formFields.add(new FormField(ODKConstants.META_IS_COMPLETE, FieldTypeConstants.BOOLEAN));
            formFields.add(new FormField(ODKConstants.META_DATE_MARKED_AS_COMPLETE, FieldTypeConstants.DATE_TIME));
        }
    }
}

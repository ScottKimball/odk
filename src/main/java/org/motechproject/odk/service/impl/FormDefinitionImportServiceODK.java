package org.motechproject.odk.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;

import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.client.HttpClient;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class FormDefinitionImportServiceODK extends AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private HttpClient client;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FormDefinitionImportServiceODK.class);

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
            for (FormDefinition.FormField formField : formDefinition.getFormFields()) {
                String[] array = formField.getName().split("/");
                formField.setName(array[array.length - 1]);
            }
        }
    }
}

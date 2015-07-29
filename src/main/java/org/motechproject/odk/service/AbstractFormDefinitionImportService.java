package org.motechproject.odk.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormField;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public abstract class AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormDefinitionImportService.class);
    private static final HashMap<String, String> NAMESPACE_MAP = new HashMap<String, String>() {{
        put("xForms", "http://www.w3.org/2002/xforms");
        put("ev", "http://www.w3.org/2001/xml-events");
        put("h", "http://www.w3.org/1999/xhtml");
        put("jr", "http://openrosa.org/javarosa");
        put("orx", "http://openrosa.org/xforms");
        put("xsd", "http://www.w3.org/2001/XMLSchema");
    }};
    protected static final String FORM_LIST_PATH = "/formList";

    private HttpClient client;
    private TasksService tasksService;
    FormDefinitionService formDefinitionService;

    @Autowired
    public AbstractFormDefinitionImportService(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        this.client = httpClientBuilderFactory.newBuilder().build();
        this.tasksService = tasksService;
        this.formDefinitionService = formDefinitionService;
    }

    @Override
    public boolean importForms (Configuration config) {

        try {
            List<String> formUrls = getFormUrls(config);
            List<String> xmlFormDefinitions = getXmlFormDefinitions(formUrls);
            List<FormDefinition> formDefinitions = parseXmlFormDefinitions(xmlFormDefinitions, config.getName());
            modifyFormDefinitionForImplementation(formDefinitions);
            updateFormDefinitions(formDefinitions, config.getName());
            tasksService.updateTasksChannel();
            return true;

        } catch (Exception e) {
            LOGGER.error(e.toString());
            return false;
        }
    }

    protected List<FormDefinition> parseXmlFormDefinitions (List<String> xmlFormDefinitions, String configName) throws XPathExpressionException {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.setBindings(NAMESPACE_MAP);
        xPath.setNamespaceContext(namespaces);

        List<FormDefinition> formDefinitions = new ArrayList<>();

        for (String xmlFormDefinition: xmlFormDefinitions) {
            FormDefinition formDefinition = new FormDefinition(configName);
            InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlFormDefinition.getBytes()));
            String title = xPath.compile("/h:html/h:head/h:title").evaluate(inputSource);
            formDefinition.setTitle(title);
            inputSource = new InputSource(new ByteArrayInputStream(xmlFormDefinition.getBytes()));
            NodeList nodeList = (NodeList) xPath.compile("/h:html/h:head/xForms:model/xForms:bind").evaluate(inputSource, XPathConstants.NODESET);
            formDefinition.setFormFields(createFormFields(nodeList));
            formDefinitions.add(formDefinition);

        }
        return formDefinitions;

    }

    protected List<String> getFormUrls(Configuration configuration) throws Exception {
        HttpGet request = new HttpGet(configuration.getUrl() + configuration.getNamespace() + FORM_LIST_PATH);
        HttpResponse response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return parseToUrlList(responseBody);
    }

    protected List<String> getXmlFormDefinitions(List<String> formUrls) throws Exception {
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            formDefinitions.add(responseBody);
        }
        return formDefinitions;
    }

    private List<FormField> createFormFields(NodeList nodeList) {
        List<FormField> formFields = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            NamedNodeMap namedNodeMap = nodeList.item(i).getAttributes();
            Node type = namedNodeMap.getNamedItem("type");
            Node readOnly = namedNodeMap.getNamedItem("readonly");

            if (readOnly == null && type != null) {
                String typeString = type.getNodeValue();
                String name = namedNodeMap.getNamedItem("nodeset").getNodeValue();
                formFields.add( new FormField(name,typeString));
            }
        }
        return formFields;
    }

    private void updateFormDefinitions(List<FormDefinition> formDefinitions, String configName) {
        formDefinitionService.deleteFormDefinitionsByConfigurationName(configName);
        for (FormDefinition formDefinition : formDefinitions) {
            formDefinitionService.create(formDefinition);
        }
    }



    protected abstract void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions);
    protected abstract List<String> parseToUrlList(String responseBody) throws XPathException;
}

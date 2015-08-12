package org.motechproject.odk.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.parser.XformParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.xpath.XPathException;
import java.util.ArrayList;
import java.util.List;


@Component
public abstract class AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormDefinitionImportService.class);
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

    protected List<FormDefinition> parseXmlFormDefinitions (List<String> xmlFormDefinitions, String configName) throws Exception {
        List<FormDefinition> formDefinitions = new ArrayList<>();
        for (String def : xmlFormDefinitions) {
            formDefinitions.add(XformParser.parse(def, configName));
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


    private void updateFormDefinitions(List<FormDefinition> formDefinitions, String configName) {
        formDefinitionService.deleteFormDefinitionsByConfigurationName(configName);
        for (FormDefinition formDefinition : formDefinitions) {
            formDefinitionService.create(formDefinition);
        }
    }


    protected abstract void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions);
    protected abstract List<String> parseToUrlList(String responseBody) throws XPathException;
}

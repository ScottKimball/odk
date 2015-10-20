package org.motechproject.odk.service;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.ImportStatus;
import org.motechproject.odk.parser.XformParser;
import org.motechproject.odk.parser.factory.XformParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public abstract class AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormDefinitionImportService.class);
    protected static final String FORM_LIST_PATH = "/formList";

    private HttpClient client;
    private TasksService tasksService;
    private FormDefinitionService formDefinitionService;

    @Autowired
    public AbstractFormDefinitionImportService(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        this.client = httpClientBuilderFactory.newBuilder().build();
        this.tasksService = tasksService;
        this.formDefinitionService = formDefinitionService;
    }

    @Override
    public ImportStatus importForms(Configuration config) {

        try {
            List<String> formUrls = getFormUrls(config);
            List<String> xmlFormDefinitions = getXmlFormDefinitions(formUrls, config);
            List<FormDefinition> formDefinitions = parseXmlFormDefinitions(xmlFormDefinitions, config);
            modifyFormDefinitionForImplementation(formDefinitions);
            updateFormDefinitions(formDefinitions, config.getName());
            tasksService.updateTasksChannel();
            return new ImportStatus(true);

        } catch (Exception e) {
            LOGGER.error(e.toString());
            return new ImportStatus(false);
        }
    }

    protected List<FormDefinition> parseXmlFormDefinitions(List<String> xmlFormDefinitions, Configuration configuration) throws Exception {
        List<FormDefinition> formDefinitions = new ArrayList<>();
        XformParser parser = new XformParserFactory().getParser(configuration.getType());
        for (String def : xmlFormDefinitions) {
            formDefinitions.add(parser.parse(def, configuration.getName()));
        }
        return formDefinitions;
    }

    protected List<String> getFormUrls(Configuration configuration) throws Exception {
        HttpGet request = new HttpGet(configuration.getUrl() + FORM_LIST_PATH);
        HttpResponse response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return parseToUrlList(responseBody);
    }

    protected List<String> getXmlFormDefinitions(List<String> formUrls, Configuration configuration) throws Exception {
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            formDefinitions.add(responseBody);
        }
        return formDefinitions;
    }

    protected Header generateBasicAuthHeader(HttpUriRequest request, Configuration configuration) {
        Header basicAuthHeader;
        BasicScheme basicScheme = new BasicScheme();
        try {
            basicAuthHeader = basicScheme.authenticate(
                    new UsernamePasswordCredentials(configuration.getUsername(), configuration.getPassword()),
                    request,
                    HttpClientContext.create());
        } catch (Exception e) {
            return null;
        }
        return basicAuthHeader;
    }


    private void updateFormDefinitions(List<FormDefinition> formDefinitions, String configName) {
        formDefinitionService.deleteFormDefinitionsByConfigurationName(configName);
        for (FormDefinition formDefinition : formDefinitions) {
            formDefinitionService.create(formDefinition);
        }
    }


    protected abstract void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions);

    protected abstract List<String> parseToUrlList(String responseBody) throws Exception;

    protected HttpClient getClient() {
        return client;
    }

}

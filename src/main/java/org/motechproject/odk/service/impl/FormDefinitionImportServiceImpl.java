package org.motechproject.odk.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.spi.LoggerFactory;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.client.HttpClient;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service
public class FormDefinitionImportServiceImpl implements FormDefinitionImportService {

    private HttpClient client;
    private HttpClientBuilderFactory httpClientBuilderFactory;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FormDefinitionImportService.class);


    @Autowired
    public FormDefinitionImportServiceImpl(HttpClientBuilderFactory httpClientBuilderFactory) {
        this.httpClientBuilderFactory = httpClientBuilderFactory;
        this.client = httpClientBuilderFactory.newBuilder().build();
    }

    @Override
    public boolean importForms (Configuration config) {

        List<String> formUrls = getFormUrls(config);
        return false;

    }

    private List<String> getFormUrls(Configuration configuration) {
        List<String> formUrls = new ArrayList<>();

        HttpGet request = new HttpGet(configuration.getUrl());
        request.addHeader("accept", MediaType.APPLICATION_XML_VALUE);

        HttpResponse response;

        try {
            response = client.execute(request);
            HttpEntity content = response.getEntity();

            if(content != null) {
                String responseBody = EntityUtils.toString(content);
                LOGGER.info(responseBody);

            }

        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        return null;





    }

}

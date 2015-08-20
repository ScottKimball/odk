package org.motechproject.odk.service.impl;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.domain.KoboFormInfo;
import org.motechproject.odk.service.AbstractFormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionImportService;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.TasksService;
import org.motechproject.odk.tasks.FieldTypeConstants;
import org.motechproject.odk.tasks.KoboConstants;
import org.motechproject.odk.tasks.ODKConstants;
import org.motechproject.odk.tasks.OnaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.xpath.XPathException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FormDefinitionImportServiceKobo extends AbstractFormDefinitionImportService implements FormDefinitionImportService {

    private static final String API_PATH = "/api/v1";
    private static final String FORMS_PATH = "/forms";
    private static final String OWNER_QUERY = "?owner=";
    private static final String XML_MEDIA_PATH = "/form.xml";
    private ObjectMapper objectMapper;
    private JavaType type;


    @Autowired
    public FormDefinitionImportServiceKobo(HttpClientBuilderFactory httpClientBuilderFactory, TasksService tasksService, FormDefinitionService formDefinitionService) {
        super(httpClientBuilderFactory, tasksService, formDefinitionService);
        objectMapper = new ObjectMapper();
        type = objectMapper.getTypeFactory(). constructCollectionType(List.class, KoboFormInfo.class);
    }

    @Override
    protected List<String> getFormUrls(Configuration configuration) throws Exception {

        HttpGet request = new HttpGet(buildFormsQuery(configuration));
        request.addHeader(generateBasicAuthHeader(request, configuration));
        HttpResponse response = getClient().execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return parseToUrlList(responseBody);
    }

    @Override
    protected List<String> getXmlFormDefinitions(List<String> formUrls, Configuration configuration) throws Exception {
        List<String> formDefinitions = new ArrayList<>();

        for (String url : formUrls) {
            HttpGet request = new HttpGet(url + XML_MEDIA_PATH);
            request.addHeader(generateBasicAuthHeader(request, configuration));
            HttpResponse response = getClient().execute(request);
            String formDefinition = EntityUtils.toString(response.getEntity());
            formDefinitions.add(normalizeFormDef(formDefinition));
        }
        return formDefinitions;
    }

    @Override
    protected void modifyFormDefinitionForImplementation(List<FormDefinition> formDefinitions) {
        for (FormDefinition formDefinition : formDefinitions) {
            List<FormElement> formElements = formDefinition.getFormElements();

            for (FormElement formElement : formElements) {
                String formFieldName = formElement.getName();
                String name = formFieldName.substring(formFieldName.indexOf("/", 1) + 1, formFieldName.length()); // removes form title from URI
                formElement.setName(name);

            }
            formElements.add(new FormElement(OnaConstants.NOTES,OnaConstants.NOTES, FieldTypeConstants.STRING_ARRAY));
            formElements.add(new FormElement(OnaConstants.UUID,OnaConstants.UUID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(KoboConstants.BAMBOO_DATASET_ID,KoboConstants.BAMBOO_DATASET_ID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.TAGS,OnaConstants.TAGS, FieldTypeConstants.STRING_ARRAY));
            formElements.add(new FormElement(OnaConstants.SUBMITTED_BY,OnaConstants.SUBMITTED_BY, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.XFORM_ID_STRING,OnaConstants.XFORM_ID_STRING, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.META_INSTANCE_ID,OnaConstants.META_INSTANCE_ID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.FORMHUB_UUID,OnaConstants.FORMHUB_UUID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(KoboConstants.END,KoboConstants.END, FieldTypeConstants.DATE_TIME));
            formElements.add(new FormElement(OnaConstants.SUBMISSION_TIME,OnaConstants.SUBMISSION_TIME, FieldTypeConstants.DATE_TIME));
            formElements.add(new FormElement(KoboConstants.START,KoboConstants.START, FieldTypeConstants.DATE_TIME));
            formElements.add(new FormElement(OnaConstants.GEOLOCATION,OnaConstants.GEOLOCATION, FieldTypeConstants.DOUBLE_ARRAY));
            formElements.add(new FormElement(KoboConstants.USERFORM_ID,KoboConstants.USERFORM_ID, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.STATUS,OnaConstants.STATUS, FieldTypeConstants.STRING));
            formElements.add(new FormElement(OnaConstants.ID,OnaConstants.ID, FieldTypeConstants.INT));
        }
    }

    @Override
    protected List<String> parseToUrlList(String responseBody) throws Exception {
        List<KoboFormInfo> koboFormInfoList = objectMapper.readValue(responseBody,type);

        List<String> urls = new ArrayList<>();
        for (KoboFormInfo koboFormInfo : koboFormInfoList) {
            urls.add(koboFormInfo.getUrl());
        }

        return urls;
    }

    private String buildFormsQuery(Configuration configuration) {
        return configuration.getUrl() + API_PATH + FORMS_PATH + OWNER_QUERY + configuration.getUsername();
    }

    private String normalizeFormDef(String formDef) {
        if (formDef.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;")) {
            return formDef.replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("<root><?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "")
                    .replace("\n</root>" , "")
                    .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n", "");
        } else {
            return formDef;
        }


    }





}

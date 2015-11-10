package org.motechproject.odk.it;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.impl.ServerEventRelay;
import org.motechproject.odk.web.FormController;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FormControllerIT extends OdkBaseIT {


    @Inject
    FormController formController;

    @Before
    public void setUp() throws IOException, InterruptedException {
        createAdminUser();

        getHttpClient().getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthPolicy.BASIC),
                new UsernamePasswordCredentials("motech", "motech")
        );

        login();

    }

    @Test
    public void testNestedRepeats() throws Exception{

        HttpPost request = new HttpPost(String.format("http://localhost:%d/odk/forms/%s/%s", TestContext.getJettyPort(), CONFIG_NAME, TITLE));
        StringEntity entity = new StringEntity(getJson());
        request.setEntity(entity);
        HttpResponse response = getHttpClient().execute(request);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);


    }
}

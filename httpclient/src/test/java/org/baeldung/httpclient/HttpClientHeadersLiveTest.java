package org.baeldung.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpProtocolParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class HttpClientHeadersLiveTest {

    private static final String SAMPLE_URL = "http://www.github.com";

    private CloseableHttpClient client;

    private CloseableHttpResponse response;

    @Before
    public final void before() {
        client = HttpClientBuilder.create().build();
    }

    @After
    public final void after() throws IllegalStateException, IOException {
        if (response == null) {
            return;
        }

        try {
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                final InputStream instream = entity.getContent();
                instream.close();
            }
        } finally {
            response.close();
        }
    }

    // tests - headers - deprecated

    @Test
    public final void givenDeprecatedApi_whenClientUsesCustomUserAgent_thenCorrect() throws ClientProtocolException, IOException {
        client = HttpClients.custom().build();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 Firefox/26.0");
        HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 Firefox/26.0");

        final HttpGet request = new HttpGet(SAMPLE_URL);
        response = client.execute(request);
    }

    // tests - headers - user agent

    @Test
    public final void givenConfigOnRequest_whenRequestHasCustomUserAgent_thenCorrect() throws ClientProtocolException, IOException {
        client = HttpClients.custom().build();
        final HttpGet request = new HttpGet(SAMPLE_URL);
        request.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 Firefox/26.0");
        response = client.execute(request);
    }

    @Test
    public final void givenConfigOnClient_whenRequestHasCustomUserAgent_thenCorrect() throws ClientProtocolException, IOException {
        client = HttpClients.custom().setUserAgent("Mozilla/5.0 Firefox/26.0").build();
        response = client.execute(new HttpGet(SAMPLE_URL));
    }

    // tests - headers - content type

    @Test
    public final void givenUsingDeprecatedApi_whenRequestHasCustomContentType_thenCorrect() throws ClientProtocolException, IOException {
        client = HttpClients.custom().build();
        final HttpGet request = new HttpGet(SAMPLE_URL);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response = client.execute(request);
    }

    @Test
    public final void givenRequestBuildWithBuilderWithDeprecatedApi_whenRequestHasCustomContentType_thenCorrect() throws ClientProtocolException, IOException {
        final DefaultHttpClient client2 = new DefaultHttpClient();
        final HttpGet request = new HttpGet(SAMPLE_URL);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response = client2.execute(request);
    }

    @Test
    public final void givenRequestBuildWithBuilder_whenRequestHasCustomContentType_thenCorrect() throws ClientProtocolException, IOException {
        client = HttpClients.custom().build();
        final HttpUriRequest request = RequestBuilder.get().setUri(SAMPLE_URL).setHeader(HttpHeaders.CONTENT_TYPE, "application/json").build();
        response = client.execute(request);
    }

    @Test
    public final void givenConfigOnClient_whenRequestHasCustomContentType_thenCorrect() throws ClientProtocolException, IOException {
        final Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        final List<Header> headers = Lists.newArrayList(header);
        client = HttpClients.custom().setDefaultHeaders(headers).build();
        final HttpUriRequest request = RequestBuilder.get().setUri(SAMPLE_URL).build();
        response = client.execute(request);
    }

}

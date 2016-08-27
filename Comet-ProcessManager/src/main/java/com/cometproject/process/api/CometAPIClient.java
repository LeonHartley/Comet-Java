package com.cometproject.process.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CometAPIClient {
    private static CometAPIClient instance;

    private final Logger log = org.apache.logging.log4j.LogManager.getLogger(CometAPIClient.class);

    public CometAPIClient() {

    }

    public String submitRequest(HttpMethod method, String accessToken, String url, Map<String, String> elements)
            throws Exception {
        HttpClient httpclient = HttpClients.createDefault();

        HttpRequestBase httpRequest = null;

        if (method == HttpMethod.POST) {
            httpRequest = new HttpPost(url);
        } else {
            httpRequest = new HttpGet(url);
        }

        List<NameValuePair> pairList = new ArrayList<>();

        if (elements != null) {
            for (Map.Entry<String, String> element : elements.entrySet()) {
                pairList.add(new BasicNameValuePair(element.getKey(), element.getValue()));
            }
        }

        httpRequest.addHeader("authToken", accessToken);

        if(httpRequest instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
        }

        HttpResponse response = httpclient.execute(httpRequest);
        HttpEntity entity = response.getEntity();

        if (entity != null) {

            try (InputStream inputStream = entity.getContent()) {
                final String string = IOUtils.toString(inputStream);
                return string;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static CometAPIClient getInstance() {
        if (instance == null) {
            instance = new CometAPIClient();
        }

        return instance;
    }
}

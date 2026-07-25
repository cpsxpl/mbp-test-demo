package com.mbp.eng.framework.common.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

public class HttpManager {
    private static final PoolingHttpClientConnectionManager cm;
    private static final RequestConfig conf;
    static {
        try {
            cm = new PoolingHttpClientConnectionManager();
            // Increase max total connection to 200
            cm.setMaxTotal(200);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(20);
            conf = RequestConfig
                    .custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(60000)
                    .build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static HttpClient getClient(String userName, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));
        return HttpClients
                .custom()
                .setConnectionManager(cm)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(conf)
                .build();
    }

    public static HttpClient getClient() {
        return HttpClients
                .custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(conf)
                .build();
    }

    public static String post(HttpPost post) throws Exception {
        HttpResponse httpResponse = doPost(post);
        return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
    }

    public static String get(HttpGet get) throws Exception {
        HttpResponse httpResponse = doGet(get);
        return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
    }

    public static String delete(HttpDelete delete) throws Exception {
        HttpResponse httpResponse = doDelete(delete);
        return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
    }

    public static String put(HttpPut put) throws Exception {
        HttpResponse httpResponse = doPut(put);
        return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
    }

    public static HttpResponse doPost(HttpPost post) throws Exception {
        HttpClient httpClient = HttpManager.getClient();
        HttpResponse httpResponse = httpClient.execute(post);
        return httpResponse;
    }

    public static HttpResponse doGet(HttpGet get) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse httpResponse = client.execute(get);
        return httpResponse;
    }

    public static HttpResponse doDelete(HttpDelete delete) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse httpResponse = client.execute(delete);
        return httpResponse;
    }

    public static HttpResponse doPut(HttpPut put) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse httpResponse = client.execute(put);
        return httpResponse;
    }
}
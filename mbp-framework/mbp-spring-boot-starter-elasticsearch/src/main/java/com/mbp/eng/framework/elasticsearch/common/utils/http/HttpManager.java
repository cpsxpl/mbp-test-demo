package com.mbp.eng.framework.elasticsearch.common.utils.http;

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
    private static final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    private static final RequestConfig requestConfig;

    static {
        try {
            poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
            // Increase max total connection to 200
            poolingHttpClientConnectionManager.setMaxTotal(200);
            // Increase default max connection per route to 20
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);
            requestConfig = RequestConfig
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
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static HttpClient getClient() {
        return HttpClients
                .custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static String post(HttpPost httpPost) throws Exception {
        HttpResponse response = doPost(httpPost);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    public static String get(HttpGet httpGet) throws Exception {
        HttpResponse response = doGet(httpGet);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    public static String delete(HttpDelete httpDelete) throws Exception {
        HttpResponse response = doDelete(httpDelete);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    public static String put(HttpPut httpPut) throws Exception {
        HttpResponse response = doPut(httpPut);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    public static HttpResponse doPost(HttpPost httpPost) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse response = client.execute(httpPost);
        return response;
    }

    public static HttpResponse doGet(HttpGet httpGet) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse response = client.execute(httpGet);
        return response;
    }

    public static HttpResponse doDelete(HttpDelete httpDelete) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse response = client.execute(httpDelete);
        return response;
    }

    public static HttpResponse doPut(HttpPut httpPut) throws Exception {
        HttpClient client = HttpManager.getClient();
        HttpResponse response = client.execute(httpPut);
        return response;
    }
}
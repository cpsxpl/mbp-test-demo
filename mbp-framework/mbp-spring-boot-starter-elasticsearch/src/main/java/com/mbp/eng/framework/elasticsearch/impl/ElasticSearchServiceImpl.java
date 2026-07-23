package com.mbp.eng.framework.elasticsearch.impl;

import com.mbp.eng.framework.common.exception.AppException;
import com.mbp.eng.framework.elasticsearch.common.utils.http.HttpManager;
import com.mbp.eng.framework.elasticsearch.ElasticSearchService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service("elasticSearchService")
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    @Value("${spring.elasticsearch.jest.uris}")
    String url;
    @Value("${spring.elasticsearch.jest.username}")
    String username;
    @Value("${spring.elasticsearch.jest.password}")
    String password;

    private HttpClient httpClient;

    @Override
    public String executeQuery(String sql) {
        try {
            URIBuilder uriBuilder = new URIBuilder(getSqlPluginUri());
            uriBuilder.addParameter("sql", sql);
            HttpGet get = new HttpGet(uriBuilder.build());
            // TODO 上线之前需将代理清除
            //setSocksProxy();
            HttpResponse response = getHttpClient().execute(get);
            // TODO 上线之前需将代理清除
            //clearSocksProxy();
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("execute " + sql + " with es error:" + e.getMessage(), e);
            throw new AppException("es service error", e);
        }
    }

    /**
     * 设置代理,用于本地测试
     */
    private void setSocksProxy() {
        System.setProperty("socksProxyVersion", "5");
        System.setProperty("socksProxyHost", "172.22.178.100");
        System.setProperty("socksProxyPort", "80");
    }
    /**
     * 清除代理,用于本地测试
     */
    private void clearSocksProxy() {
        System.clearProperty("socksProxyVersion");
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");
    }
    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpManager.getClient(username, password);
        }
        return httpClient;
    }
    private String getSqlPluginUri() {
        String sqlPath = url;
        if (url.endsWith("/")) {
            sqlPath += "_sql";
        } else {
            sqlPath += "/_sql";
        }
        return sqlPath;
    }

    // TODO 上线删除
    void setProxy() {
        System.setProperty("socksProxyVersion", "5");
        System.setProperty("socksProxyHost", "172.22.178.100");
        System.setProperty("socksProxyPort", "80");
    }

    public void destroy() {
        System.clearProperty("socksProxyVersion");
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");
    }
}

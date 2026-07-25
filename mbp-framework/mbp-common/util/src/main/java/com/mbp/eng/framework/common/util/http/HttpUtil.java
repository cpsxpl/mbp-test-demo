package com.mbp.eng.framework.common.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtil {
    public static String httpGet(String urlStr, List<String> urlParam) throws IOException, InterruptedException {
        // 实例一个URL资源
        URL url = new URL(urlStr);
        HttpURLConnection httpURLConnection = null;
        int i = 0;
        while (httpURLConnection == null || httpURLConnection.getResponseCode() != 200) {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // 连接超时 单位毫秒
            httpURLConnection.setConnectTimeout(15000);
            // 读取超时 单位毫秒
            httpURLConnection.setReadTimeout(15000);
            i++;
            if (i == 50) {
                break;
            }
            Thread.sleep(500);
        }
        //将返回的值存入到String中
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        return stringBuilder.toString();
    }
}

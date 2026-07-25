package com.mbp.eng.framework.common.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
    protected static final Logger logger = LoggerFactory.getLogger(IOUtils.class);
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static String stream2String(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append(LINE_SEPARATOR);
                }
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.info("stream2String failed ", e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return "";
    }
}

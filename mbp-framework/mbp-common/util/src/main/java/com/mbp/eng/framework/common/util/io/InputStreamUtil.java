package com.mbp.eng.framework.common.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class InputStreamUtil {
    private static final Logger logger = LoggerFactory.getLogger(InputStreamUtil.class);
    /**
     * 将字符串转换成一个InputStream流
     *
     * @param data
     * @return
     */
    public static InputStream toConvertInputStream(String data) {
        InputStream inputStream = null;
        try {
            byte[] content = data.getBytes("UTF-8");
            inputStream = new ByteArrayInputStream(content);
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            logger.error(e.getMessage());
        }
        return inputStream;
    }

    /**
     * 合并 InputStream 流
     *
     * @param inputStreamList
     * @return
     * @throws IOException
     */
    public static SequenceInputStream createSequenceInputStream(List<InputStream> inputStreamList) throws IOException {
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreamList);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        //System.out.println("==========" + toConvertString(sequenceInputStream));
        return sequenceInputStream;
    }
}

package com.mbp.eng.framework.common.utils;

import com.mbp.eng.framework.common.util.date.DateUtil;
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

public class SingletonPatternDemo {
    private static final Logger logger = LoggerFactory.getLogger(SingletonPatternDemo.class);

    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    private static volatile SingletonPatternDemo instance;

    private SingletonPatternDemo() {
        long time = System.currentTimeMillis();
        logger.info("==========单例(双重校验锁)_time:{}", DateUtil.getFormatTime(time));
    }

    public static SingletonPatternDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonPatternDemo.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new SingletonPatternDemo();
            }
        }
        return instance;
    }
    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/
    /**
     * 将字符串转换成一个InputStream流
     *
     * @param data
     * @return
     */
    public InputStream toConvertInputStream(String data) {
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
    public SequenceInputStream createSequenceInputStream(List<InputStream> inputStreamList) throws IOException {
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreamList);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        //System.out.println("==========" + toConvertString(sequenceInputStream));
        return sequenceInputStream;
    }
}

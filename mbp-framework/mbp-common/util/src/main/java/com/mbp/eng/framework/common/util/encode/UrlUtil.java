package com.mbp.eng.framework.common.util.encode;

import java.io.UnsupportedEncodingException;

/**
 * url转码、解码
 */
public class UrlUtil {
    private static final String ENCODE = "UTF-8";
    //private final static String ENCODE = "GBK";

    /**
     * URL 解码
     *
     * @return String
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) throws UnsupportedEncodingException {
        if (null == str) {
            return "";
        }
        String result = java.net.URLDecoder.decode(str, ENCODE);
        return result;
    }

    /**
     * URL 转码
     *
     * @return String
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) throws UnsupportedEncodingException {
        if (null == str) {
            return "";
        }
        String result = java.net.URLEncoder.encode(str, ENCODE);
        return result;
    }

    /**
     * @return void
     * @date 2015-3-17 下午04:09:16
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "测试1";
        System.out.println(getURLEncoderString(str));
        System.out.println(getURLDecoderString(str));
    }
}

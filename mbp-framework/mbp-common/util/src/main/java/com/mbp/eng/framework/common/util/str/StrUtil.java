package com.mbp.eng.framework.common.util.str;

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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    private static final Logger logger = LoggerFactory.getLogger(StrUtil.class);

    private static String pattern = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    /**
     * 过滤掉中文
     *
     * @param str 待过滤中文的字符串
     * @return 过滤掉中文后字符串
     */
    public static String filterChinese(String str) {
        // 用于返回结果
        String result = str;
        boolean flag = isContainChinese(str);
        if (flag) { // 包含中文
            // 用于拼接过滤中文后的字符
            StringBuffer stringBuffer = new StringBuffer();
            // 用于校验是否为中文
            boolean flag2 = false;
            // 用于临时存储单字符
            char chinese = 0;
            // 5.去除掉文件名中的中文
            // 将字符串转换成char[]
            char[] charArray = str.toCharArray();
            // 过滤到中文及中文字符
            for (int i = 0; i < charArray.length; i++) {
                chinese = charArray[i];
                flag2 = isChinese(chinese);
                if (!flag2) { // 不是中日韩文字及标点符号
                    stringBuffer.append(chinese);
                }
            }
            result = stringBuffer.toString();
        }
        return result;
    }

    /**
     * 判断字符是否为汉字
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 判断 url 是否合法
     */
    public static boolean isUrl(String url) {
        Pattern httpPattern = Pattern.compile(pattern);
        if (httpPattern.matcher(url).matches()) {
            return true;
        }
        return false;
    }

    /**
     * 将字符串中的变量替换为map中的值
     *
     * @param str
     * @param map
     * @return
     */
    public static String resolvedResult(String str, Map<String, Object> map) {
        Matcher matcher = Pattern.compile("\\$\\{\\w+\\}").matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String string = matcher.group();
            Object value = map.get(string.substring(2, string.length() - 1));
            if (value != null) {
                matcher.appendReplacement(stringBuffer, value.toString());
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
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
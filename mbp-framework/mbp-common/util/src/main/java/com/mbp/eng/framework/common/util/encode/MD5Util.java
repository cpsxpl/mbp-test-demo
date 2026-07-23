package com.mbp.eng.framework.common.util.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //初始化一个字符数组，用来存放每个16进制字符
    private static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public MD5Util() {
    }

    /**
     * 实现MD5加密,获取加密后的字符串
     *
     * @param pw
     * @return
     */
    public static String stringMD5(String pw) throws NoSuchAlgorithmException {
        // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        // 输入的字符串转换成字节数组
        byte[] inputByteArray = pw.getBytes();
        // inputByteArray是输入字符串转换得到的字节数组
        messageDigest.update(inputByteArray);
        // 转换并返回结果，也是字节数组，包含16个元素
        byte[] resultByteArray = messageDigest.digest();
        // 字符数组转换成字符串返回
        return byteArrayToHex(resultByteArray);
    }

    public static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        //char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    /***
     * MD5加密 生成32位md5码
     *
     * @param inStr
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = messageDigest.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static final String encode(String value) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(value.getBytes());
        byte[] md = messageDigest.digest();
        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;

        for (int i = 0; i < j; ++i) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 15];
            str[k++] = hexDigits[byte0 & 15];
        }

        return new String(str);
    }

    public static void main(String[] args) throws Exception {
        String str = new String("天下 2009-08-06");
        System.out.println("原始：" + str);
        System.out.println("MD5后：" + md5Encode(str));

        String password = "天下 2009-08-06";
        System.out.println("=====" + stringMD5(password));
    }
}

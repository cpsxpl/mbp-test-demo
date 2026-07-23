package com.mbp.eng.framework.common.util.encode;

import java.security.MessageDigest;

public class SHAUtil {
    /***
     * SHA加密 生成40位SHA码
     * @param inStr
     * @return 返回40位SHA码
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");

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

    public static void main(String[] args) throws Exception {
        String str = new String("天下 2009-08-06");
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + shaEncode(str));
    }
}

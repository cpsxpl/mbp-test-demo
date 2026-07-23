package com.mbp.eng.framework.common.util.encode;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;

/**
 * This program generates a AES key, retrieves its raw bytes, and then
 * reinstantiates a AES key from the key bytes. The reinstantiated key is used
 * to initialize a AES cipher for encryption and decryption.
 */
public class AESHelper {
    private static final String AES = "AES";

    private static String cryptKey = "mbp%app%";

    public void setCRYPT_KEY(String key) {
        this.cryptKey = key;
    }

    /**
     * 加密
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey); //设置密钥和加密形式
        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES); //设置加密Key
        cipher.init(Cipher.DECRYPT_MODE, securekey); //设置密钥和解密形式
        return cipher.doFinal(src);
    }

    /**
     * 二行制转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static final String decrypt(String data) throws Exception {
        return new String(decrypt(hex2byte(data.getBytes()), cryptKey));
    }

    /**
     * 加密
     *
     * @param data
     * @return
     */
    public static final String encrypt(String data) throws Exception {
        return byte2hex(encrypt(data.getBytes(), cryptKey));
    }
    public static void main(String[] args) throws Exception {
        Date d = new Date();
        System.out.println(d.getTime());
        System.out.println(encrypt(d.getTime() + ""));
    }
}

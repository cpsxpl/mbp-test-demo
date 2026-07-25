package com.mbp.eng.framework.common.util.encode;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.Key;

public class EncryptTool {
    public EncryptTool() {
    }

    public static String aes3(String key, String param) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(1, secretKey, iv);
        byte[] bytes = cipher.doFinal(param.getBytes("GB18030"));
        return base64(bytes);
    }

    public static String aes3_de(String result, String key, String ivStr) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        cipher.init(2, secretKey, iv);
        byte[] encrypted1 = base64De(result);
        byte[] bytes = cipher.doFinal(encrypted1);
        String originalString = new String(bytes, "GB18030");
        return originalString;
    }

    public static byte[] base64De(String s) {
        Base64 base64 = new Base64();
        return base64.decode(s);
    }

    public static String base64(byte[] s) throws EncoderException {
        Base64 base64 = new Base64();
        return base64.encodeToString(s);
    }
}
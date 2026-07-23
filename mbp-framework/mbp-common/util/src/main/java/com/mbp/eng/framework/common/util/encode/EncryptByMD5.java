package com.mbp.eng.framework.common.util.encode;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptByMD5 {
    public EncryptByMD5() {
    }

    public static String md5Hex(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static boolean validatePassword(String password, String md5Value) {
        return md5Value.equals(md5Hex(password));
    }
}

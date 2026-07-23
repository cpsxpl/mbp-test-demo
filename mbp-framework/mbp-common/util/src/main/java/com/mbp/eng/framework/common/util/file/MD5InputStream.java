package com.mbp.eng.framework.common.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5InputStream extends FilterInputStream {
    protected static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected MessageDigest messagedigest = null;

    @Override
    public int read(byte[] b) throws IOException {
        int length = super.read(b, 0, b.length);
        if (length > 0) {
            messagedigest.update(b, 0, length);
        }

        return length;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int length = super.read(b, off, len);
        if (length > 0) {
            messagedigest.update(b, off, length);
        }
        return length;
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public MD5InputStream(InputStream in) {
        super(in);
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            throw new RuntimeException(MD5InputStream.class.getName()
                    + "init failed,MessageDigest not support MD5InputStream");
        }
    }

    public String getMD5() {
        return bufferToHex(messagedigest.digest());
    }

    public static void main(String[] args) {
        InputStream fis = null;
        OutputStream out = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(new File(
                    "e:\\error.txt")));
            out = new FileOutputStream("e:\\test1103.txt");

            byte[] buffer = new byte[1024];
            int numRead = 0;

            // fis.reset();
            while ((numRead = fis.read(buffer)) > 0) {
                out.write(buffer, 0, numRead);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println("md5:"+md5Hex(md.digest()));
    }
}
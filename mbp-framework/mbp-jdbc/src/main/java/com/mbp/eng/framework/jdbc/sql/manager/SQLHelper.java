package com.mbp.eng.framework.jdbc.sql.manager;

import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLHelper {
    private static final String hexChars = "0123456789abcdef";

    public static void main(String[] args) {
        SQLHelper sqlHelper = new SQLHelper();
    }

    private static boolean arrayEquals(Object[] a0, Object[] a1) {
        if (a0.length != a1.length) {
            return false;
        }
        for (int i = 0; i < a0.length; i++) {
            if (!a0[i].equals(a1[i])) {
                return false;
            }
        }
        return true;
    }

    public static String booleanToYN(boolean value) {
        return value ? "Y" : "N";
    }

    public static String byteArrayToHex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append("0123456789abcdef".charAt(array[i] >> 4 & 0xF));
            sb.append("0123456789abcdef".charAt(array[i] & 0xF));
        }
        return sb.toString();
    }

    private static boolean checkPack(String[] sa, String s) {
        printStringArray(sa);
        System.out.println(s);
        System.out.println(packStringArray(sa));
        printStringArray(unpackStringArray(s));
        if (!packStringArray(sa).equals(s)) {
            return false;
        }

        return arrayEquals(unpackStringArray(s), sa);
    }

    public static int getMaxVarchar() {
        return 255;
    }

    public static String packStringArray(String[] array) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                stringBuffer.append(";");
            }
            String s = array[i];
            for (int j = 0; j < s.length(); j++) {
                if ((s.charAt(j) == ';') || (s.charAt(j) == '\\')) {
                    stringBuffer.append('\\');
                }
                stringBuffer.append(s.charAt(j));
            }
        }
        return stringBuffer.toString();
    }

    private static void printStringArray(String[] sa) {
        System.out.print("[ ");
        for (int i = 0; i < sa.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print("\"" + sa[i] + "\" ");
        }
        System.out.println("]");
    }

    public static String[] unpackStringArray(String string) {
        Vector vector = new Vector();

        StringBuffer sb = new StringBuffer();
        int n = string.length();

        for (int i = 0; i < n; i++) {
            if ((string.charAt(i) == '\\') && (i < n - 1)) {
                i++;
                sb.append(string.charAt(i));
            } else if (string.charAt(i) == ';') {
                vector.addElement(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(string.charAt(i));
            }
        }
        vector.addElement(sb.toString());

        String[] out = new String[vector.size()];
        vector.copyInto(out);
        return out;
    }

    public static boolean ynToBoolean(String value) {
        return (value != null) && (value.equals("Y"));
    }
}
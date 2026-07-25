package com.mbp.eng.framework.jdbc.sql.table;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class StringHelper {
    public static String getBigDecimal(String value, int scale) {
        StringBuffer stringBuffer = new StringBuffer("#,##0");
        if (scale > 0) {
            stringBuffer.append(".");
            for (int i = 0; i < scale; ++i) {
                stringBuffer.append("0");
            }
        }
        try {
            value = NumberHelper.getNumStr(value, scale);
            double d = Double.parseDouble(value);
            return new DecimalFormat(stringBuffer.toString()).format(d);
        } catch (Exception ex) {
        }
        return "0";
    }

    public static String formateNumber(String value, boolean isShowZero) {
        String formatedNum = getBigDecimal(value, 2);
        if ((((formatedNum.equals("0.00")) || (formatedNum.equals("0"))))
                && (isShowZero))
            formatedNum = " ";
        if (formatedNum.startsWith("-"))
            formatedNum = "<font color='red'>" + formatedNum.substring(1)
                    + "</font>";
        return formatedNum;
    }

    public static String formateSimplyNumber(String value, boolean isShowZero) {
        String formatedNum = getBigDecimal(value, 2);
        if ((((formatedNum.equals("0.00")) || (formatedNum.equals("0"))))
                && (isShowZero))
            formatedNum = " ";
        return formatedNum;
    }

    public static String strTo88591(String str)
            throws UnsupportedEncodingException {
        if ((str == null) || (str.equalsIgnoreCase(""))) {
            return "";
        }
        return new String(str.getBytes("8859_1"), "gb2312");
    }

    public static String strToScaleBigDecimal(String strDouble, int scale) {
        try {
            if ((strDouble != null) && (!(strDouble.equals("")))
                    && (scale >= 0)) {
                return new BigDecimal(new Double(strDouble).doubleValue())
                        .setScale(scale, 4).toString();
            }
        } catch (Exception localException) {
        }
        return "";
    }

    public static final int toNum(String param) throws NumberFormatException {
        return Integer.parseInt(param);
    }

    public static boolean booleanValue(String value) {
        if (value == null) {
            return false;
        }

        return ((value.trim().toLowerCase().equals("y"))
                || (value.trim().toLowerCase().equals("true"))
                || (value.trim().toLowerCase().equals("1")) || (value.trim()
                .toLowerCase().equals("yes")));
    }

    public static String vectorToString(Vector value, String delim) {
        String r = new String();
        for (int i = 0; i < value.size(); ++i) {
            if (i == value.size() - 1)
                r = r + ((String) value.elementAt(i));
            else {
                r = r + ((String) value.elementAt(i)) + delim;
            }
        }
        return r;
    }

    public static String[] parseTokensToArray(String str, String delim) {
        StringTokenizer tk = new StringTokenizer(str, delim);
        int i = tk.countTokens();
        String[] r = new String[i];

        for (int j = 0; j < i; ++j) {
            r[j] = tk.nextElement().toString().trim();
        }

        return r;
    }

    public static String stringReplace(String inString, String beforeString, String afterString) {
        if (inString == null) {
            return inString;
        }

        int istart = 0;
        int iend = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            iend = inString.indexOf(beforeString, istart);

            if (iend == -1) {
                break;
            }

            stringBuffer.append(inString.substring(istart, iend));
            stringBuffer.append(afterString);

            istart = iend + beforeString.length();
        }

        stringBuffer.append(inString.substring(istart));

        return stringBuffer.toString();
    }

    public static Vector parseTokens(String str, String delim) throws Exception {
        StringTokenizer stringTokenizer = new StringTokenizer(str, delim);
        Vector vector = new Vector();

        while (stringTokenizer.hasMoreElements()) {
            vector.addElement(stringTokenizer.nextToken());
        }

        return vector;
    }

    public static void main(String[] args) {
        String aa = "0.000";
        System.out.print(formateNumber(aa, true));
    }

    public static List getKeySet(String value, String symbol) {
        LinkedList keyList = new LinkedList();
        if (value == null)
            return keyList;
        StringTokenizer token = new StringTokenizer(value, symbol);
        while (token.hasMoreTokens()) {
            String key = token.nextToken();
            keyList.add(key);
        }
        return keyList;
    }

    public static final String transform(String para)
            throws ApplicationException {
        if (para == null)
            return null;
        try {
            return new String(para.getBytes("ISO-8859-1"));
        } catch (Exception e) {
            throw new ApplicationException("encode String :" + para + " fail!");
        }
    }

    public static final String[] transform(String[] para)
            throws ApplicationException {
        if (para == null) {
            return null;
        }
        String[] ret = new String[para.length];
        for (int i = 0; i < para.length; ++i) {
            ret[i] = transform(para[i]);
        }
        return ret;
    }

    public static final boolean isNull(String para) {
        return ((para == null) || (para.trim().equals("")));
    }

    public static boolean isEmptyString(String str) {
        return ((str == null) || (str.equals("")));
    }

    public static boolean isBlankString(String str) {
        return ((str == null) || (str.trim().equals("")));
    }
}

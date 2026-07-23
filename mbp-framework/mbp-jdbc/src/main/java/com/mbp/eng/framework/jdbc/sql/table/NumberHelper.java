package com.mbp.eng.framework.jdbc.sql.table;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class NumberHelper {
    public static String doubleToScaleBigDecimal(double strDouble, int scale) {
        try {
            if (scale >= 0) {
                return new BigDecimal(strDouble).setScale(scale, 4).toString();
            }
        } catch (Exception localException) {
        }
        return "";
    }

    public static String getBigDecimal(String value, int scale) {
        return StringHelper.getBigDecimal(value, scale);
    }

    public static float floatValue(String value) {
        try {
            Float f = new Float(value.trim().toLowerCase());
            float r = f.floatValue();
            return r;
        } catch (NullPointerException e1) {
            return 0.0F;
        } catch (NumberFormatException e2) {
        }
        return 0.0F;
    }

    public static void longToBytes(long sd, byte[] dd) {
        for (int i = dd.length - 1; i >= 0; --i) {
            dd[i] = (byte) (int) sd;
            sd >>>= 8;
        }
    }

    public static long strToLong(String key) {
        byte[] bKey = key.getBytes();
        return bytesToLong(bKey);
    }

    public static long bytesToLong(byte[] rd) {
        long dd = 0L;
        int length = rd.length;

        for (int i = 0; i < length; ++i) {
            dd = dd << 8 | rd[i] & 0xFF;
        }
        return dd;
    }

    public static final String getNumStr(String num, boolean boo)
            throws AppException {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            if ((num == null) || (num.equals("")))
                return "--";
            if (Double.valueOf(num).doubleValue() == 0.0D)
                return "--";
            double dnum = Double.parseDouble(num);
            return decimalFormat.format(dnum);
        } catch (Exception e) {
            throw new AppException(num + "parsed is fail");
        }
    }

    public static final String getNumStr(String num, int decimal)
            throws AppException {
        BigDecimal bigDecimal = new BigDecimal(num);
        bigDecimal = bigDecimal.setScale(decimal, 4);
        return bigDecimal.toString();
    }

    public static final String getNumStr(String num) throws AppException {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            if ((num == null) || (num.equals("")))
                return decimalFormat.format(0.0D);
            double dnum = Double.parseDouble(num);
            return decimalFormat.format(dnum);
        } catch (Exception e) {
            throw new AppException(num + "parsed is fail");
        }
    }

    public static final String getPercentNumStr(String num) {
        return NumberFormat.getPercentInstance()
                .format(Double.parseDouble(num));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getNumStr("14.985", 2));
    }
}

package com.mbp.eng.framework.common.util.num;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumUtil {
    /**
     * 1用JAVA自带的函数 检查是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric1(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 2用正则表达式 检查是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 3用ascii码 检查是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric3(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }
    /**
     * 4 能否转换 检查是否为数字
     *
     * @param str
     * @return
     */
    public static boolean checkNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断一个字符串是否都为数字
     *
     * @param strNum
     * @return
     */
    public boolean isDigit0(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    /**
     * 判断一个字符串是否都为数字
     *
     * @param strNum
     * @return
     */
    public boolean isDigit1(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    /**
     * 截取数字
     *
     * @param content
     * @return
     */
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 截取非数字
     *
     * @param content
     * @return
     */
    public String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 判断一个字符串是否含有数字
     *
     * @param content
     * @return
     */
    public boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isPositiveNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 利用正则表达式判断是否为数字,包含负数情况
     *
     * @param str
     * @return
     */
    public static boolean isNumeric4(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为数字,包含负数情况
     *
     * @param str
     * @return
     */
    public static boolean isNumeric5(String str) {
        Boolean flag = false;
        String tmp;
        if (StringUtils.isNotBlank(str)) {
            if (str.startsWith("-")) {
                tmp = str.substring(1);
            } else {
                tmp = str;
            }
            flag = tmp.matches("^[0.0-9.0]+$");
        }
        return flag;
    }

    public static double getNum(double param) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        String str = decimalFormat.format(param);
        //System.out.println("=======" + str);
        Double number = Double.valueOf(str);
        return number;
    }

    public static double getFactNum(double param) {
        BigDecimal bigDecimal = new BigDecimal(param + "");
        BigDecimal factionNumNew = bigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(6);
        String str = numberFormat.format(factionNumNew);
        //System.out.println("=======" + str);
        Double number = Double.parseDouble(str);
        return number;
    }

    public static void main(String[] args) {
        long cutSize = Long.valueOf(3000) > 31691348 ? 31691348 : Long.valueOf(3000);
        double factionNum = cutSize * 1.0 / 31691348 * 1.0;
        System.out.println("==========" + getNum(factionNum));
        System.out.println("==========" + getFactNum(factionNum));
        if (factionNum < 1.0) {
            double factionNumUp = Math.ceil(factionNum * 1020000) / 1000000;
            factionNum = factionNumUp;
        }
        System.out.println("==========" + factionNum);
        System.out.println("==========" + factionNum * 31691348);
    }
}

package com.mbp.eng.framework.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class UtilAPP {
    /**
     * 统计字符串中包含几个需要查找的字符串
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int countStr(String str1, String str2) {
        int counter = 0;
        if (str1.indexOf(str2) == -1) {
            return 0;
        }
        while (str1.indexOf(str2) != -1) {
            counter++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return counter;
    }

    /**
     * 字符串数组中的每个字符串中是否包含所有查找的内容
     *
     * @param strs
     * @param s
     * @return
     */
    public static boolean isHave(String[] strs, String s) {
        for (int i = 0; i < strs.length; i++) {
            // 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
            if (strs[i].indexOf(s) != -1) {
                return true;
            }
        }
        return false;
    }

    /*
     * 2008/07/15   Chen Pei
     * 自动保留小数位数
     * 参数1：被操作数字,参数2：保留位数,参数3：是否四舍五入
     */
    public static double getNumberRound(double number, int decimal, boolean is4s5r) throws Exception {
        double result;

        if (is4s5r) {
            BigDecimal b = new BigDecimal(number);
            result = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            String tmp = "#.";
            for (int i = 0; i < decimal; i++) {
                tmp += "0";
            }
            result = Double.parseDouble(new DecimalFormat(tmp).format(number));
        }
        return result;
    }

    public static ArrayList<Integer> Getbit(long num) {
        int i;
        long t = num;
        ArrayList<Integer> blist = new ArrayList<Integer>();
        while (t >= 1) {
            i = new Long(t % 10).intValue();
            blist.add(i);
            t = t / 10;
        }
        return blist;
    }

    /**
     * 改变数组长度(可用于动态数组定义)
     *
     * @param oldArray
     * @param newSize
     * @return
     */
    public static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        return newArray;
    }

    /**
     * 字符串数组去重,利用set的元素唯一性
     *
     * @param arr1
     * @return
     */
    public static String[] union(String[] arr1) {
        Set<String> set = new HashSet<String>();
        for (String str : arr1) {
            set.add(str);
        }
        String[] result = {};
        return set.toArray(result);
    }

    /**
     * 求两个字符串数组的并集,利用set的元素唯一性
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] union(String[] arr1, String[] arr2) {
        Set<String> set = new HashSet<String>();
        for (String str : arr1) {
            set.add(str);
        }
        for (String str : arr2) {
            set.add(str);
        }
        String[] result = {};
        return set.toArray(result);
    }

    /**
     * 求两个数组的交集
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] intersect(String[] arr1, String[] arr2) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        List<String> list = new ArrayList<String>();
        for (String str : arr1) {
            if (!map.containsKey(str)) {
                map.put(str, Boolean.FALSE);
            }
        }
        for (String str : arr2) {
            if (map.containsKey(str)) {
                map.put(str, Boolean.TRUE);
            }
        }

        for (Iterator<Entry<String, Boolean>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, Boolean> e = (Entry<String, Boolean>) it.next();
            if (e.getValue().equals(Boolean.TRUE)) {
                list.add(e.getKey());
            }
        }
        return list.toArray(new String[]{});
    }
}

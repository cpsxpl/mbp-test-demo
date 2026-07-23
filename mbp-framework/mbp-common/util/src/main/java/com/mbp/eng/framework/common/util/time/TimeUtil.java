package com.mbp.eng.framework.common.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * 毫秒转换
     *
     * @param ms
     * @return
     */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        // 天
        String strDay = day < 10 ? "0" + day : "" + day;
        // 小时
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        // 分钟
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        // 秒
        String strSecond = second < 10 ? "0" + second : "" + second;
        // 毫秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;

        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute + " 分钟 " + strSecond + " 秒";
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间,注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author Chen Pei
     * @Date: 2018-12-28
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);

        if (calendar.after(beginCalendar) && calendar.before(endCalendar)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00]
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) throws ParseException {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        long now = simpleDateFormat.parse(curTime).getTime();
        long start = simpleDateFormat.parse(args[0]).getTime();
        long end = simpleDateFormat.parse(args[1]).getTime();
        if (args[1].equals("00:00")) {
            args[1] = "24:00";
        }
        if (end < start) {
            if (now >= end && now < start) {
                return false;
            } else {
                return true;
            }
        } else {
            if (now >= start && now < end) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param startTime 时间区间开始时间,10:00
     * @param endTime   时间区间结束时间,20:00
     * @param curTime   需要判断的时间 如10:00
     * @return
     * @throws ParseException
     */
    public static boolean isInTime(String startTime, String endTime, String curTime) throws ParseException {
        if (startTime == null || !startTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + startTime);
        }
        if (endTime == null || !endTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + endTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        long now = simpleDateFormat.parse(curTime).getTime();

        long start = simpleDateFormat.parse(startTime).getTime();
        long end = simpleDateFormat.parse(endTime).getTime();

        if (endTime.equals("00:00")) {
            endTime = "24:00";
        }
        if (end < start) {
            if (now >= end && now < start) {
                return false;
            } else {
                return true;
            }
        } else {
            if (now >= start && now < end) {
                return true;
            } else {
                return false;
            }
        }
    }
}

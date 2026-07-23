package com.mbp.eng.framework.common.util.date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static transient int gregorianCutoverYear = 1582;

    /**
     * 闰年中每月天数
     */
    private static final int[] DAYS_P_MONTH_LY = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * 非闰年中每月天数
     */
    private static final int[] DAYS_P_MONTH_CY = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * 代表数组里的年、月、日
     */
    private static final int Y = 0;
    private static final int M = 1;
    private static final int D = 2;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 验证字符串是否为合法日期 支持2019-03-12 2019/03/12 2019.03.12   HH:mm:ss HH:mm常用格式
     *
     * @param date
     * @return
     */
    public static boolean verifyDateLegal(String date) {
        if ((date.contains("-") && date.contains("/"))
                || (date.contains("-") && date.contains("."))
                || (date.contains("/") && date.contains("."))) {
            return false;
        }
        date.trim();
        StringBuilder timeSb = new StringBuilder();
        date = date.replaceAll("[\\.]|[//]", "-");
        String[] time = date.split(" ");
        timeSb.append(time[0]);
        timeSb.append(" ");
        if (time.length > 1) {
            timeSb.append(time[1]);
        }
        int i = time.length > 1 ? time[1].length() : 0;
        for (; i < 8; i++) {
            if (i == 2 || i == 5) {
                timeSb.append(":");
            } else {
                timeSb.append("0");
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.setLenient(false);
            sdf.parse(timeSb.toString());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * @param date
     * @param field
     * @param amount
     * @return
     * @throws Exception
     */
    public static String getAddCalendar(Date date, int field, int amount) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        /*field如果是
        1则代表的是对年份操作,
        2是对月份操作,
        3是对星期操作,
        5是对日期操作,
        11是对小时操作,
        12是对分钟操作,
        13是对秒操作,
        14是对毫秒操作*/
        calendar.add(field, amount);
        date = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 自定义日期的amount天的日期
     *
     * @param sdate
     * @param amount
     * @return
     * @throws Exception
     */
    public static final String getAddDate(String sdate, int amount) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date d = df.parse(sdate);

        String dateString = getAddCalendar(d, 5, amount);

        return dateString;
    }

    /**
     * 自定义日期的amount月的日期
     *
     * @param sdate
     * @param amount
     * @return
     * @throws Exception
     */
    public static final String getAddMon(String sdate, int amount) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date d = df.parse(sdate);

        String dateString = getAddCalendar(d, 2, amount);

        return dateString;
    }

    /**
     * 自定义日期的amount年的日期
     *
     * @param sdate
     * @param amount
     * @return
     * @throws Exception
     */
    public static final String getAddYear(String sdate, int amount) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date d = df.parse(sdate);

        String dateString = getAddCalendar(d, 1, amount);

        return dateString;
    }

    /**
     * 自定义日期的amount天的日期
     *
     * @param sdate
     * @param amount
     * @return
     * @throws Exception
     */
    public static final String getStrDate(String sdate, int amount) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date d = df.parse(sdate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        calendar.add(5, amount);
        d = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        String dateString = formatter.format(d);

        return dateString;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static final String getToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        String todayStr = simpleDateFormat.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static final String getTodaySSS() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String todayStrSSS = simpleDateFormat.format(new Date());
        return todayStrSSS;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static final String getDayId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static final String getToMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static final String getMonthId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 自定义日期月份拼接处每个月第一天
     *
     * @param monthStr
     * @return
     */
    public static final String getFirstDay(String monthStr) {
        return monthStr + "/01";
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static final String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前日期和当前时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 自定义时间日期转字符串格式yyyy/MM/dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getDateTimeStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        return str;
    }

    /**
     * 获取某日期区间的所有日期  日期倒序
     *
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param dateFormat 日期格式
     * @return 区间内所有日期
     */
    public static List<String> getPerDaysByStartAndEndDate(String startDate, String endDate, String dateFormat) throws ParseException {
        DateFormat format = new SimpleDateFormat(dateFormat);

        Date sDate = format.parse(startDate);
        Date eDate = format.parse(endDate);
        long start = sDate.getTime();
        long end = eDate.getTime();
        if (start > end) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(eDate);
        List<String> res = new ArrayList<String>();
        while (end >= start) {
            res.add(format.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            end = calendar.getTimeInMillis();
        }
        return res;
    }

    /**
     * 自定义日期返回自定义日期
     *
     * @param format
     * @return
     */
    public static final String getToday(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前周的第一天
     *
     * @return
     */
    public static final String getFristDayOfWeek() {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        calendar.add(5, 2 - calendar.get(7));
        Date fristDayOfWeek = calendar.getTime();
        return format.format(fristDayOfWeek);
    }

    public static final String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getHourId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getMinuteId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static final String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        String todayStr = simpleDateFormat.format(new Date());
        return todayStr;
    }

    /**
     * 自定义日期返回自定义日期
     *
     * @param format
     * @return
     */
    public static final String getTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String todayStr = simpleDateFormat.format(new Date());
        return todayStr;
    }

    /**
     * 获取日期时间
     *
     * @param currentTime
     * @return
     */
    public static final String getFormatTime(long currentTime) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        String todayStr = simpleDateFormat.format(date);
        return todayStr;
    }

    /**
     * 获取日期时间
     *
     * @param currentTime
     * @return
     */
    public static final String getFormatTimeId(long currentTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHH:mm:ss.SSS");
        Date date = new Date(currentTime);
        String todayStr = simpleDateFormat.format(date);
        return todayStr;
    }

    /**
     * @return String todayStr
     * @preserve 获取日期时间
     */
    public static final String getFormatTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
        Date date = new Date(time);
        String todayStr = simpleDateFormat.format(date);
        return todayStr;
    }

    /**
     * @param str
     * @param amount
     * @return
     */
    public static String getAmountDate(String str, int amount) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, amount);
        Date date1 = calendar.getTime();
        String outDate = simpleDateFormat.format(date1);
        return outDate;
    }

    /**
     * 取当前时间的前amount天的日期
     *
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String getAmountDate(int amount, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, amount);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 取当前时间的前amount天的日期
     *
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String getAmountDate(int amount) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, amount);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    /**
     * 取当前时间的前amount的小时
     *
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String getAmountHour(int amount) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, amount);
        Date monHour = c.getTime();
        String preMonHour = sdf.format(monHour);
        return preMonHour;
    }

    /**
     * 取当前时间的前amount的分钟
     *
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String getAmountMinute(int amount) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, amount);
        Date monMinute = c.getTime();
        String preMonMinute = sdf.format(monMinute);
        return preMonMinute;
    }

    /**
     * 取当前时间的前amount的秒
     *
     * @param amount
     * @return
     * @throws ParseException
     */
    public static String getAmountSecond(int amount) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, amount);
        Date monSecond = c.getTime();
        String preMonSecond = sdf.format(monSecond);
        return preMonSecond;
    }

    /**
     * 根据dayId获取每小时
     *
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDayHour(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYYMMddHH");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.HOUR, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

    /**
     * 根据dayId获取每分钟
     *
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDayMinute(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYYMMddHHmm");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.MINUTE, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

    /**
     * 根据dayId获取每秒
     *
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDaySecond(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.SECOND, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

    /**
     * 获取指定日期的年份
     *
     * @param date
     * @return
     */
    public static int getYearOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的月份
     *
     * @param date
     * @return
     */
    public static int getMonthOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期的日份
     *
     * @param date
     * @return
     */
    public static int getDayOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期的小时
     *
     * @param date
     * @return
     */
    static int getHourOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取指定日期的分钟
     *
     * @param date
     * @return
     */
    public static int getMinuteOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取指定日期的秒钟
     *
     * @param date
     * @return
     */
    public static int getSecondOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param date
     * @return long 毫秒
     */
    public static long getMillisOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 获取指定日期格式当前日期的字符型日期
     *
     * @param format 日期格式:
     *               格式1:"yyyy-MM-dd"
     *               格式2:"yyyy-MM-dd hh:mm:ss EE"
     *               格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
     * @return String 当前时间字符串
     */
    public static String getNowOfDateByFormat(String format) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(d);
        return dateStr;
    }

    /**
     * 获取指定日期格式系统日期的字符型日期
     *
     * @param format 日期格式:
     *               格式1:"yyyy-MM-dd"
     *               格式2:"yyyy-MM-dd hh:mm:ss EE"
     *               格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
     * @return String 系统时间字符串
     */
    public static String getSystemOfDateByFormat(String format) {
        long time = System.currentTimeMillis();
        Date d2 = new Date();
        Date d = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = simpleDateFormat.format(d);
        return dateStr;
    }

    /**
     * 获取字符日期一个月的天数
     *
     * @param date
     * @return 天数
     */
    public static long getDayOfMonth(Date date) throws ParseException {
        int year = getYearOfDate(date);
        int month = getMonthOfDate(date) - 1;
        int day = getDayOfDate(date);
        int hour = getHourOfDate(date);
        int minute = getMinuteOfDate(date);
        int second = getSecondOfDate(date);
        Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
        return calendar.getActualMaximum(calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定月份的第一天
     *
     * @param strDate 指定月份
     * @param format  日期格式
     * @return String   时间字符串
     */
    public static String getDateOfMonthBegin(String strDate, String format) throws ParseException {
        Date date = toUtilDateFromStrDateByFormat(strDate, format);
        return toStrDateFromDateByFormat(date, "yyyy-MM") + "-01";
    }

    /**
     * 获取指定月份的最后一天
     *
     * @param strDate 指定月份
     * @param format  日期格式
     * @return String   时间字符串
     */
    public static String getDateOfMonthEnd(String strDate, String format) throws ParseException {
        Date date = toUtilDateFromStrDateByFormat(getDateOfMonthBegin(strDate, format), format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return toStrDateFromDateByFormat(calendar.getTime(), format);
    }

    /**
     * 是否开始日期在结束日期之前(不包括相等)
     *
     * @param startDate
     * @param endDate
     * @return boolean 在结束日期前:ture;否则：false
     */
    public static boolean isStartDateBeforeEndDate(Date startDate, Date endDate) throws ParseException {
        long startTime = getMillisOfDate(startDate);
        long endTime = getMillisOfDate(endDate);
        return (startTime - endTime > (long) 0) ? true : false;
    }

    /**
     * 获取2个字符串日期的天数差
     *
     * @param startDate
     * @param endDate
     * @return 天数差
     */
    public static long getDaysOfTowDiffDate(String startDate, String endDate) throws ParseException {
        Date lstartDate = toUtilDateFromStrDateByFormat(startDate, "yyyy-MM-dd");
        Date lendDate = toUtilDateFromStrDateByFormat(endDate, "yyyy-MM-dd");
        long startTime = getMillisOfDate(lstartDate);
        long endTime = getMillisOfDate(lendDate);
        long betweenDays = (long) ((endTime - startTime) / (1000 * 60 * 60 * 24));
        return betweenDays;
    }

    /**
     * 获取2个字符串日期的周数差
     *
     * @param startDate
     * @param endDate
     * @return 周数差
     */
    public static long getWeeksOfTowDiffDate(String startDate, String endDate) throws ParseException {
        return getDaysOfTowDiffDate(startDate, endDate) / 7;
    }

    /**
     * 获取2个字符串日期的月数差
     *
     * @param startDate
     * @param endDate
     * @return 月数差
     */
    public static long getMonthsOfTowDiffDate(String startDate, String endDate) throws ParseException {
        return getDaysOfTowDiffDate(startDate, endDate) / 30;
    }

    /**
     * 获取2个字符串日期的年数差
     *
     * @param startDate
     * @param endDate
     * @return 年数差
     */
    public static long getYearsOfTowDiffDate(String startDate, String endDate) throws ParseException {
        return getDaysOfTowDiffDate(startDate, endDate) / 365;
    }

    /**
     * 判断给定日期是不是润年
     *
     * @param date 给定日期
     * @return boolean 如果给定的年份为闰年,则返回 true；否则返回 false。
     */
    public static boolean isLeapYear(Date date) {
        int year = getYearOfDate(date);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return gregorianCalendar.isLeapYear(year);
    }

    /**
     * 在给定的日期基础上添加年,月,日、时,分,秒 例如要再2006－10－21（uitl日期）添加3个月,并且格式化为yyyy-MM-dd格式,
     * 这里调用的方式为 addDate(2006－10－21,3,Calendar.MONTH,"yyyy-MM-dd")
     *
     * @param startDate 给定的日期
     * @param count     时间的数量
     * @param field     添加的域
     * @param format    时间转化格式,例如：yyyy-MM-dd hh:mm:ss 或者yyyy-mm-dd等
     * @return 添加后格式化的时间
     */
    public static String addDate(Date startDate, int count, int field, String format) throws ParseException {
        // 年,月,日、时,分,秒
        int lyear = getYearOfDate(startDate);
        int lmonth = getMonthOfDate(startDate) - 1;
        int lday = getDayOfDate(startDate);
        int lhour = getHourOfDate(startDate);
        int lminute = getMinuteOfDate(startDate);
        int lsecond = getSecondOfDate(startDate);
        Calendar calendar = new GregorianCalendar(lyear, lmonth, lday,
                lhour, lminute, lsecond);
        calendar.add(field, count);
        return toStrDateFromDateByFormat(calendar.getTime(), format);
    }

    /**
     * 获取相隔日期的每一天
     *
     * @param dateFirst
     * @param dateSecond
     */
    public static void display(String dateFirst, String dateSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateOne = dateFormat.parse(dateFirst);
            Date dateTwo = dateFormat.parse(dateSecond);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOne);
            while (calendar.getTime().before(dateTwo)) {
                System.out.println(dateFormat.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param time19 19位的时间 yyyy-MM-dd HH:mm:ss
     * @return 15位的时间 yyyyMMdd HHmmss
     */
    public static String time19To15(String time19) {
        String time15 = "";
        if (time19 == null || "".equals(time19) || time19.length() != 19) {
            time15 = "";
        } else {
            String[] r = time19.replace('-', '#').replace(':', '#').split("#");
            for (int i = 0; i < r.length; i++) {
                time15 += r[i];
            }
        }
        return time15;
    }

    /**
     * @param time15 15位的时间 yyyyMMdd HHmmss
     * @return 19位的时间 yyyy-MM-dd HH:mm:ss
     */
    public static String time15To19(String time15) {
        String time19 = "";
        if (time15 == null || "".equals(time15) || time15.length() != 15) {
            time19 = "";
        } else {
            String y = time15.substring(0, 4);
            String m = time15.substring(4, 6);
            String d = time15.substring(6, 8);
            String h = time15.substring(9, 11);
            String mi = time15.substring(11, 13);
            String s = time15.substring(13, 15);
            time19 = y + "-" + m + "-" + d + "   " + h + ":" + mi + ":" + s;
        }
        return time19;
    }

    /**
     * @param time16 16位的时间 yyyy-MM-dd HH:mm
     * @return 13位的时间 yyyyMMdd HHmm
     */
    public static String time16To13(String time16) {
        String time13 = "";
        if (time16 == null || "".equals(time16) || time16.length() != 16) {
            time13 = "";
        } else {
            String[] r = time16.replace('-', '#').replace(':', '#').split("#");
            for (int i = 0; i < r.length; i++) {
                time13 += r[i];
            }
        }
        return time13;
    }

    /**
     * @param time13 13位的时间 yyyyMMdd HHmm
     * @return 16位的时间 yyyy-MM-dd HH:mm
     */
    public static String time13To16(String time13) {
        String time16 = "";
        if (time13 == null || "".equals(time13) || time13.length() != 13) {
            time16 = "";
        } else {
            String y = time13.substring(0, 4);
            String m = time13.substring(4, 6);
            String d = time13.substring(6, 8);
            String h = time13.substring(9, 11);
            String mi = time13.substring(11, 13);
            time16 = y + "-" + m + "-" + d + "   " + h + ":" + mi;
        }
        return time16;
    }

    /**
     * @param date10 10位的日期 yyyy-MM-dd
     * @return 8位的日期 yyyyMMdd
     */
    public static String date10To8(String date10) {
        String date8 = "";
        if (date10 == null || "".equals(date10) || date10.length() != 10) {
            date8 = "";
        } else {
            String[] r = date10.split("-");
            for (int i = 0; i < r.length; i++) {
                date8 += r[i];
            }
        }
        return date8;
    }

    /**
     * @param date8 8位的日期 yyyyMMdd
     * @return 10位的日期 yyyy-MM-dd
     */
    public static String date8To10(String date8) {
        String date10 = "";
        if (date8 == null || "".equals(date8) || date8.length() != 8) {
            date10 = "";
        } else {
            String y = date8.substring(0, 4);
            String m = date8.substring(4, 6);
            String d = date8.substring(6, 8);
            date10 = y + "-" + m + "-" + d;
        }
        return date10;
    }

    /**
     * @param date7 7位的日期 yyyy-MM
     * @return 6位的日期 yyyyMM
     */
    public static String date7To6(String date7) {
        String date6 = "";
        if (date7 == null || "".equals(date7) || date7.length() != 7) {
            date6 = "";
        } else {
            String[] r = date7.split("-");
            for (int i = 0; i < r.length; i++) {
                date6 += r[i];
            }
        }
        return date6;
    }

    /**
     * @param date6 6位的日期 yyyyMM
     * @return 7位的日期 yyyy-MM
     */
    public static String date6To7(String date6) {
        String date7 = "";
        if (date6 == null || "".equals(date6) || date6.length() != 6) {
            date7 = "";
        } else {
            String y = date6.substring(0, 4);
            String m = date6.substring(4, 6);
            date7 = y + "-" + m;
        }
        return date7;
    }

    /**
     * 字符型日期转化util.Date型日期
     *
     * @param strDate 格式:"yyyy-MM-dd" / "yyyy-MM-dd hh:mm:ss"
     * @Param:p_strDate 字符型日期
     * @Return:java.util.Date util.Date型日期
     * @Throws: ParseException
     */
    public static Date toUtilDateFromStrDateByFormat(String strDate, String format) throws ParseException {
        Date ldate = null;
        DateFormat df = new SimpleDateFormat(format);
        if (strDate != null && (!"".equals(strDate)) && format != null && (!"".equals(format))) {
            ldate = df.parse(strDate);
        }
        return ldate;
    }

    /**
     * 字符型日期转化成sql.Date型日期
     *
     * @param strDate 字符型日期
     * @return java.sql.Date sql.Date型日期
     * @throws ParseException
     */
    public static java.sql.Date toSqlDateFromStrDate(String strDate) throws ParseException {
        java.sql.Date returnDate = null;
        DateFormat sdf = new SimpleDateFormat();
        if (strDate != null && (!"".equals(strDate))) {
            returnDate = new java.sql.Date(sdf.parse(strDate).getTime());
        }
        return returnDate;
    }

    /**
     * util.Date型日期转化指定格式的字符串型日期
     *
     * @param date
     * @param format String 格式1:"yyyy-MM-dd"
     *               String 格式2:"yyyy-MM-dd hh:mm:ss EE"
     *               String 格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
     * @return String
     */
    public static String toStrDateFromDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * util.Date型日期转化转化成Calendar日期
     *
     * @param utilDate
     * @return Calendar
     */
    public static Calendar toCalendarFromUtilDate(Date utilDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(utilDate);
        return c;
    }

    /**
     * util.Date型日期转化sql.Date(年月日)型日期
     *
     * @Param: p_utilDate util.Date型日期
     * @Return: java.sql.Date sql.Date型日期
     */
    public static java.sql.Date toSqlDateFromUtilDate(Date utilDate) {
        java.sql.Date returnDate = null;
        if (utilDate != null) {
            returnDate = new java.sql.Date(utilDate.getTime());
        }
        return returnDate;
    }

    /**
     * util.Date型日期转化sql.Time(时分秒)型日期
     *
     * @Param: p_utilDate util.Date型日期
     * @Return: java.sql.Time sql.Time型日期
     */
    public static java.sql.Time toSqlTimeFromUtilDate(Date utilDate) {
        java.sql.Time returnDate = null;
        if (utilDate != null) {
            returnDate = new java.sql.Time(utilDate.getTime());
        }
        return returnDate;
    }

    /**
     * util.Date型日期转化sql.Date(时分秒)型日期
     *
     * @Param: p_utilDate util.Date型日期
     * @Return: java.sql.Timestamp sql.Timestamp型日期
     */
    public static java.sql.Timestamp toSqlTimestampFromUtilDate(Date utilDate) {
        java.sql.Timestamp returnDate = null;
        if (utilDate != null) {
            returnDate = new java.sql.Timestamp(utilDate.getTime());
        }
        return returnDate;
    }

    /**
     * sql.Date型日期转化util.Date型日期
     *
     * @Param: sqlDate sql.Date型日期
     * @Return: java.util.Date util.Date型日期
     */
    public static Date toUtilDateFromSqlDate(java.sql.Date sqlDate) {
        Date returnDate = null;
        if (sqlDate != null) {
            returnDate = new Date(sqlDate.getTime());
        }
        return returnDate;
    }

    /**
     * 判断是否日期正确有几种格式
     *
     * @param createDate 格式1:"2007108"
     *                   格式2:"20070108"
     *                   格式3:"2007-01-08"
     *                   格式4:"2007-1-8"
     *                   格式5:"2009/01/02"
     * @return 天数差
     */
    public static String[] formatDate(String createDate) throws Exception {
        String[] backDate = new String[2];
        String message = "";
        String correctDate = "";

        if (!createDate.equals("")) {
            String year = "";
            String month = "";
            String date = "";
            if (createDate.indexOf("-") == -1 && createDate.indexOf("/") == -1) {
                if (createDate.length() <= 6) {
                    message = "日期" + createDate + "格式不对";
                } else {
                    year = createDate.substring(0, 4);
                    if (createDate.substring(4, 5).equals("")) {
                        message = "日期" + createDate + "格式不对";
                    } else {
                        month = createDate.substring(4, 6);
                        date = createDate.substring(6, 8);
                    }
                }
            } else if (createDate.indexOf("-") != -1) {
                String[] allDate = createDate.split("-");
                if (allDate.length != 3) {
                    message = "日期" + createDate + "格式不对";
                } else {
                    year = allDate[0];
                    month = allDate[1];
                    date = allDate[2];
                }
            } else if (createDate.indexOf("/") != -1) {
                String[] allDate = createDate.split("/");
                if (allDate.length != 3) {
                    message = "日期" + createDate + "格式不对";
                } else {
                    year = allDate[0];
                    month = allDate[1];
                    date = allDate[2];
                }
            }
            if (year.length() != 4) {
                message = "日期" + createDate + "格式不对";
            }
            if (month.equals("") || month.equals("0")) {
                message = "日期" + createDate + "格式不对";
            } else {
                if (month.length() == 1) {
                    month = "0" + month;
                }
            }
            if (date.equals("") || date.equals("0")) {
                message = "日期" + createDate + "格式不对";
            } else {
                if (date.length() == 1) {
                    date = "0" + date;
                }
            }
            if (message.equals("")) {
                correctDate = year + "/" + month + "/" + date;
            }
        }
        backDate[0] = message;
        backDate[1] = correctDate;
        return backDate;
    }

    /**
     * 验证日期格式YYYY/MM/DD
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static boolean checkDate(String date) throws Exception {
        try {
            if (date == null) {
                return false;
            }
            if (date.trim().length() != 10) {
                return false;
            }
            date = date.trim();
            String[] tempArray = date.split("/");
            if (tempArray.length != 3) {
                return false;
            }
            if (tempArray[0].length() != 4) { // year
                return false;
            }
            if (tempArray[1].length() != 2) { // month
                return false;
            }
            if (tempArray[2].length() != 2) { // day
                return false;
            }
            if (Integer.parseInt(tempArray[0]) < 1) {
                return false;
            }
            if (Integer.parseInt(tempArray[1]) < 1 || Integer.parseInt(tempArray[1]) > 12) {
                return false;
            }
            if (Integer.parseInt(tempArray[2]) < 1 || Integer.parseInt(tempArray[2]) > 31) {
                return false;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            formatter.setLenient(false);
            formatter.format(formatter.parse(date));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证日期格式YYYY/MM/DD
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        StringTokenizer stoken = new StringTokenizer(date, "/");
        int[] dayOfMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        try {
            String year = stoken.nextToken();
            String month = stoken.nextToken();
            String day = stoken.nextToken();

            return isDate(year, month, day);
        } catch (NoSuchElementException e) {
        }
        return false;
    }

    /**
     * 如:isDate("2009-10-19 15:00:47",yyyy-MM-dd hh:mm:ss")
     *
     * @param str
     * @param dateFormat
     * @return
     */
    public static boolean isDate(String str, String dateFormat) {
        if (str != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setLenient(false);
            try {
                simpleDateFormat.format(simpleDateFormat.parse(str));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean isDate(String year, String month, String day) {
        int[] dayOfMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        try {
            boolean bLeapYear = false;
            if (year.length() != 4)
                return false;
            int iYear = toNum(year);
            int iMonth = toNum(month);
            int iDay = toNum(day);
            if (isLeapYear(iYear))
                dayOfMonths[1] = 29;
            if ((iMonth < 1) || (iMonth > 12))
                return false;
            return (iDay >= 1) && (iDay <= dayOfMonths[(iMonth - 1)]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0)
            return false;
        return (year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 将代表日期的字符串分割为代表年月日的整形数组
     *
     * @param date
     * @return
     */
    public static int[] splitYMD(String date) {
        date = date.replace("-", "");
        int[] ymd = {0, 0, 0};
        ymd[Y] = Integer.parseInt(date.substring(0, 4));
        ymd[M] = Integer.parseInt(date.substring(4, 6));
        ymd[D] = Integer.parseInt(date.substring(6, 8));
        return ymd;
    }

    /**
     * 检查传入的参数代表的年份是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYears(int year) {
        return year >= gregorianCutoverYear ? ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) : (year % 4 == 0);
    }

    /**
     * 日期加1天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static int[] addOneDay(int year, int month, int day) {
        if (isLeapYears(year)) {
            day++;
            if (day > DAYS_P_MONTH_LY[month - 1]) {
                month++;
                if (month > 12) {
                    year++;
                    month = 1;
                }
                day = 1;
            }
        } else {
            day++;
            if (day > DAYS_P_MONTH_CY[month - 1]) {
                month++;
                if (month > 12) {
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }
        int[] ymd = {year, month, day};
        return ymd;
    }

    /**
     * 将不足两位的月份或日期补足为两位
     *
     * @param decimal
     * @return
     */
    public static String formatMonthDay(int decimal) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(decimal);
    }

    /**
     * 将不足四位的年份补足为四位
     *
     * @param decimal
     * @return
     */
    public static String formatYear(int decimal) {
        DecimalFormat df = new DecimalFormat("0000");
        return df.format(decimal);
    }

    /**
     * 计算两个日期之间相隔的天数
     *
     * @param begin
     * @param end
     * @return
     * @throws ParseException
     */
    public static long countDay(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        long day = 0;
        try {
            beginDate = format.parse(begin);
            endDate = format.parse(end);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 以循环的方式计算日期
     *
     * @param beginDate endDate
     * @param endDate
     * @return
     */
    public static List<String> getEveryday(String beginDate, String endDate) {
        long days = countDay(beginDate, endDate);
        int[] ymd = splitYMD(beginDate);
        List<String> everyDays = new ArrayList<String>();
        everyDays.add(beginDate);
        for (int i = 0; i < days; i++) {
            ymd = addOneDay(ymd[Y], ymd[M], ymd[D]);
            everyDays.add(formatYear(ymd[Y]) + "-" + formatMonthDay(ymd[M]) + "-" + formatMonthDay(ymd[D]));
        }
        return everyDays;
    }

    /**
     * @param param
     * @return
     * @throws NumberFormatException
     */
    public static final int toNum(String param) throws NumberFormatException {
        return Integer.parseInt(param);
    }

    /**
     * 比较日期是否有效
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final String string = "0123456789";
        if (dateStr.length() != pattern.length()) {
            return false;
        } else {
            for (int i = 0; i < dateStr.length(); i++) {
                if (string.indexOf(dateStr.substring(i, i + 1)) == -1) {
                    return false;
                }
            }
            try {
                simpleDateFormat.setLenient(false);
                String toLocaleString2 = simpleDateFormat.parse(dateStr).toLocaleString();
                String toLocaleString = toLocaleString2;
                System.out.println(toLocaleString);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public static boolean valid(String date1, String date2) {
        String s = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
        if (date1.matches(s) && date2.matches(s)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static final int betweenDateCount(String beginDate, String endDate) throws Exception {
        String date1 = beginDate.replace('/', '-');
        String date2 = endDate.replace('/', '-');
        long ldate1 = java.sql.Date.valueOf(date1).getTime();
        long ldate2 = java.sql.Date.valueOf(date2).getTime();
        long ldate = ldate2 - ldate1;
        int dateCount = (int) (ldate / 86400000L);
        return dateCount;
    }

    /**
     * 比较两个日期相差的天数,用第二个日期减第一个日期,获得差距天数
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static int minusDate(String date1, String date2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);

            long l = d2.getTime() - d1.getTime();

            int day = (int) (l / (1000 * 60 * 60 * 24)); // 1000毫秒*60秒*60分*24小时

            return day;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 比较日期大小,第一个日期小返回-1,一样大返回0,第二个日期小返回1
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static int compareDate(String date1, String date2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);

            return d1.compareTo(d2);
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * 方法名: 功能描述: 两个参数格式必须为20050827,而且不能为空。
     *
     * @param preDate
     * @param date
     * @return
     */
    public static boolean equal(String preDate, String date) {
        if (preDate == null && date == null) {
            return false;
        } else if (preDate == null) {
            return false;
        } else if (date == null) {
            return true;
        }
        preDate = preDate.trim();
        date = date.trim();
        if (preDate.length() == 8 && date.length() == 8) {
            Integer date1 = new Integer(preDate);
            Integer date2 = new Integer(date);
            if (date1.compareTo(date2) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法名: 功能描述: 两个参数格式必须为20050827,而且不能为空。
     * 参数说明： 第一个参数小于第二个参数返回true 返回值:
     * 函数返回值的说明
     */
    public static boolean lessThan(String preDate, String date) {
        if (preDate == null && date == null) {
            return false;
        } else if (preDate == null) {
            return true;
        } else if (date == null) {
            return false;
        }
        preDate = preDate.trim();
        date = date.trim();
        if (preDate.length() == 8 && date.length() == 8) {
            Integer date1 = new Integer(preDate);
            Integer date2 = new Integer(date);
            if (date1.compareTo(date2) < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法名: 功能描述: 两个参数格式必须为20050827,而且不能为空。
     * 参数说明： 第一个参数大于第二个参数返回true 返回值:
     * 函数返回值的说明 其他: // 其它说明
     */
    public static boolean greaterThan(String preDate, String date) {
        if (preDate == null && date == null) {
            return false;
        } else if (preDate == null) {
            return false;
        } else if (date == null) {
            return true;
        }
        preDate = preDate.trim();
        date = date.trim();
        if (preDate.length() == 8 && date.length() == 8) {
            Integer date1 = new Integer(preDate);
            Integer date2 = new Integer(date);
            if (date1.compareTo(date2) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置返回字符串的长度,如果传入的字符串长度不够,就在左边补0
     *
     * @param dateStr
     * @param length
     * @return
     * @throws Exception
     */
    public static String lpad(String dateStr, int length) throws Exception {
        if (dateStr.trim().length() != length) {
            dateStr = dateStr.trim();
            String tmp = "";
            for (int i = dateStr.length(); i < length; i++) {
                tmp += "0";
            }
            return tmp + dateStr;
        } else {
            return dateStr;
        }
    }

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String datePattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        // 实例化模板对象
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(oldPattern);
        // 实例化模板对象
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(newPattern);
        Date d = null;
        try {
            // 将给定的字符串中的日期提取出来
            d = simpleDateFormat1.parse(date);
        } catch (Exception e) { // 如果提供的字符串格式有错误,则进行异常处理
            // 打印异常信息
            e.printStackTrace();
        }
        return simpleDateFormat2.format(d);
    }

    /**
     * @param dataStr
     * @return
     */
    public static String strToFormat(String dataStr) {
        return StringUtils.isNotBlank(dataStr) ? dataStr.indexOf(".") != -1 ? dataStr.substring(0, dataStr.indexOf(".")) : dataStr : "";
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDateByPattern(Date date, String dateFormat) {
        logger.debug("date = {}, dateFormat = {}", date, dateFormat);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = simpleDateFormat.format(date);
        }
        return formatTimeStr;
    }

    public static DateTime parseFormatDate(String dateStr) {
        return dateTimeFormatter.parseDateTime(dateStr);
    }

    /**
     * 得到格式化后的当前日期
     *
     * @param sf, 可以为null
     * @return
     */
    public static String getFormattedCurTime(SimpleDateFormat sf) {
        if (sf == null) {
            sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date createDate = new Date();
        return sf.format(createDate);
    }

    public static String getFormattedCurTime() {
        return getFormattedCurTime(null);
    }

    /**
     * 判断是否是有效的cron表达式
     *
     * @param cronExpression
     * @return
     */
    public static boolean isValidExpression(String cronExpression) {
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        try {
            cronTriggerImpl.setCronExpression(cronExpression);
            Date date = cronTriggerImpl.computeFirstFireTime(null);
            return date != null && date.after(new Date());
        } catch (Exception e) {
            logger.error("[DateUtil.isValidExpression]:failed. throw ex:", e);
        }
        return false;
    }

    /**
     * 生成quartz cron表达式
     * <p>
     * http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html
     * <p>
     * 秒 Seconds           0-59
     * 分钟 Minutes         0-59
     * 小时 Hours           0-23
     * 日 Day of month      1-31 或者 L（the last day of the month）
     * 月份 Month           1-12 或者 JAN-DEC
     * 星期 Day of week     1-7 或者 SUN-SAT
     * 年度 Year            empty, 1970-2099
     *
     * @param time
     * @param month, day of month, 'L' 表示月末,为防止转义,要前后加单引号
     * @param week
     * @param day,   true/false, 代表是否每天都运行
     * @param hour
     * @param minute
     * @return
     */
    public static String geneCronExpression(String time, String month, String week, String day, String hour, String minute) throws Exception {
        String cronExpression = null;
        if (StringUtils.isNotBlank(time)) {
            DateTime dateTime = DateUtil.parseFormatDate(time);
            DateTime now = DateTime.now();
            long millis1 = dateTime.getMillis();
            long millis2 = now.getMillis();
            if (millis1 <= millis2) {
                throw new RuntimeException("您设置的调度时间已过期,请重新设置！");
            }
            String dateFormat = "s m H d M ? y";
            cronExpression = DateUtil.formatDateByPattern(dateTime.toDate(), dateFormat);
        } else {
            Calendar calendar = Calendar.getInstance();
            if (StringUtils.isNotBlank(month)) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                Date date = calendar.getTime();
                String dateFormat = String.format("0 m H %s * ? y", month);
                cronExpression = DateUtil.formatDateByPattern(date, dateFormat);
            } else if (StringUtils.isNotBlank(week)) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                Date date = calendar.getTime();
                String text = String.format("0 m H ? * %s y", week);
                String[] searchList = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                String[] replacementList = {"1", "2", "3", "4", "5", "6", "7"};
                String dateFormat = StringUtils.replaceEach(text, searchList, replacementList);
                cronExpression = DateUtil.formatDateByPattern(date, dateFormat);
            } else if (StringUtils.isNotBlank(day)) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                Date date = calendar.getTime();
                String dateFormat = "0 m H * * ? y";
                cronExpression = DateUtil.formatDateByPattern(date, dateFormat);
            }
        }
        return cronExpression;
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String sdate = sdf.format("2016/07/01");
        String edate = sdf.format("2016/07/31");
        Date date = sdf.parse("2016/07/01");
        System.out.println("===" + getAddCalendar(date, 1, 1));
        System.out.println("===" + getAddDate(sdate, 1));
        System.out.println("===" + getAddMon(sdate, 1));
        System.out.println("===" + getAddYear(sdate, 1));
        System.out.println("===" + getStrDate(sdate, 1));
        System.out.println("===" + getToday());
        System.out.println("===" + getFirstDay(sdate));
        System.out.println("===" + getFirstDay(sdate));
        System.out.println("===" + getCurrentDateTime());
        System.out.println("===" + getDateTimeStr(date));
        System.out.println("===" + getToday(sdate));
        System.out.println("===" + getFristDayOfWeek());
        System.out.println("===" + getAmountDate(2));
        System.out.println("===" + time19To15(""));
        System.out.println("===" + time15To19(""));
        System.out.println("===" + time16To13(""));
        System.out.println("===" + time13To16(""));
        System.out.println("===" + date10To8(""));
        System.out.println("===" + date8To10(""));
        System.out.println("===" + date7To6(""));
        System.out.println("===" + date6To7(""));
        System.out.println("===" + formatDate(sdate));
        System.out.println("===" + checkDate(sdate));
        System.out.println("===" + isDate(sdate));
        System.out.println("===" + isDate(sdate, "yyyy/mm/dd"));
        System.out.println("===" + isDate("", "", ""));
        System.out.println("===" + isLeapYear(2008));
        System.out.println("===" + toNum(sdate));
        System.out.println("===" + isValidDate("", ""));
        System.out.println("===" + betweenDateCount(sdate, edate));
        System.out.println("===" + minusDate(sdate, edate));
        System.out.println("===" + compareDate(sdate, edate));
        System.out.println("===" + equal(sdate, edate));
        System.out.println("===" + lessThan(sdate, edate));
        System.out.println("===" + greaterThan(edate, sdate));
        System.out.println("===" + lpad("1", 2));
        System.out.println("===" + datePattern(sdate, "yyyy-MM-dd", "yyyy/MM/dd"));

        long day;
        //获取2个字符日期的天数差
        String startDate = "2005-05-01";
        String endDate = "2006-09-30";
        day = getDaysOfTowDiffDate(startDate, endDate);
        System.out.println("day=========" + day);
        System.out.println("week=========" + day / 7);
        System.out.println("month=========" + day / 30);
        System.out.println("year=========" + day / 365);
        //获取字符日期一个月的天数
        String ldate = "2006-02-01";
        String lformat = "yyyy-MM-dd";
        day = getDayOfMonth(toUtilDateFromStrDateByFormat(ldate, lformat));
        System.out.println("day=========" + day);
        ///字符串型转化util.Date
        ldate = "2005-05-01";
        Date day1 = toUtilDateFromStrDateByFormat(ldate, "yyyy-MM-dd");
        System.out.println("java.util.Date =========" + day1);
        //字符型日期转化成 sql.Date 型日期
        ldate = "2005-05-01";
        Date day2 = toSqlDateFromStrDate(ldate);
        System.out.println("java.sql.Date=========" + day2);
        //util.Date型日期转化指定的格式字符串型
        Date ldate2 = new Date();
        String day3 = toStrDateFromDateByFormat(ldate2, "yyyy-MM-dd");
        String day4 = toStrDateFromDateByFormat(ldate2, "yyyy-MM-dd hh:mm:ss EE");
        System.out.println("string-Date-yyyy-MM-dd=========" + day3);
        System.out.println("string-Date-yyyy-MM-dd hh:mm:ss=========" + day4);
        //util.Date型日期转化 sql.Date 型日期
        Date ldate5 = new Date();
        java.sql.Date day5 = toSqlDateFromUtilDate(ldate5);
        System.out.println("java.sql.Date-=========" + day5);
        //sql.Date型日期转化util.Date型日期
        java.sql.Date ldate6 = toSqlDateFromStrDate("2005-05-01");
        Date date1 = toUtilDateFromSqlDate(ldate6);
        System.out.println("java.util.Date-=========" + date1);
        //获取当前日期的字符化处理
        String date2 = getNowOfDateByFormat("yyyyMM");
        System.out.println("java.util.Date-=========" + date2);
        //获取指定日期格式系统日期的字符型日期
        // yyyy年MM月dd日 hh:mm:ss EE
        String date3 = getSystemOfDateByFormat("yyyyMM");
        System.out.println("java.util.Date-=========" + date3);
        //获取指定月份的第一天
        // yyyy年MM月dd日 hh:mm:ss EE
        String lstrDate = "2005-09-11";
        String lformate = "yyyy-MM-dd";
        String date4 = getDateOfMonthBegin(lstrDate, lformate);
        System.out.println("java.util.Date-=========" + date4);
        //取得指定月份的最后一天
        // yyyy年MM月dd日 hh:mm:ss EE
        String lstrDate2 = "2006-02-11";
        String lformate2 = "yyyy-MM-dd";
        String date5 = getDateOfMonthEnd(lstrDate2, lformate2);
        System.out.println("java.util.Date-=========" + date5);
        //获取指定日期的年份,月份,日份,小时,分,秒,毫秒
        // yyyy年MM月dd日 hh:mm:ss EE
        String lstrDate3 = "2004-02-11 08:25:15";
        String lformat3 = "yyyy-MM-dd hh:mm:ss";
        int year = getYearOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        int month = getMonthOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        int day6 = getDayOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        int hour = getHourOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        int minute = getMinuteOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        int second = getSecondOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        long millis = getMillisOfDate(toUtilDateFromStrDateByFormat(lstrDate3, lformat3));
        System.out.println("year==========" + year);
        System.out.println("month==========" + month);
        System.out.println("day==========" + day6);
        System.out.println("hour==========" + hour);
        System.out.println("minute==========" + minute);
        System.out.println("second==========" + second);
        System.out.println("millis==========" + millis);
        //是否开始日期在结束日期之前
        Date lstartDate = toUtilDateFromStrDateByFormat("2005-02-11", "yyyy-MM-dd");
        Date lendDate = toUtilDateFromStrDateByFormat("2005-02-11", "yyyy-MM-dd");
        boolean isBofore = isStartDateBeforeEndDate(lstartDate, lendDate);
        System.out.println("isBofore==" + isBofore);
        /**
         *在当前的时间基础上添加月、天、或者其他 例如添加3个月,并且格式化为yyyy-MM-dd格式,
         *这里调用的方式为addMonth(3,Calendar.MONTH,"yyyy-MM-dd")
         **/
        Date lstartDate2 = toUtilDateFromStrDateByFormat("2006-02-27 07:59:59", "yyyy-MM-dd hh:mm:ss");
        int lcount = 2;
        int lfield = Calendar.DATE;
        String lformat2 = "yyyy-MM-dd hh:mm:ss";
        String date6 = addDate(lstartDate2, lcount, lfield, lformat2);
        System.out.println("addDate============" + date6);
        //判断给定日期是不是润年
        Date ldate7 = toUtilDateFromStrDateByFormat("2000-01-25", "yyyy-MM-dd");
        boolean isLeap = isLeapYear(ldate7);
        System.out.println("isLeapYear=" + isLeap);
    }
}

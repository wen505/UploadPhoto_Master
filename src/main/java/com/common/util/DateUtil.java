package com.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cs005 on 2017/5/15.
 */
public class DateUtil {
    //转换为Date
    public static Date parseDate(String dateStr, String format) {
        java.util.Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
            date = (Date) df.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }

    //转换为String
    public static String parseString(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat df = new SimpleDateFormat(format,Locale.CHINA);
                result = df.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    //根据与某日期相隔的天数获取日期
    public static Date diffDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将日期格式化为字符串日期
     * @param date
     * @return
     */
    public static String formatDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//默认格式：2015-2-15 下午12:57:32
        String stringDate = null;
        if(date!= null){
            stringDate = sdf.format(date);
            return stringDate;
        }
        return null;
    }
}

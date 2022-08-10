package com.bjpowernode.crm.commons.utils;

/**
 * @Author 临渊
 * @Date 2022-08-10 12:28
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 专门对日期和时间格式化  yyyy-MM-dd HH:mm:ss
 */
public class DateUtils {

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}

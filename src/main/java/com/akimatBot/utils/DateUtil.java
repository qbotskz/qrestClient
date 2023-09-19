package com.akimatBot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat formatDateAndTimeSS = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat onlyOClock = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat formatDateAndTimeSS2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.wwwwww");
    private static final SimpleDateFormat formatDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat formatDate2 = new SimpleDateFormat("dd-MM-yyyy");

    public static String getDbMmYyyyHhMmSs2(Date date) {
        formatDateAndTimeSS2.setLenient(false);

        return formatDateAndTimeSS2.format(date);
    }

    public static String getDbMmYyyyHhMmSs(Date date) {
        if (date != null) {
            formatDateAndTimeSS.setLenient(false);
            return formatDateAndTimeSS.format(date);
        }
        return null;
    }

    public static String getOnlyOClock(Date date) {
        if (date != null) {
            onlyOClock.setLenient(false);
            return onlyOClock.format(date);
        }
        return null;
    }

    public static String getDayDate(Date date) {
        formatDateAndTime.setLenient(false);
        return formatDate.format(date);
    }

    public static String getDayDate2(Date date) {
        formatDateAndTime.setLenient(false);
        return formatDate2.format(date);
    }

    public static String getString(Date date, String format) {
        return getResult(new SimpleDateFormat(format), date);
    }

    private static String getResult(SimpleDateFormat simpleDateFormat, Date date) {
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.format(date);
    }

    public static String getTimeDate(Date date) {
        return getString(date, "HH:mm:ss");
    }

    public static String getTimeDate2(Date date) {
        return getString(date, "HH:mm");
    }

    public static String getFullTimeDate(Date date) {
        return getString(date, "YYYY-MM-DD HH:mm:ss");
    }

    public static String getDateAndTime(Date date) {
        if (date == null) return "";
        return getString(date, "dd.MM.yyyy HH:mm");
    }

    public static Date getNextMorning() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        if (new Date().after(calendar.getTime())) calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date getHour(int hour) {
        Date date = new Date();
        if (date.getHours() >= hour) date.setDate(date.getDate() + 1);
        date.setHours(hour);
        date.setMinutes(0);
        date.setSeconds(1);
        return date;
    }
}

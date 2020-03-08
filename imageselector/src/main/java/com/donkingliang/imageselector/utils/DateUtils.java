package com.donkingliang.imageselector.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getImageTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar imageTime = Calendar.getInstance();
        imageTime.setTimeInMillis(time);
        if (sameDay(calendar, imageTime)) {
            return "今天";
        } else if (sameWeek(calendar, imageTime)) {
            return "本周";
        } else if (sameMonth(calendar, imageTime)) {
            return "本月";
        } else {
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
            return sdf.format(date);
        }
    }

    public static boolean sameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean sameWeek(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean sameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    //毫秒值转 分秒
    public static String getTimeToMinutesSeconds(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;


        String secondsStr = String.valueOf(seconds);
        String minutesStr = String.valueOf(minutes);

        if (seconds<10){
            secondsStr = "0"+seconds;
        }
        if (minutes<10){
            minutesStr = "0"+minutes;
        }
//            int hours = totalSeconds / 3600;
//            return hours > 0 ? String.format("%02dh%02dm%02ds", hours, minutes, seconds) : String.format("%02dm%02ds", minutes, seconds);
        return minutesStr+":"+secondsStr;
    }


}

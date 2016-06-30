package heath.android.sample.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by heath on 2016/6/22.
 */
public class TimeUtils {

    public static String convertTimeStringToTimeString(String timeString, String ori_time_format, String target_time_format) {
        SimpleDateFormat format = new SimpleDateFormat(ori_time_format, Locale.US);
        String str = null;
        try {
            Date date = format.parse(timeString);
            SimpleDateFormat target_format = new SimpleDateFormat(target_time_format, Locale.US);
            str = target_format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static long convertTimeStringToMilliseconds(String timeString, String time_format) {
        SimpleDateFormat format = new SimpleDateFormat(time_format, Locale.US);
        Date date = null;
        try {
            date = format.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = null;
        return date.getTime();
    }

    public static String convertMillisecondsToTimeString(long milli, String time_format) {
        Date date = new Date(milli);
        SimpleDateFormat format = new SimpleDateFormat(time_format, Locale.US);
        String str = format.format(date);

        return str;
    }

    /*
    input:	timestamp with timezone offset
    output:	2014-01-01T00:00:00+0800
    */
    public static String getTimeStringAddTimeZoneInfo(long timestamp, String time_format) {
        String converted_time = convertMillisecondsToTimeString(timestamp, time_format);
        int offset = TimeZone.getDefault().getRawOffset();
        String prefix = "+";
        if (offset < 0) {
            prefix = "-";
        }

        DecimalFormat format = new DecimalFormat("00");
        String timezone = prefix + format.format(Math.abs(offset / 1000 / 60 / 60)) + "00";
        return converted_time + timezone;
    }
}

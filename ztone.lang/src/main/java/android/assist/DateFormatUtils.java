package android.assist;

import android.log.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {
    private static final String TAG = "DateFormat";

    public static final String PATTERN_FORMAT_DATE = "yyyy/MM/dd HH:mm:ss";

    public static Date parse(String date, String pattern) {
        Date dfDate = null;

        if (Assert.notEmpty(date)) {
            try {
                dfDate = new SimpleDateFormat(Assert.notEmpty(pattern) ? pattern : PATTERN_FORMAT_DATE).parse(date);
            } catch (Exception e) {
                Log.e(TAG, "date: " + date + " to format: " + pattern);
                Log.v(TAG, e);
            }
        }

        return dfDate;
    }

    public static String formatCurrentTimeMillis(String format) {

        return format(new Date(), format);
    }

    public static String format(Date date, String pattern) {

        return new SimpleDateFormat(Assert.notEmpty(pattern) ? pattern : PATTERN_FORMAT_DATE) //
                .format(date != null ? date : new Date());
    }

    public static String format(long date, String pattern) {

        return new SimpleDateFormat(Assert.notEmpty(pattern) ? pattern : PATTERN_FORMAT_DATE) //
                .format(new Date(date <= 0 ? System.currentTimeMillis() : date));
    }


}

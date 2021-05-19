package android.text;

import android.assist.Assert;
import android.log.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormats {
    private static final String TAG = "DateFormats";

    public static final String PATTERN_FORMAT_DATE = "yyyy/MM/dd HH:mm:ss.SSS";

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

    public static String nowMillis() {

        return format(0, "");
    }

    public static String nowMillis(String pattern) {

        return format(0, pattern);
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

package android.ztone;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static int getInteralDays(Date date) {
        if (date == null) {
            return 0;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 0);

        Calendar theDay = Calendar.getInstance();
        theDay.setTime(date);
        theDay.set(Calendar.HOUR_OF_DAY, 23);
        theDay.set(Calendar.MINUTE, 59);
        theDay.set(Calendar.SECOND, 59);
        theDay.set(Calendar.MILLISECOND, 0);

        if (theDay.getTimeInMillis() - today.getTimeInMillis() > 0) {
            long rest = (theDay.getTimeInMillis() - today.getTimeInMillis()) % (1000 * 60 * 60 * 24);
            if (rest > 0) {
                return (int) ((theDay.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24) + 2);
            } else {
                return (int) ((theDay.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24) + 1);
            }
        } else if (theDay.getTimeInMillis() - today.getTimeInMillis() == 0) {
            return 1;
        }
        return 0;
    }

    public static String formateDay(Date date) {
        String dateString = dateFormat.format(date);
        return dateString;
    }

    @Test
    public void main() throws ParseException {
        Date date = dateFormat.parse("2021-03-20");
        System.out.println(getInteralDays(date));
    }
}

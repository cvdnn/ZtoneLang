package android.check;

/**
 * Created by xiot on 2017/10/31.
 */

public class ValidateUtils {

    public static boolean check(Validator v) {

        return v != null && v.check();
    }
}

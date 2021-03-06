package android.text;

import static java.util.Locale.CHINESE;

public class Chars {

    public static String format(String format, Object... args) {
        return String.format(CHINESE, format, args);
    }
}

package android.log;

/**
 * Created by handy on 17-3-14.
 */

public interface OnLoggingPrinter {

    void print(long t, String tag, String message);
}

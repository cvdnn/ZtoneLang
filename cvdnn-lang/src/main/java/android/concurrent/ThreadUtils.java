package android.concurrent;

/**
 * Created by handy on 17-3-15.
 */
@Deprecated
public class ThreadUtils {

    public static void sleepThread(long time) {
        Threads.sleepThread(time);
    }

    public static Thread start(Runnable runnable, String threadName) {

        return Threads.start(runnable, threadName);
    }
}

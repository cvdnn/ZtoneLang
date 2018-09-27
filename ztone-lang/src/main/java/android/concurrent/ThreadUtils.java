package android.concurrent;

import android.assist.Assert;
import android.reflect.TrackingUtils;

/**
 * Created by handy on 17-3-15.
 */

public class ThreadUtils {

    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            // do nothing
        }
    }

    public static Thread start(Runnable runnable, String threadName) {
        Thread thread = null;
        if (runnable != null) {
            thread = new Thread(TrackingUtils.proxy(runnable), //
                    Assert.notEmpty(threadName) ? threadName : "THREAD_" + System.currentTimeMillis());
            thread.start();
        }

        return thread;
    }
}

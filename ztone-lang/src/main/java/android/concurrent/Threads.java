package android.concurrent;

import android.assist.Assert;
import android.reflect.TrackingUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by handy on 17-3-15.
 */

public class Threads {
    private static final AtomicInteger ThreadNum = new AtomicInteger(1);

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Deprecated
    public static void sleepThread(long time) {
        sleep(time);
    }

    public static Thread start(Runnable runnable) {

        return start(runnable, null);
    }

    public static Thread start(Runnable runnable, String threadName) {
        Thread thread = null;
        if (runnable != null) {
            thread = new Thread(TrackingUtils.proxy(runnable), //
                    ((Assert.notEmpty(threadName) ? (threadName + "_") : "__THREAD_") + ThreadNum.incrementAndGet()));
            thread.start();
        }

        return thread;
    }
}

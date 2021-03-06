package android.task;


import android.Loople;

import java.util.concurrent.Future;

/**
 * FIXME
 */
public abstract class ScheduleTask implements Runnable {
    protected final long delayMillis;

    private Future<?> mRunFuture;

    public ScheduleTask(long millis) {
        delayMillis = millis;
    }

    protected abstract void onRunning();

    public void start(boolean rightNow) {
        if (rightNow) {
            mRunFuture = Loople.Task.schedule(this);
        } else {
            mRunFuture = Loople.Task.schedule(this, delayMillis);
        }
    }

    @Override
    public final void run() {
        onRunning();

        mRunFuture = Loople.Task.schedule(this, delayMillis);
    }

    public void stop() {
        mRunFuture = Loople.Task.cancel(mRunFuture);
    }
}

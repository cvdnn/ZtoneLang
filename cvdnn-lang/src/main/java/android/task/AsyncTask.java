package android.task;

import android.Loople;
import android.assist.Assert;

import androidx.annotation.AnyThread;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by handy on 17-3-28.
 */

public abstract class AsyncTask<Result> implements Runnable, Canellation {
    private Future<?> mFuture;
    private Runnable mPostRunnable;

    protected abstract Result doInBackground();

    protected void onPostExecute(Result result) {

    }

    @AnyThread
    public final Future<?> execute() {
        cancel();

        mFuture = Loople.Task.schedule(this);

        return mFuture;
    }

    @AnyThread
    public final Future<?> execute(long millis) {
        cancel();

        mFuture = Loople.Task.schedule(this, millis);

        return mFuture;
    }

    @AnyThread
    public final Future<?> execute(long delay, TimeUnit unit) {
        cancel();

        mFuture = Loople.Task.schedule(this, delay, unit);

        return mFuture;
    }

    @AnyThread
    public final void slice(long initialDelay, long period, TimeUnit unit) {
        cancel();

        Loople.Task.slice(this, initialDelay, period, unit);
    }

    @AnyThread
    public final void chain(long initialDelay, long period, TimeUnit unit) {
        cancel();

        Loople.Task.chain(this, initialDelay, period, unit);
    }

    @Override
    public final void run() {
        Result result = doInBackground();

        Loople.Main.post(mPostRunnable = () -> {
            onPostExecute(result);

            mPostRunnable = null;
        });

        mFuture = null;
    }

    @Override
    public final void cancel() {
        mFuture = Loople.Task.cancel(mFuture);

        if (mPostRunnable != null) {
            Loople.Main.removeCallbacks(mPostRunnable);

            mPostRunnable = null;
        }
    }

    public static AsyncTask cancel(AsyncTask... tasks) {
        if (Assert.notEmpty(tasks)) {
            for (AsyncTask at : tasks) {
                if (at != null) {
                    at.cancel();
                }
            }
        }

        return null;
    }
}
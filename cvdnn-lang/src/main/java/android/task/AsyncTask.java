package android.task;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.Loople;
import android.assist.Assert;

import androidx.annotation.AnyThread;
import androidx.annotation.Nullable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by handy on 17-3-28.
 */

public abstract class AsyncTask<R> implements Runnable, Canellation {
    private Future<?> mFuture;
    private Runnable mPreRunnable, mPostRunnable;

    protected void onPreExecute() {
    }

    protected abstract R doInBackground();

    protected void onPostExecute(R result) {

    }

    @AnyThread
    public final Future<?> execute() {

        return execute(0, MILLISECONDS);
    }

    @AnyThread
    public final Future<?> execute(long delay) {

        return execute(delay, MILLISECONDS);
    }

    @AnyThread
    public final Future<?> execute(long delay, TimeUnit unit) {
        cancel();

        mPreRunnable = () -> {
            onPreExecute();

            mFuture = Loople.Task.schedule(this, delay, unit);
        };
        Loople.Main.post(mPreRunnable);

        return mFuture;
    }

    @AnyThread
    public final void slice(long initialDelay, long period, TimeUnit unit) {
        cancel();

        Loople.Task.slice(this::execute, initialDelay, period, unit);
    }

    @AnyThread
    public final void chain(long initialDelay, long period, TimeUnit unit) {
        cancel();

        Loople.Task.chain(this::execute, initialDelay, period, unit);
    }

    @Override
    public final void run() {
        R rst = doInBackground();
        Loople.Main.post(mPostRunnable = () -> {
            onPostExecute(rst);

            mPostRunnable = null;
        });

        mFuture = null;
    }

    @Override
    public final void cancel() {
        mPreRunnable = Loople.Main.cancel(mPreRunnable);

        mFuture = Loople.Task.cancel(mFuture);

        mPostRunnable = Loople.Main.cancel(mPostRunnable);
    }

    @Nullable
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
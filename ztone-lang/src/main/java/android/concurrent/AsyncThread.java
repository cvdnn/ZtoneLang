package android.concurrent;

import android.os.Handler;
import android.os.Looper;

public abstract class AsyncThread<Result> implements Runnable {
    private static final String ASYNC_TASK_NAME = "__ASYNC_TASK";

    private static final Handler mMainLoop = new Handler(Looper.getMainLooper());

    protected abstract Result doInBackground();

    protected void onPostExecute(Result result) {

    }

    public final void execute() {
        ThreadPool.Impl.execute(ASYNC_TASK_NAME, this);
    }

    @Override
    public void run() {
        final Result result = doInBackground();
        mMainLoop.post(() -> onPostExecute(result));
    }
}

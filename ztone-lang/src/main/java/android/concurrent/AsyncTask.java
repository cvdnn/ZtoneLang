package android.concurrent;


/**
 * Created by handy on 17-3-28.
 */

public abstract class AsyncTask<Result> extends android.os.AsyncTask<Object, Integer, Result> {

    protected abstract void doInBackground();

    @Override
    protected final Result doInBackground(Object[] params) {
        doInBackground();

        return null;
    }

    @Override
    protected final void onCancelled(Result r) {
        super.onCancelled(r);
    }

    public final void start() {
        super.execute();
    }
}

package android.concurrent;


/**
 * Created by handy on 17-3-28.
 */

public abstract class AsyncTask extends android.os.AsyncTask {

    protected abstract void doInBackground();

    protected void onPostExecute() {

    }

    @Override
    protected final Object doInBackground(Object[] params) {
        doInBackground();

        return null;
    }

    @Override
    protected final void onPostExecute(Object o) {
        onPostExecute();
    }

    @Override
    protected final void onCancelled(Object o) {
        super.onCancelled(o);
    }
}

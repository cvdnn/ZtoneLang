package android.task;

import android.Void;

import static android.Const.NIL;

public abstract class AsyncThread extends AsyncTask<Void> {
    protected abstract void onInBackground();

    protected void onPostExecute() {

    }

    @Override
    protected final Void doInBackground() {
        doInBackground();

        return NIL;
    }

    @Override
    protected final void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        onPostExecute();
    }
}

package android.collection;

import android.log.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class BlockNode<E> extends LinkedBlockingQueue<E> {
    private static final String TAG = "BlockNode";

    public BlockNode() {
        super(1);
    }

    @Deprecated
    @Override
    public E take() {
        E e = null;

        try {
            e = super.take();
        } catch (Exception ex) {
            Log.i(TAG, ex);
        }

        return e;
    }

    @Override
    public E poll() {
        E e = null;

        try {
            e = super.poll();
        } catch (Exception ex) {
            Log.i(TAG, ex);
        }

        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) {
        E e = null;

        try {
            e = super.poll(timeout, unit);
        } catch (Exception ex) {
            Log.i(TAG, ex);
        }

        return e;
    }
}

package android.collection;

import android.log.Log;
import android.util.SparseArray;

import java.util.concurrent.TimeUnit;

public class ChokeArray<E> extends SparseArray<ChokePoint<E>> {

    public synchronized ChokePoint<E> make(int key) {
        ChokePoint<E> chokePoint = get(key);
        if (chokePoint == null) {
            chokePoint = new ChokePoint<>();
            put(key, chokePoint);
        }

        return chokePoint;
    }

    public synchronized boolean revert(int key, E e) {
        boolean result = false;

        ChokePoint<E> chokePoint = get(key);
        if (chokePoint != null) {
            try {
                remove(key);
                chokePoint.put(e);
                result = true;
            } catch (Exception exce) {
                Log.e(exce);
            }
        }

        return result;
    }

    public synchronized E poll(int key) {
        E e = null;
        try {
            e = make(key).poll();
        } catch (Exception exce) {
        }

        return e;
    }

    public synchronized E poll(int key, long timeout, TimeUnit unit) {
        E e = null;
        try {
            e = make(key).poll(timeout, unit);
        } catch (Exception exce) {
        }

        return e;
    }

    public synchronized boolean containKey(int key) {
        return indexOfKey(key) >= 0;
    }

    public synchronized boolean contain(int key) {
        return get(key) != null;
    }
}

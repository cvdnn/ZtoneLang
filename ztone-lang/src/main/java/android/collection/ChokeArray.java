package android.collection;

import android.log.Log;
import android.util.SparseArray;

import java.util.concurrent.TimeUnit;

public class ChokeArray<E> extends SparseArray<ChokePoint<E>> {

    public ChokePoint<E> make(int key) {
        ChokePoint<E> chokePoint = get(key);
        if (chokePoint == null) {
            chokePoint = new ChokePoint<>();
            put(key, chokePoint);
        }

        return chokePoint;
    }

    public boolean revert(int key, E e) {
        boolean result = false;

        ChokePoint<E> chokePoint = get(key);
        if (chokePoint != null) {
            try {
                chokePoint.put(e);
                result = true;
            } catch (Exception exce) {
                Log.e(exce);
            }
        }

        return result;
    }

    public E poll(int key) {
        E e = null;
        try {
            e = make(key).poll();
        } catch (Exception exce) {
        }

        return e;
    }

    public E poll(int key, long timeout, TimeUnit unit) {
        E e = null;
        try {
            e = make(key).poll(timeout, unit);
        } catch (Exception exce) {
        }

        return e;
    }
}

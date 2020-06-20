package android.collection;

import android.log.Log;
import android.util.SparseArray;

import java.util.concurrent.TimeUnit;

public class ChokeArray<E> extends SparseArray<ChokePoint<E>> {

    public ChokeArray() {
        this(32);
    }

    public ChokeArray(int capacity) {
        super(capacity);
    }

    public ChokePoint<E> quemake(int key) {
        ChokePoint<E> chokePoint = new ChokePoint<>();
        put(key, chokePoint);

        return chokePoint;
    }

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
                remove(key);
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

    /**
     * 唯一
     *
     * @param key
     *
     * @return
     */
    public E quepoll(int key) {
        E e = null;
        try {
            e = quemake(key).poll();
        } catch (Exception exce) {
        }

        return e;
    }

    public E quepoll(int key, long timeout, TimeUnit unit) {
        E e = null;
        try {
            e = quemake(key).poll(timeout, unit);
        } catch (Exception exce) {
        }

        return e;
    }

    public boolean contain(int key) {
        return get(key) != null;
    }
}

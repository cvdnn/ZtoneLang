package android.collection;

import android.log.Log;
import android.util.SparseArray;

public class ChokeArray<E> extends SparseArray<ChokePoint<E>> {

    public ChokePoint<E> make(int key) {
        ChokePoint<E> chokePoint = new ChokePoint<>();
        put(key, chokePoint);

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
}

package android.collection;

import android.reflect.Clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SparseArray<E> extends android.util.SparseArray<E> {
    /**
     * Creates a new SparseArray containing no mappings.
     */
    public SparseArray() {
        this(10);
    }

    /**
     * Creates a new SparseArray containing no mappings that will not
     * require any additional memory allocation to store the specified
     * number of mappings.  If you supply an initial capacity of 0, the
     * sparse array will be initialized with a light-weight representation
     * not requiring any additional array allocations.
     */
    public SparseArray(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public SparseArray<E> clone() {
        SparseArray<E> clone = null;
        try {
            clone = (SparseArray<E>) super.clone();
        } catch (Exception e) {
            /* ignore */
        }

        return clone;
    }

    public boolean containsKey(int key) {
        return indexOfKey(key) != -1;
    }

    public boolean containsValue(E e) {
        return indexOfValue(e) != -1;
    }

    public void removeBy(Predicate<E> p) {
        if (p != null) {
            for (int i = size() - 1; i >= 0; i--) {
                E e = valueAt(i);
                if (p.test(e)) {
                    removeAt(i);
                }
            }
        }
    }

    public int[] keys() {
        int size = size();
        int[] keys = new int[size];

        for (int i = 0; i < size; i++) {
            keys[i] = keyAt(i);
        }

        return keys;
    }

    public List<E> values() {
        int size = size();
        ArrayList<E> values = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            values.add(valueAt(i));
        }

        return values;
    }

    public void gcc() {
        Clazz.invoke(android.util.SparseArray.class, this, "gc");
    }
}

package android.collection;

import android.log.Log;
import android.util.SparseArray;

public class BlockingArray<E> extends SparseArray<BlockNode<E>> {

    public BlockingArray() {
        this(32);
    }

    public BlockingArray(int capacity) {
        super(capacity);
    }

    /**
     * 创建以及新的添加到队列中
     *
     * @param key
     *
     * @return
     */
    public BlockNode<E> make(int key) {
        BlockNode<E> node = new BlockNode<>();
        put(key, node);

        return node;
    }

    /**
     * 先从队列中查询，没有添加一个新的
     *
     * @param key
     *
     * @return
     */
    public BlockNode<E> obtain(int key) {
        BlockNode<E> node = get(key);
        if (node == null) {
            node = make(key);
        }

        return node;
    }

    public boolean signal(int key, E e) {
        boolean result = false;

        BlockNode<E> node = get(key);
        if (node != null) {
            try {
                remove(key);
                node.put(e);
                result = true;
            } catch (Exception exce) {
                Log.e(exce);
            }
        }

        return result;
    }

    public boolean contain(int key) {
        return get(key) != null;
    }
}

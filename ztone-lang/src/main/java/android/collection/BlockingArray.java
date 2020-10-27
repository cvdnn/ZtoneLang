package android.collection;

import android.log.Log;
import android.reflect.Clazz;
import android.util.SparseArray;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public final class BlockingArray<E> extends SparseArray<BlockNode<E>> {
    private final ReentrantLock mLocking = new ReentrantLock();

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

        lockInterruptibly();
        try {
            put(key, node);
        } finally {
            unlock();
        }

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
        BlockNode<E> node = null;

        lockInterruptibly();
        try {
            node = get(key);
            if (node == null) {
                node = make(key);
            }
        } finally {
            unlock();
        }

        return node;
    }

    public boolean signal(int key, E e) {
        boolean result = false;

        lockInterruptibly();
        try {
            BlockNode<E> node = removeOff(key);
            if (node != null) {
                try {
                    node.put(e);
                    result = true;
                } catch (Exception exc) {
                    Log.e(exc);
                }
            }
        } finally {
            unlock();
        }

        return result;
    }

    public boolean signal(int key, E e, long timeout, TimeUnit unit) {
        boolean result = false;

        lockInterruptibly();
        try {
            BlockNode<E> node = removeOff(key);
            if (node != null) {
                try {
                    result = node.offer(e, timeout, unit);
                } catch (Exception exc) {
                    Log.e(exc);
                }
            }
        } finally {
            unlock();
        }

        return result;
    }

    public boolean trySignal(int key, E e) {
        boolean result = false;

        lockInterruptibly();
        try {
            BlockNode<E> node = removeOff(key);
            if (node != null) {
                try {
                    result = node.offer(e);
                } catch (Exception exc) {
                    Log.e(exc);
                }
            }
        } finally {
            unlock();
        }

        return result;
    }

    @Override
    public BlockNode<E> get(int key, BlockNode<E> valueIfKeyNotFound) {
        BlockNode<E> node = null;

        lockInterruptibly();
        try {
            node = super.get(key, valueIfKeyNotFound);
        } finally {
            unlock();
        }

        return node;
    }

    public boolean contain(int key) {
        return indexOfKey(key) >= 0 && get(key) != null;
    }

    @Override
    public void delete(int key) {
        lockInterruptibly();
        try {
            super.delete(key);
        } finally {
            unlock();
        }
    }

    @Override
    public void removeAt(int index) {
        lockInterruptibly();
        try {
            super.removeAt(index);
        } finally {
            unlock();
        }
    }

    @Override
    public void removeAtRange(int index, int size) {
        lockInterruptibly();
        try {
            super.removeAtRange(index, size);
        } finally {
            unlock();
        }
    }

    @Override
    public void put(int key, BlockNode<E> value) {
        lockInterruptibly();
        try {
            super.put(key, value);
        } finally {
            unlock();
        }
    }

    @Override
    public int size() {
        int size = 0;

        lockInterruptibly();
        try {
            size = super.size();
        } finally {
            unlock();
        }

        return size;
    }

    @Override
    public int keyAt(int index) {
        int at = 0;

        lockInterruptibly();
        try {
            at = super.keyAt(index);
        } finally {
            unlock();
        }

        return at;
    }

    @Override
    public BlockNode<E> valueAt(int index) {
        BlockNode<E> node = null;

        lockInterruptibly();
        try {
            node = super.valueAt(index);
        } finally {
            unlock();
        }

        return node;
    }

    @Override
    public void setValueAt(int index, BlockNode<E> value) {
        lockInterruptibly();
        try {
            super.setValueAt(index, value);
        } finally {
            unlock();
        }
    }

    @Override
    public int indexOfKey(int key) {
        int index = -1;

        lockInterruptibly();
        try {
            index = super.indexOfKey(key);
        } finally {
            unlock();
        }

        return index;
    }

    @Override
    public int indexOfValue(BlockNode<E> value) {
        int index = -1;

        lockInterruptibly();
        try {
            index = super.indexOfValue(value);
        } finally {
            unlock();
        }

        return index;
    }

    @Override
    public void clear() {
        lockInterruptibly();
        try {
            super.clear();
        } finally {
            unlock();
        }
    }

    @Override
    public void append(int key, BlockNode<E> value) {
        lockInterruptibly();
        try {
            super.append(key, value);
        } finally {
            unlock();
        }
    }

    /**
     * Alias for {@link #deleteOff(int)}.
     *
     * @param key
     *
     * @return
     */
    public BlockNode<E> removeOff(int key) {

        return deleteOff(key);
    }

    public BlockNode<E> deleteOff(int key) {
        BlockNode<E> node = null;

        lockInterruptibly();
        try {
            node = Clazz.invoke(SparseArray.class, this, "removeReturnOld", key);
        } finally {
            unlock();
        }

        return node;
    }

    public void gcc() {
        lockInterruptibly();
        try {
            Clazz.invoke(SparseArray.class, this, "gc");
        } finally {
            unlock();
        }
    }

    private void lockInterruptibly() {
        try {
            mLocking.lockInterruptibly();
        } catch (Exception e) {
            // do nothing
        }
    }

    private void unlock() {
        try {
            mLocking.unlock();
        } catch (Exception e) {
            // do nothing
        }
    }
}

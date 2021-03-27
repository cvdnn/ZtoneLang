package android.collection;

import android.task.DelayRunnable;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class DelayTaskQueue<E extends DelayRunnable> extends DelayQueue<E> {

    @Override
    public E poll(long timeout, TimeUnit unit) {
        E e = null;

        try {
            e = super.poll(timeout, unit);
        } catch (Throwable t) {
        }

        return e;
    }
}

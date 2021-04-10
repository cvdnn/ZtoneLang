package android.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class DelayRunnable implements Runnable, Delayed {

    public long start;
    public long millis;

    public DelayRunnable(long m) {
        start = System.currentTimeMillis();

        millis = m;
    }

    @Override
    public int compareTo(Delayed o) {

        return Long.compare(getDelay(MILLISECONDS), o.getDelay(MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(start + millis - System.currentTimeMillis(), MILLISECONDS);
    }
}

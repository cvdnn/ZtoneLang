package android.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class DelayRunnable implements Runnable, Delayed {

    public long millis;

    public DelayRunnable(long m) {
        millis = m;
    }

    @Override
    public int compareTo(Delayed o) {

        return Long.compare(getDelay(MILLISECONDS), o.getDelay(MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(millis, MILLISECONDS);
    }
}

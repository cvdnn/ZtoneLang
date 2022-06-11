package android.assist;

import android.log.Log;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Nils<T> {
    private static final String TAG = "Nils";

    private T obj;

    private Nils(T v) {
        obj = v;
    }

    public static <T> Nils<T> of(T v) {
        return new Nils<>(v);
    }

    public static <T> Nils<T> empty() {
        return new Nils<>(null);
    }

    public <R> R valueOf(Function<T, R> p) {
        return valueOf(p, null);
    }

    public <R> R valueOf(Function<T, R> p, Supplier<R> n) {
        R result = null;

        if (obj != null && p != null) {
            try {
                result = p.apply(obj);
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        }

        if (result == null && n != null) {
            try {
                result = n.get();
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    public void invokeOf(Consumer<T> p) {

        invokeOf(p, null);
    }

    public void invokeOf(Consumer<T> p, Runnable run) {
        if (obj != null && p != null) {
            try {
                p.accept(obj);
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        } else if (run != null) {
            try {
                run.run();
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        }
    }

    public <R> R nullOf(Supplier<R> n) {
        R result = null;

        if (obj == null && n != null) {
            try {
                result = n.get();
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    public void nullBy(Runnable run) {
        if (obj == null && run != null) {
            try {
                run.run();
            } catch (Throwable e) {
                Log.e(TAG, e);
            }
        }
    }
}

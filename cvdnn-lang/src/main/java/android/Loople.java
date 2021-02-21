package android;

import android.assist.Assert;
import android.exception.OnMainThreadException;
import android.log.Log;
import android.os.Handler;
import android.os.Looper;
import android.task.TaskPoolExecutor;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static android.Const.NIL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Loople {
    public static final String TAG = "Loople";

    public static final MainHandle Main = new MainHandle(Looper.getMainLooper());
    public static final TaskHandle Task = new TaskHandle();

    public static class MainHandle extends Handler {

        public MainHandle(Looper looper) {
            super(looper);
        }

        public void asserts() {
            if (as()) {
                throw new OnMainThreadException("ERROR: Cannot execute in main thread!");
            }
        }

        public boolean as() {
            return Thread.currentThread() == Looper.getMainLooper().getThread();
        }

        /**
         * 验证是否需要在主线程中执行
         *
         * @param o
         * @param methodName
         * @param parameterTypes
         * @param <O>
         *
         * @return
         */
        public <O> boolean demand(@NonNull O o, @NonNull String methodName, Class<?>... parameterTypes) {

            return demand(o, null, methodName, parameterTypes);
        }

        /**
         * 验证是否需要在主线程中执行
         *
         * @param o
         * @param methodName
         * @param parameterTypes
         * @param <O>
         *
         * @return
         */
        public <O> boolean demand(@NonNull O o, Class<?> clazz, @NonNull String methodName, Class<?>... parameterTypes) {
            boolean result = false;

            if (o != null) {
                try {
                    Class<?> cls = clazz == null ? o.getClass() : clazz;
                    Method method = cls.getMethod(methodName, parameterTypes);
                    if (method != null) {
                        MainThread ann = method.getAnnotation(MainThread.class);
                        if (ann != null) {
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }

            return result;
        }

        public final Runnable cancel(Runnable... runs) {
            if (runs != null) {
                try {
                    for (Runnable r : runs) {
                        if (r != null) {
                            removeCallbacks(r);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }

            return null;
        }
    }

    public static class TaskHandle {
        public final TaskPoolExecutor Pool = new TaskPoolExecutor(TAG, 32);

        public CompletableFuture allOf(Collection<Runnable> runnables) {

            return allOf(Pool, runnables);
        }

        public CompletableFuture allOf(ExecutorService es, Collection<Runnable> runnables) {
            CompletableFuture future = null;

            if (Assert.notEmpty(runnables) && es != null && !es.isShutdown()) {
                try {
                    CompletableFuture[] arrays = runnables.stream().map(r -> CompletableFuture.runAsync(r, es)).toArray(CompletableFuture[]::new);
                    if (Assert.check(arrays)) {
                        future = CompletableFuture.allOf(arrays);
                    }
                } catch (Throwable t) {
                    Log.e(TAG, t);
                }
            }

            return future != null ? future : CompletableFuture.completedFuture(NIL);
        }

        public <E> CompletableFuture allOf(Collection<E> items, @NonNull Consumer<E> action) {

            return allOf(Pool, items, action);
        }

        public <E> CompletableFuture allOf(ExecutorService es, Collection<E> items, @NonNull Consumer<E> action) {
            CompletableFuture future = null;

            if (Assert.notEmpty(items) && action != null) {
                try {
                    future = allOf(es, Arrays.asList(items.stream().map(item -> (Runnable) () -> {
                        try {
                            action.accept(item);
                        } catch (Throwable t) {
                            Log.e(TAG, t);
                        }
                    }).toArray(Runnable[]::new)));
                } catch (Throwable t) {
                    Log.e(TAG, t);
                }
            }

            return future != null ? future : CompletableFuture.completedFuture(NIL);
        }

        public <E> Future<?> schedule(Collection<E> items, Consumer<? super E> action) {
            Future<?> future = null;

            if (Assert.notEmpty(items) && action != null) {
                future = Pool.submit(() -> items.forEach(action));
            } else {
                future = CompletableFuture.completedFuture(NIL);
            }

            return future;
        }

        public final Future<?> schedule(Runnable r) {
            return Pool.submit(r);
        }

        public final <V> Future<V> schedule(Callable<V> c) {
            return Pool.submit(c);
        }

        public final Future<?> schedule(Runnable r, long delayMillis) {
            return Pool.schedule(r, delayMillis, MILLISECONDS);
        }

        public final <V> Future<V> schedule(Callable<V> c, long delayMillis) {
            return Pool.schedule(c, delayMillis, MILLISECONDS);
        }

        public final Future<?> schedule(Runnable r, long delay, TimeUnit unit) {
            return Pool.schedule(r, delay, unit);
        }

        public final <V> Future<V> schedule(Callable<V> c, long delay, TimeUnit unit) {
            return Pool.schedule(c, delay, unit);
        }

        /**
         * 固定时间切片执行任务
         *
         * @param r
         * @param initialDelay
         * @param period
         * @param unit
         */
        public final void slice(Runnable r, long initialDelay, long period, TimeUnit unit) {
            Pool.scheduleAtFixedRate(r, initialDelay, period, unit);
        }

        /**
         * 固定等待执行时间
         *
         * @param r
         * @param initialDelay
         * @param period
         * @param unit
         */
        public final void chain(Runnable r, long initialDelay, long period, TimeUnit unit) {
            Pool.scheduleWithFixedDelay(r, initialDelay, period, unit);
        }

        public final <R> R join(Future<R> f) {
            return get(f);
        }

        public final <R> R get(Future<R> f) {
            R r = null;

            if (f != null) {
                try {
                    r = f.get();
                } catch (Exception e) {
                    // do nothing
                }
            }

            return r;
        }

        public final <R> R join(Future<R> f, long timeout, TimeUnit unit) {
            return get(f, timeout, unit);
        }

        public final <R> R get(Future<R> f, long timeout, TimeUnit unit) {
            R r = null;

            if (f != null) {
                try {
                    r = f.get(timeout, unit);
                } catch (Exception e) {
                    // do nothing
                }
            }

            return r;
        }

        public final <O> Future<O> cancel(Future<O>... futures) {
            if (futures != null) {
                try {
                    for (Future<?> f : futures) {
                        if (f != null) {
                            f.cancel(true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }

            return null;
        }

        public final void shutdown() {
            try {
                Pool.shutdownNow();
            } catch (Exception e) {
                // do nothing
            }
        }

        private TaskHandle() {
        }
    }

    public static void logPoolNum() {
        int count = Task.Pool.ThreadCount.get();
        int coreSize = Task.Pool.getCorePoolSize();
        int threadNum = Task.Pool.getThreadFactory().ThreadNum.get();

        String msg = String.format("TaskPool: %d / %d", count, threadNum);
        if (count == coreSize) {
//            CrashReport.postCatchedException(new RuntimeException("TaskPool: " + count));
            Log.e(TAG, msg);
        } else {
            Log.d(TAG, msg);
        }
    }
}

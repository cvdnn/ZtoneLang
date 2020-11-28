package android.reflect;

import android.assist.Assert;
import android.log.monitoring.MonitorUtils;
import android.log.monitoring.PerformanceMonitoring.MonitorRunnable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

public final class TrackingUtils {

    public static Runnable proxy(Runnable r) {

        return proxyTaskable(r);
    }

    public static <V> Callable<V> proxy(Callable<V> c) {

        return proxyTaskable(c);
    }

    /**
     * 代理
     *
     * @param l
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <L> L proxyTaskable(final L l) {
        L listener = l;
        if (l != null && MonitorUtils.isMonitorThread()) {
            Class<?> clazz = l.getClass();
            Class<?>[] lInterface = clazz.getInterfaces();
            if (Assert.notEmpty(lInterface)) {
                listener = (L) Proxy.newProxyInstance(clazz.getClassLoader(), lInterface, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = null;

                        final MonitorRunnable monitorRunnable = MonitorUtils.startMonitorRunnable(Thread.currentThread());

                        result = method.invoke(l, args);

                        monitorRunnable.stop();

                        return result;
                    }
                });
            }
        }

        return listener;
    }
}

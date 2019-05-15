/*
 * ThreadPoolManager.java
 * 
 * Copyright 2011 androwa team, Inc. All rights reserved.
 * androwa PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.concurrent;

import android.assist.Assert;
import android.reflect.TrackingUtils;
import android.text.TextUtils;
import android.util.ArrayMap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理器
 *
 * @author ANDROWA TEAM
 * @version 1.0.0
 * @since 1.0.0 Handy 2011-04-09
 */
public class ThreadPool {

    public static final String M_POOL_NAME = "__DEFAULT_POOL";

    /**
     * 默认线程池缓存区大小
     */
    private static final int DEFAULT_BLOCKING_QUEUE_SIZE = 24;

    /**
     * 核心线程数
     */
    private final static int CORE_POOL_SIZE = 12;

    /**
     * 最大线程数
     */
    private final static int MAXIMUM_POOL_SIZE = 32;

    /**
     * 线程空闲时间
     */
    private final static long KEEP_ALIVE_TIME = 500;

    /**
     * 时间单位
     */
    private final static TimeUnit UNIT = TimeUnit.MILLISECONDS;

    /**
     * 线程池管理器
     */
    public static ThreadPool Impl = new ThreadPool();


	/* ***************************************
     *
	 * ***************************************
	 */

    /**
     * 默认线程池
     */
    private ThreadPoolExecutor mDefaultThreadExecutor;

    /**
     * 最大能注册64个线程池
     */
    private final ArrayMap<String, ExecutorService> mThreadPoolMap;

    /**
     * 构造方法
     */
    private ThreadPool() {
        // 最大能注册16个线程池
        mThreadPoolMap = new ArrayMap<String, ExecutorService>(16);
        mDefaultThreadExecutor = createThreadPoolExecutor(M_POOL_NAME);
    }

    /**
     * 获取默认的线程池
     *
     * @return
     */
    public ExecutorService obtain() {
        if (mDefaultThreadExecutor == null || mDefaultThreadExecutor.isShutdown()) {
            synchronized (ThreadPool.class) {
                if (mDefaultThreadExecutor == null || mDefaultThreadExecutor.isShutdown()) {
                    mDefaultThreadExecutor = createThreadPoolExecutor(M_POOL_NAME);
                }
            }
        }
        return mDefaultThreadExecutor;
    }

    /**
     * 按照默认的参数注册线程池
     *
     * @param poolName
     */
    public synchronized ExecutorService create(String poolName) {
        Assert.notNull(poolName);

        ExecutorService excuter = createThreadPoolExecutor(poolName);
        put(poolName, excuter);

        return excuter;
    }

    /**
     * 注册线程池
     *
     * @param poolName
     * @param executor
     */
    public synchronized void put(String poolName, ExecutorService executor) {
        if (!TextUtils.isEmpty(poolName) && executor != null) {
            if (mThreadPoolMap != null) {
                ExecutorService oldEcecutor = mThreadPoolMap.put(poolName, executor);
                if (oldEcecutor != null) {
                    oldEcecutor.shutdown();
                }
            } else {
                throw new RuntimeException("Prohibition of registration of ThreadPoolExecutors at run-time!");
            }
        } else {
            throw new IllegalArgumentException("registed ThreadPoolExecutor is null.");
        }
    }

    /**
     * 移除线程池
     *
     * @param poolName
     */
    public synchronized void remove(String poolName) {
        if (Assert.notEmpty(poolName) && Assert.containsKey(mThreadPoolMap, poolName)) {
            ExecutorService executor = mThreadPoolMap.remove(poolName);
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    /**
     * 获取线程池
     *
     * @param poolName
     * @return
     */
    public synchronized ExecutorService get(String poolName) {
        Assert.notNull(poolName);

        ExecutorService executor = null;
        if (Assert.containsKey(mThreadPoolMap, poolName)) {
            executor = mThreadPoolMap.get(poolName);
        }

        if (executor == null) {
            executor = create(poolName);
        }

        return executor;
    }

    /**
     * 在制定线程池中执行
     *
     * @param runnable
     */
    public synchronized void execute(String poolName, Runnable runnable) {
        Assert.notNull(runnable);

        get(poolName).execute(TrackingUtils.proxy(runnable));
    }

    /**
     * 在默认线程池中执行
     *
     * @param runnable
     */
    public synchronized void execute(Runnable runnable) {
        Assert.notNull(runnable);

        obtain().execute(TrackingUtils.proxy(runnable));
    }

    public void release() {
        if (mDefaultThreadExecutor != null) {
            mDefaultThreadExecutor.shutdownNow();
            mDefaultThreadExecutor = null;
        }
    }

    /**
     * 在默认线程池中执行
     *
     * @param callable
     * @return
     */
    public synchronized <V> Future<V> submit(Callable<V> callable) {
        Assert.notNull(callable);

        return obtain().submit(TrackingUtils.proxy(callable));
    }

    private static ThreadPoolExecutor createThreadPoolExecutor(String poolName) {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(DEFAULT_BLOCKING_QUEUE_SIZE);
//		CallerRunsPolicy policy = new CallerRunsPolicy();
        ThreadPoolExecutor.DiscardPolicy policy = new ThreadPoolExecutor.DiscardPolicy();
        ThreadPoolExecutor excuter = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, UNIT,
                queue, new PoolThreadFactory(poolName), policy);
        excuter.prestartAllCoreThreads();
        excuter.allowCoreThreadTimeOut(true);

        return excuter;
    }

    /**
     * The default thread factory
     */
    public static class PoolThreadFactory implements ThreadFactory {
        private static final AtomicInteger mPoolNumber = new AtomicInteger(1);
        private final ThreadGroup mThreadGroup;
        private final AtomicInteger mThreadNumber = new AtomicInteger(1);
        private final String mNamePrefix;

        public PoolThreadFactory(String poolName) {
            SecurityManager s = System.getSecurityManager();
            mThreadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            mNamePrefix = String.format("%s_%d_", (Assert.notEmpty(poolName) ? poolName : M_POOL_NAME),
                    mPoolNumber.getAndIncrement());
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(mThreadGroup, r, mNamePrefix + mThreadNumber.getAndIncrement(), 0);

            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;
        }
    }
}

/*
 * PerformanceMonitoring.java
 * 
 * Copyright 2011 sillar team, Inc. All rights reserved.
 * 
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.log.monitoring;

import android.assist.Assert;
import android.concurrent.HandlerThread;
import android.log.Log;
import android.os.Looper;
import android.os.SystemClock;

import java.lang.Thread.State;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Const.LINE_SEPARATOR;

/**
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2013-11-2
 */
public class PerformanceMonitoring {
    private static final String TAG = "PerformanceMonitoring";

    private static final String PERFORMANCE_MONITORING_NAME = "PERFORMANCE_MONITORING_THREAD";

    private static final int COUNT_MONTIORING = 3;

    private SimpleDateFormat mDataFormat;

    private final HandlerThread mMonitorThreadWrapper;

    protected PerformanceMonitoring(String name) {
        mMonitorThreadWrapper = new HandlerThread(Assert.notEmpty(name) ? name : PERFORMANCE_MONITORING_NAME);

        try {
            mDataFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.getDefault());
        } catch (Throwable t) {
            Log.d(TAG, t);

            mDataFormat = null;
        }
    }

    public final MonitorRunnable start(Thread thread) {

        return new MonitorRunnable(thread).start();
    }

    public class MonitorRunnable implements Runnable {
        private static final int FLAG_NULL = Integer.MIN_VALUE;
        private static final int FLAG_PREPARE = 1;

        private final Thread mThread;

        private int mDumpingCount;

        /**
         * 线程启动日期, 启动线程时间
         */
        private final long mStartDateTime, mThreadStartMillis;

        /**
         * 不用StringBuilder,避免非线程安全问题
         */
        private final StringBuffer mLogStringBuilder;

        private final int M_BLOCK_THRESHOLD;

        protected MonitorRunnable(Thread thread) {
            if (thread == null) {
                new InstantiationError("The thread by monitored is null!");
            }

            M_BLOCK_THRESHOLD = (int) (MonitorUtils.getBlockThreshold() * 0.8f);

            mThread = thread;

            mStartDateTime = System.currentTimeMillis();
            mThreadStartMillis = SystemClock.currentThreadTimeMillis();

            mLogStringBuilder = new StringBuffer();
            mLogStringBuilder.append("TI: ★[").append(mThread.getName()).append("]: ");
            mLogStringBuilder.append("TI: millis: %1$dms(%2$dms), state: %3$s");

            if (mDataFormat != null) {
                mLogStringBuilder.append(LINE_SEPARATOR);
                mLogStringBuilder.append("TI:  [start]: %4$s, [stop]: %5$s");
            }
        }

        /**
         * 設置延遲M_BLOCK_THRESHOLD時間啓動線程監控
         */
        protected MonitorRunnable start() {
            if (mDumpingCount < FLAG_PREPARE) {
                mDumpingCount = FLAG_PREPARE;
                mMonitorThreadWrapper.postDelayed(this, M_BLOCK_THRESHOLD);
            }

            return this;
        }

        public void stop() {
            stop(State.TERMINATED);
        }

        protected void stop(final State state) {
            if (mDumpingCount != FLAG_NULL) {
                final long endDateTime = System.currentTimeMillis();
                final long runningMillis = SystemClock.currentThreadTimeMillis() - mThreadStartMillis;
                final long systemMillis = endDateTime - mStartDateTime;

                mDumpingCount = FLAG_NULL;
                mMonitorThreadWrapper.removeCallbacks(this);

                mLogStringBuilder.append(LINE_SEPARATOR).append("TI: ");

                // FIXME: 统一Printer输出
                if (systemMillis >= MonitorUtils.getBlockThreshold()) {
                    int level = mThread == Looper.getMainLooper().getThread() ? android.util.Log.DEBUG
                            : android.util.Log.VERBOSE;
                    if (mDataFormat != null) {
                        Log.println(level, TAG, mLogStringBuilder.toString(), systemMillis, runningMillis, state.name(),
                                mDataFormat.format(new Date(mStartDateTime)), mDataFormat.format(new Date(endDateTime)));
                    } else {
                        Log.println(level, TAG, mLogStringBuilder.toString(), systemMillis, runningMillis, state.name());
                    }
                }
            }
        }

        /**
         * 线程快照
         */
        @Override
        public void run() {
            final State state = mThread.getState();

            if (mDumpingCount <= COUNT_MONTIORING) {
                mLogStringBuilder.append(LINE_SEPARATOR);
                mLogStringBuilder.append("TI: ").append(mDumpingCount).append("# state: ").append(state.name());
                mLogStringBuilder.append(", CPU: 0%%");
                mLogStringBuilder.append(LINE_SEPARATOR);

                StackTraceElement[] stackTraceList = mThread.getStackTrace();
                if (Assert.notEmpty(stackTraceList)) {
                    for (StackTraceElement element : stackTraceList) {
                        if (element != null) {
                            mLogStringBuilder.append("TI:   ").append(element.toString());
                            mLogStringBuilder.append(LINE_SEPARATOR);
                        }
                    }

                    mLogStringBuilder.append(LINE_SEPARATOR);
                }

                if (state == State.RUNNABLE) {
                    if (mDumpingCount >= FLAG_PREPARE) {
                        ++mDumpingCount;

                        mMonitorThreadWrapper.postDelayed(this, M_BLOCK_THRESHOLD);
                    }
                } else {
                    stop(state);
                }
            } else {
                stop(State.BLOCKED);
            }
        }
    }
}

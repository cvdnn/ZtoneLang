package android.log.monitoring;

import android.assist.Assert;
import android.log.monitoring.PerformanceMonitoring.MonitorRunnable;
import android.os.Looper;

public final class MonitorUtils {
    private static String mProcessName;

    /**
     * 持续时长(秒)，开启后到达时长则关闭
     */
    public static final int DURATION = 10;

    /**
     * 间隔(毫秒)，超出该间隔判定为卡慢。建议根据机器性能设置不同的数值，如2000/Cpu核数
     */
    private static final int BLOCK_THRESHOLD = 800;
    private static int sBlockThreshold = BLOCK_THRESHOLD;

    private static boolean mIsMonitorThread;

    private static PerformanceMonitoring mPerformanceMonitoring;

    public static void onInit() {

    }

    public static void setProcessName(String processName) {
        mProcessName = processName;
    }

    public static void startMonitorMainThread() {
        Looper mainLooper = Looper.getMainLooper();
        mainLooper.setMessageLogging(new MonitorLogPrinter(mainLooper.getThread()));
    }

    public static void setBlockThreshold(int blockThreshold) {
        sBlockThreshold = blockThreshold;
    }

    public static int getBlockThreshold() {
        return sBlockThreshold;
    }

    public static void stopMonitorMainThread() {
        Looper.getMainLooper().setMessageLogging(null);
    }

    public static void startMonitorUserThread() {
        mIsMonitorThread = true;
    }

    public static void stopMoniterUserThread() {
        mIsMonitorThread = false;
    }

    public static boolean isMonitorThread() {

        return mIsMonitorThread;
    }

    public static MonitorRunnable startMonitorRunnable(Thread thread) {
        if (mPerformanceMonitoring == null) {
            synchronized (MonitorUtils.class) {
                if (mPerformanceMonitoring == null) {
                    if (Assert.notEmpty(mProcessName)) {
                        int mindex = mProcessName.indexOf(':');
                        if (mindex >= 0) {
                            mProcessName = mProcessName.substring(mindex + 1);
                        }
                    }

                    mPerformanceMonitoring = new PerformanceMonitoring(String.format(
                            "%s:PERFORMANCE_MONITORING_THREAD",
                            Assert.notEmpty(mProcessName) ? mProcessName.toUpperCase() : ""));
                }
            }
        }

        return mPerformanceMonitoring.start(thread);
    }

    private MonitorUtils() {
        new InstantiationError();
    }
}

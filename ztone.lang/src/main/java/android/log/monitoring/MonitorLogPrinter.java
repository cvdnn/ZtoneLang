package android.log.monitoring;

import android.util.Printer;

public class MonitorLogPrinter extends PerformanceMonitoring implements Printer {

	protected boolean mIsStartInvoked;

	private MonitorRunnable mMonitorRunable;

	private final Thread mMonitorThread;

	public MonitorLogPrinter(Thread thread) {
		super(String.format("%1$s_PERFORMANCE_MONITORING_THREAD", //
				thread == null ? "RUNNABLE" : thread.getName().toUpperCase()));

		mMonitorThread = thread;

		if (mMonitorThread == null) {
			new InstantiationError("The thread by monitored is null!");
		}
	}

	@Override
	public void println(String x) {
		if (mMonitorRunable != null) {
			mMonitorRunable.stop();
		}

		if (!mIsStartInvoked) {
			mMonitorRunable = start(mMonitorThread);
		}

		mIsStartInvoked = !mIsStartInvoked;
	}
}

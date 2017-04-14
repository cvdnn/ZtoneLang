package android.concurrent;

import android.log.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SequentialThread<E> extends Thread {
	private static final String TAG = "SequentialThread";

	private final ReentrantLock mLock;

	/** Condition for waiting takes */
	private final Condition mLockCondition;

	private ArrayList<E> mElementList;

	public SequentialThread(String threadName, ReentrantLock lock) {
		super(threadName);

		mLock = lock != null ? lock : new ReentrantLock(false);
		mLockCondition = mLock.newCondition();

		mElementList = new ArrayList<E>();
	}

	/**
	 * 线程执行
	 */
	public abstract boolean handle();

	/**
	 * 判断条件
	 * 
	 * @return
	 */
	public boolean onPrepare() {

		return false;
	}

	public SequentialThread startHandle() {
		start();

		return this;
	}

	public void notifyToHandle() {
		if (mLockCondition != null) {
			mLockCondition.signalAll();
		}

		if (mLock != null && mLockCondition != null) {
			try {
				mLock.lock();
				mLockCondition.signalAll();
			} catch (Exception e) {
				Log.e(TAG, e);
			} finally {
				mLock.unlock();
			}
		}
	}

	@Override
	public void run() {
		for (;;) {
			if (onPrepare()) {
				handle();
			} else {
				if (mLock != null) {
					try {
						mLock.lock();

						if (mLockCondition != null) {
							mLockCondition.await();
						}
					} catch (Exception e) {
						Log.e(TAG, e);
					} finally {
						mLock.unlock();
					}
				}
			}
		}
	}
}
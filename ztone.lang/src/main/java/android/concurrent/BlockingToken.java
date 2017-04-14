package android.concurrent;

import android.log.Log;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 阻塞令牌
 * 
 * @author handy
 * 
 */
public class BlockingToken {
	private static final byte[] LOCK = new byte[0];
	private volatile ArrayBlockingQueue<byte[]> mLockQueue;

	public BlockingToken() {
		this(1);
	}

	public BlockingToken(int capacity) {
		mLockQueue = new ArrayBlockingQueue<byte[]>(capacity);
	}

	/**
	 * 借出令牌，使得执行到apply()阻塞。
	 * 
	 */
	public void lend() {
		if (mLockQueue != null) {
			try {
				mLockQueue.clear();// 清除令牌
			} catch (Exception e) {
				Log.e(e);
			}
		}
	}

	/**
	 * 归还令牌，以便让apply()获得令牌而执行下去
	 * 
	 */
	public void restore() {
		if (mLockQueue != null) {
			try {
				if (mLockQueue.isEmpty()) {
					mLockQueue.put(LOCK);// 添加令牌
				}
			} catch (Exception e) {
				Log.e(e);
			}
		}
	}

	/**
	 * 申请令牌，在令牌还未归restore()还之前阻塞等待
	 */
	public void apply() {
		if (mLockQueue != null) {
			try {
				mLockQueue.take();// 查询令牌,当队列为空阻塞等待

				// 添加令牌, 以保证重复调用不会被阻塞。
				if (mLockQueue.isEmpty()) {
					mLockQueue.put(LOCK);
				}
			} catch (Exception e) {
				Log.e(e);
			}
		}
	}
}

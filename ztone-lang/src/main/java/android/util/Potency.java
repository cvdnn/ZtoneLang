package android.util;

import android.log.Log;

/**
 * Created by handy on 17-3-27.
 */

public class Potency {
    public static final String TAG = "Debug";
    private long mStarTime;

    private Potency() {
        mStarTime = System.currentTimeMillis();
    }

    public long record() {
        long lastTime = mStarTime;

        mStarTime = System.currentTimeMillis();

        return mStarTime - lastTime;
    }

    public void record(String tag) {
        long time = System.currentTimeMillis() - mStarTime;

        Log.i(TAG, "DD: [%s]: %dms", tag, time);

        mStarTime = System.currentTimeMillis();
    }

    public static Potency start() {

        return new Potency();
    }
}

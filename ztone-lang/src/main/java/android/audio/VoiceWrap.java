package android.audio;

import android.assist.Assert;
import android.log.Log;
import android.media.MediaPlayer;

import androidx.annotation.RawRes;

import static android.Args.context;

public class VoiceWrap {
    private static final String TAG = "VoiceWrap";

    public MediaPlayer Player = null;

    private int mRawId;
    private boolean mIsLoop;

    public VoiceWrap(int rawId) {
        this(rawId, false);
    }

    public VoiceWrap(int rawId, boolean loop) {
        mRawId = rawId;
        mIsLoop = loop;

        reset();
    }

    public VoiceWrap reset() {
        release();

        try {
            Player = MediaPlayer.create(context, mRawId);
            Player.setLooping(mIsLoop);
            if (!mIsLoop) {
                Player.setOnCompletionListener(mp -> release());
            }
        } catch (Throwable t) {
            Log.e(t);
        }

        return this;
    }

    public VoiceWrap play() {

        return reset().start();
    }

    public VoiceWrap start() {
        if (Player != null && !Player.isPlaying()) {
            Player.start();
        }

        return this;
    }

    public VoiceWrap pause() {
        if (Player != null) {
            Player.pause();
        }

        return this;
    }

    /**
     * 重置mediaplayer
     */
    public VoiceWrap release() {
        if (Player != null) {
            try {
                Player.stop();
                Player.reset();
                Player.release();
            } catch (Exception e) {
                Log.e(TAG, e);
            }
            Player = null;
        }

        return this;
    }

    public static VoiceWrap play(VoiceWrap vw, VoiceWrap lv) {
        if (lv != null) {
            lv.release();
        }

        if (vw != null) {
            vw.play();
        }

        return vw;
    }

    public static void play(@RawRes int rawId) {
        MediaPlayer player = MediaPlayer.create(context, rawId);
        player.setOnPreparedListener(mp -> mp.start());
        player.setOnCompletionListener(mp -> mp.release());
        try {
            player.start();
        } catch (Exception e) {
            Log.e(TAG, e);

            player.release();
            player = null;
            System.gc();
        }
    }

    public static void pause(VoiceWrap... wraps) {
        if (Assert.notEmpty(wraps)) {
            for (VoiceWrap vw : wraps) {
                vw.pause();
            }
        }
    }

    public static void release(VoiceWrap... wraps) {
        if (Assert.notEmpty(wraps)) {
            for (VoiceWrap vw : wraps) {
                vw.release();
            }
        }
    }
}

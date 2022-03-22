package android.audio;

import android.Loople;
import android.assist.Assert;
import android.log.Log;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.RawRes;

import static android.Args.context;

public class VoiceWrap {
    private static final String TAG = "VoiceWrap";

    public MediaPlayer Player = null;

    private int mRawId;
    private Uri mUri;
    private boolean mIsLoop;

    public VoiceWrap(int rawId) {
        this(rawId, false);
    }

    public VoiceWrap(int rawId, boolean loop) {
        mRawId = rawId;
        mIsLoop = loop;

        reset();
    }

    public VoiceWrap(Uri uri) {
        this(uri, false);
    }

    public VoiceWrap(Uri uri, boolean loop) {
        mUri = uri;
        mIsLoop = loop;

        reset();
    }


    public VoiceWrap reset() {
        release();

        try {
            Player = mRawId != 0 ? MediaPlayer.create(context, mRawId) : MediaPlayer.create(context, mUri);
            Player.setLooping(mIsLoop);
            if (!mIsLoop) {
                Player.setOnCompletionListener(mp -> release());
            }
        } catch (Throwable t) {
            Log.v(t);
        }

        return this;
    }

    public VoiceWrap play() {

        return reset().start();
    }

    public void async() {
        Loople.Task.schedule(() -> reset().start());
    }

    public VoiceWrap start() {
        if (Player != null && !Player.isPlaying()) {
            try {
//                Player.prepare();
                Player.start();
            } catch (Throwable t) {
            }
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

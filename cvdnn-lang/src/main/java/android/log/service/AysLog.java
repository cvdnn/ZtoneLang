package android.log.service;

import android.assist.Assert;
import android.log.Pairing;
import android.content.Context;
import android.frame.AbstractProvider;
import android.reflect.Clazz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

public class AysLog extends AbstractProvider {
    private static final String TAG = "AysLog";

    /** Priority constant for the println method; use Log.v. */
    public static final String VERBOSE = "VERBOSE";

    /** Priority constant for the println method; use Log.d. */
    public static final String DEBUG = "DEBUG";

    /** Priority constant for the println method; use Log.i. */
    public static final String INFO = "INFO";

    /** Priority constant for the println method; use Log.w. */
    public static final String WARN = "WARN";

    /** Priority constant for the println method; use Log.e. */
    public static final String ERROR = "ERROR";

    /** Priority constant for the println method. */
    public static final String ASSERT = "ASSERT";

    public interface Client {
        void onSetup(@NonNull Context context);

        void async(@Nullable String topic, @Nullable String tag, Pairing pair);

        void async(@Nullable String topic, @Nullable String tag, String text, Object... args);
    }

    private static Client mLogClient;

    public static void v(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(DEBUG, tag, pair);
        }

        android.log.Log.v(tag, msg(pair));
    }

    public static void v(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(VERBOSE, tag, text, args);
        }

        android.log.Log.v(tag, text, args);
    }

    public static void d(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(DEBUG, tag, pair);
        }

        android.log.Log.d(tag, msg(pair));
    }

    public static void d(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(DEBUG, tag, text, args);
        }

        android.log.Log.d(tag, text, args);
    }

    public static void i(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(INFO, tag, pair);
        }

        android.log.Log.i(tag, msg(pair));
    }

    public static void i(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(INFO, tag, text, args);
        }

        android.log.Log.i(tag, text, args);
    }

    public static void w(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(WARN, tag, pair);
        }

        android.log.Log.w(tag, msg(pair));
    }

    public static void w(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(WARN, tag, text, args);
        }

        android.log.Log.w(tag, text, args);
    }

    public static void e(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(ERROR, tag, pair);
        }

        android.log.Log.e(tag, msg(pair));
    }

    public static void e(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(ERROR, tag, text, args);
        }

        android.log.Log.e(tag, text, args);
    }

    public static void e(@Nullable String tag, Throwable t) {
        e(tag, Pairing.kv().add(t));

        android.log.Log.d(t);
    }

    public static void a(@Nullable String tag, Pairing pair) {
        if (mLogClient != null) {
            mLogClient.async(ASSERT, tag, pair);
        }

        android.log.Log.w(tag, msg(pair));
    }

    public static void a(@Nullable String tag, String text, Object... args) {
        if (mLogClient != null) {
            mLogClient.async(ASSERT, tag, text, args);
        }

        android.log.Log.w(tag, text, args);
    }

    public static String msg(Pairing pair) {
        StringBuilder msg = new StringBuilder();

        if (Assert.notEmpty(pair)) {
            for (Map.Entry<String, Object> entry : pair.entrySet()) {
                separate(msg).append(msg(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }

        return msg.toString();
    }

    public static String msg(String key, String text) {
        return Assert.notEmpty(key) ? "[" + key + "]: " + text : text;
    }

    public static StringBuilder separate(StringBuilder sb) {

        return sb.append(Assert.notEmpty(sb) ? ", " : "");
    }


    // ///////////////////////
    //

    @Override
    public boolean onCreate() {
        Class<?> slsClazz = Clazz.forName("com.aliyun.sls.android.sdk.SLSLog");
        if (slsClazz != null) {
            mLogClient = Clazz.newInstance("android.slog.aliyun.AysClient");
            if (mLogClient != null) {
                mLogClient.onSetup(getContext());
            }
        }

        return true;
    }
}

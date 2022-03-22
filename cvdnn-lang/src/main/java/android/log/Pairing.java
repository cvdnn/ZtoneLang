package android.log;

import android.Android;

import java.util.LinkedHashMap;

public class Pairing extends LinkedHashMap<String, Object> {
    public static final String TAG_ACTION = "__action__";
    public static final String TAG_SN = "__sn__";
    public static final String TAG_MSG = "__msg__";
    public static final String TAG_CAUSE = "__cause__";
    public static final String TAG_STACK = "__stack__";

    private Pairing() {
        add(TAG_SN, Android.Build.cpuSerial());
    }

    public static Pairing kv() {
        return new Pairing();
    }

    public static Pairing kv(String key, Object value) {

        return kv().add(key, value);
    }

    public Pairing action(String event) {
        super.put(TAG_ACTION, event);

        return this;
    }

    public Pairing msg(Object value) {
        super.put(TAG_ACTION, value);

        return this;
    }

    public Pairing add(String key, Object value) {
        super.put(key, value);

        return this;
    }

    public Pairing add(String key, String format, Object... args) {
        super.put(key, String.format(format, args));

        return this;
    }

    public Pairing add(Throwable t) {
        add(TAG_CAUSE, android.log.Log.cause(t));
        add(TAG_STACK, android.log.Log.stackTrace(t, "com.ztone"));

        return this;
    }
}

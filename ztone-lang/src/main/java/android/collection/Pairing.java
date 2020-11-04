package android.collection;

import java.util.HashMap;

public class Pairing extends HashMap<String, Object> {

    public static Pairing kv() {
        return new Pairing();
    }

    public static Pairing kv(String key, Object value) {

        return kv().add(key, value);
    }

    public Pairing action(String event) {
        super.put("__action__", event);

        return this;
    }

    public Pairing msg(Object value) {
        super.put("__msg__", value);

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
        add("throwable_cause", android.log.Log.cause(t));
        add("throwable_stack", android.log.Log.stackTrace(t, "com.ztone"));

        return this;
    }
}

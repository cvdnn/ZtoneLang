package android.log;

import org.json.JSONObject;

public class LogMeta {
    private static final String TAG = "LogMeta";

    public long timeMillis;
    public int priority;
    public String tag;
    public String text;

    public LogMeta() {

    }

    public LogMeta(long t, int p, String tag, String text) {
        this.timeMillis = t;
        this.priority = p;
        this.tag = tag;
        this.text = text;
    }

    public JSONObject format() {
        JSONObject json = new JSONObject();

        try {
            json.put("t", timeMillis);
            json.put("priority", priority);
            json.put("tag", tag);
            json.put("text", text);
        } catch (Exception e) {
            android.util.Log.e(TAG, "LogMessage ERROR!!!");
        }

        return json;
    }
}

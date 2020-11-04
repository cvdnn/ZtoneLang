package android.slog.aliyun;

import com.aliyun.sls.android.sdk.model.Log;

final class LogMap extends Log {

    public LogMap put(String key, String value) {
        PutContent(key, value);

        return this;
    }

    public LogMap put(String key, Object value) {
        GetContent().put(key, value);

        return this;
    }
}

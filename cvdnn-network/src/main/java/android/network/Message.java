package android.network;

import android.json.Json;

public abstract class Message implements Json.Formatter {
    public final Message msg() {
        return this;
    }
}

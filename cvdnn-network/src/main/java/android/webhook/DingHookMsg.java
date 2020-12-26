package android.webhook;

import android.json.Json;
import android.network.Message;

import org.json.JSONObject;

public abstract class DingHookMsg extends Message  {

    public enum MsgType {
        TEXT("text"),
        LINK("link"),
        MARKDOWN("markdown"),
        ACTION_CARD("ActionCard"),
        FEED_CARD("FeedCard");

        String typeName;

        MsgType(String name) {
            typeName = name;
        }
    }

    protected abstract MsgType type();

    protected abstract JSONObject content();

    @Override
    public JSONObject format() {
        return Json.make()
                .put("msgtype", type().typeName)
                .put(type().typeName, content())
                .build();
    }
}

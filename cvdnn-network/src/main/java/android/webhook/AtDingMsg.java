package android.webhook;

import android.assist.Assert;
import android.json.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AtDingMsg extends DingHookMsg {
    /** 被@人的手机号（在content里添加@人的手机号） */
    protected List<String> atMobiles = new ArrayList<>();

    /** 是否@所有人 */
    protected boolean isAtAll = false;

    public AtDingMsg at(String num) {
        if (Assert.notEmpty(num)) {
            atMobiles.add(num);
        }

        return this;
    }

    public final AtDingMsg atAll() {
        isAtAll = true;

        return this;
    }

    protected JSONObject makeAtJSON() {
        JSONObject atJson = new JSONObject();

        if (!isAtAll && Assert.notEmpty(atMobiles)) {
            Json.put(atJson, "atMobiles", new JSONArray(atMobiles));
        }

        Json.put(atJson, "isAtAll", isAtAll);

        return atJson;
    }

    @Override
    public JSONObject format() {
        return Json.put(super.format(), "at", makeAtJSON());
    }
}

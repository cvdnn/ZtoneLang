package android.webhook;

import android.log.Log;
import android.network.NetUtils;
import android.network.Response;

import com.google.gson.Gson;

public class HookResult {
    public static final String TAG = "HookResult";

    public static final int RST_FAIL = -1;
    public static final int RST_SUCCESS = 0;

    public int errcode = RST_FAIL;
    public String errmsg = "";

    public static final boolean success(HookResult result) {
        return result != null && result.errcode == RST_SUCCESS;
    }

    public static final boolean success(Response response) {
        return NetUtils.success(response) && success(HookResult.from(response.content().text()));
    }

    public static HookResult from(String json) {
        Log.d(TAG, json);

        return new Gson().fromJson(json, HookResult.class);
    }
}

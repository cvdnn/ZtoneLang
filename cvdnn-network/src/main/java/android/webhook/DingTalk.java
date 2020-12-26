package android.webhook;

import android.network.NetUtils;

public class DingTalk {

    public static final boolean post(DingRequest request) {

        return request != null && request.check() && HookResult.success(NetUtils.post(request.url, request.message.format()));
    }
}

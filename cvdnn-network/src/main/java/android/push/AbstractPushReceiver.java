package android.push;

import android.Android;
import android.assist.Assert;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.json.Json;
import android.log.Log;
import android.math.Maths;
import android.os.Bundle;

import org.json.JSONObject;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public abstract class AbstractPushReceiver extends BroadcastReceiver {
    private static final String TAG = "░░░░[AbstractPushReceiver]";

    @Override
    public final void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();

        Log.d(TAG, "onReceive: %s", action);

        if (Pushing.JPUSH_ACTION_REGISTRATION_ID.equals(action)) {
            Log.d(TAG, "[RegId]: %s");

            onRegisteredId(bundle.getString(Pushing.JPUSH_EXTRA_REGISTRATION_ID, Android.Build.cpuSerial()));

        } else if (Pushing.JPUSH_ACTION_MESSAGE_RECEIVED.equals(action)) {
            processMessage(bundle);
        }
    }

    protected abstract void onRegisteredId(String regId);

    protected abstract void onHandleReceive(Pushing.Action action, String message, JSONObject extraJson);

    private void processMessage(Bundle bundle) {
        String title = bundle.getString(Pushing.JPUSH_EXTRA_TITLE);
        String message = bundle.getString(EXTRA_MESSAGE);

        Log.d(TAG, "[title]: %s, [message]: %s", title, message);

        JSONObject extraJson = Json.from(bundle.getString(Pushing.JPUSH_EXTRA_EXTRA));
        if (extraJson != null && Assert.notEmpty(message)) {
            Pushing.Action action = Pushing.Action.valueFrom(Maths.valueOf(Json.optString(extraJson, Pushing.JPath.ACTION), Pushing.Action.NONE.action));

            onHandleReceive(action, message, extraJson);
        }
    }

    private String getBundleExtra(Bundle bundle) {
        StringBuilder text = new StringBuilder();

        for (String key : bundle.keySet()) {
            text.append(key).append(": ").append(bundle.get(key).toString()).append("; ");
        }

        return text.toString();
    }
}

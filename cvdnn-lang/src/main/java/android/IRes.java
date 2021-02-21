package android;

import android.assist.Assert;
import android.io.Stream;
import android.json.Json;
import android.log.Log;

import androidx.annotation.RawRes;

import org.json.JSONObject;

import java.io.InputStream;

public final class IRes {
    private static final String TAG = "IRes";

    public static int idRaw(String resName) {
        return identify(C.tag.resource_type_raw, resName);
    }

    public static InputStream openRawRes(@RawRes int rawRes) {
        InputStream in = null;

        if (rawRes != 0) {
            try {
                in = Args.Env.Res.openRawResource(rawRes);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return in;
    }

    public static InputStream openRawRes(String resName) {
        return openRawRes(idRaw(resName));
    }

    public static int identify(String type, String resName) {
        int resId = 0;
        if (Assert.notEmpty(resName)) {
            try {
                resId = Args.Env.Res.getIdentifier(resName, type, Args.Env.Package.packageName);
            } catch (Throwable t) {
                Log.d(TAG, t);
            }
        }

        return resId;
    }

    public static JSONObject openRawJson(@RawRes int rawId) {
        return Json.from(Stream.text(IRes.openRawRes(rawId)));
    }
}

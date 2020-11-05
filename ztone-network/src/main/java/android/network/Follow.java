package android.network;

import org.json.JSONObject;

import java.io.InputStream;

public interface Follow<O> {

    void onPulled(O response);

    void onError(int statusCode, String message);

    interface StringFollow extends Follow<String> {

    }

    interface StreamFollow extends Follow<InputStream> {

    }

    interface JSONFollow extends Follow<JSONObject> {

    }
}

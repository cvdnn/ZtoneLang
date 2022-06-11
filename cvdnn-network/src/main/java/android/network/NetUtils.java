package android.network;

import android.Args;
import android.assist.Assert;
import android.io.FileUtils;
import android.log.Log;
import android.log.Pairing;
import android.log.service.AysLog;
import android.net.NetworkInfo;
import android.task.AsyncTask;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NetUtils {
    private static final String TAG = "NetUtils";

    public static final String URL_NETWORK_TEST = "https://aliyun.com";

    public static boolean isConnected() {
        return success(request(URL_NETWORK_TEST));
    }

    public static boolean isAvailable() {
        boolean result = false;

        NetworkInfo mNetworkInfo = Args.Env.Cnn.getActiveNetworkInfo();
        result = mNetworkInfo != null && mNetworkInfo.isAvailable();

        return result;
    }

    public static boolean success(Response response) {
        return response != null && response.success();
    }

    public static void request(final String url, final Request.Callback callback) {
        new AsyncTask<Response>() {

            @Override
            protected Response doInBackground() {
                return request(url);
            }

            @Override
            protected void onPostExecute(Response response) {
                if (callback != null) {
                    callback.execute(response);
                }
            }
        }.run();
    }

    public static Response request(String url) {
        Response response = null;

        Pairing pair = Pairing.kv().action(map(url).get("a"))
                .add("url", url)
                .add("network_available", NetUtils.isAvailable());
        try {
            Log.i(TAG, url);

            response = Pulley.impl().get(url);
            if (NetUtils.success(response)) {
                // do nothing
            } else if (response != null) {
                AysLog.e(TAG, pair.add("status", response.status));

            } else {
                AysLog.e(TAG, pair.add("response", null));
            }
        } catch (Exception e) {
            response = new Response.Builder()
                    .set(-HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        }

        return response;
    }

    public static Response post(String url, JSONObject json) {

        return post(url, null, json);
    }

    public static Response post(String url, Map<String, String> headers, JSONObject json) {
        Response response = null;

        try {
            Log.i(TAG, url);

            response = Pulley.impl().post(url, headers, json);
        } catch (Exception e) {
            response = new Response.Builder()
                    .set(-HTTP_BAD_REQUEST)
                    .message(e.getMessage())
                    .build();

            Log.e(e);
        }

        return response;
    }

    public static Response post(String url, File file) {

        return post(url, null, file);
    }

    public static Response post(String url, Map<String, String> headers, File file) {
        Response response = null;

        if (Assert.exists(file)) {
            try {
                Log.i(TAG, url);

                response = Pulley.impl().post(url, headers, file);
            } catch (Exception e) {
                response = new Response.Builder()
                        .set(-HTTP_BAD_REQUEST)
                        .message(e.getMessage())
                        .build();

                Log.e(e);
            }
        }

        return response;
    }

    public static boolean download(String url, @NonNull File file) {
        boolean result = false;

        if (file != null) {
            if (file.exists()) {
                file.delete();
            }

            try {
                file.createNewFile();
            } catch (Exception e) {
                Log.e(e);
            }

            Response response = request(url);
            if (NetUtils.success(response)) {
                result = FileUtils.write(response.stream(), file.getAbsolutePath());
            }
        }

        return result;
    }

    public static ArrayMap<String, String> map(String url) {
        ArrayMap<String, String> kvMap = new ArrayMap<>();

        String key = "";
        int cursor = url.indexOf('?') + 1, len = url.length(), pile = cursor;
        while (cursor > 0 && cursor < len) {
            char c = url.charAt(cursor);
            if (c == '=') {
                if (cursor > pile) {
                    key = url.substring(pile, cursor);

                    kvMap.put(key, "");
                }

                cursor++;
                pile = cursor;

            } else if (c == '&') {
                if (cursor > pile && Assert.notEmpty(key)) {
                    kvMap.put(key, url.substring(pile, cursor));
                }

                key = "";

                cursor++;
                pile = cursor;
            } else if (cursor < len) {
                cursor++;

                if (cursor == len) {
                    if (cursor > pile && Assert.notEmpty(key)) {
                        kvMap.put(key, url.substring(pile, cursor));
                    }

                    key = "";

                    break;
                }
            }
        }

        return kvMap;
    }

    public static String path(@NonNull String url) {
        String path = "";

        if (Assert.notEmpty(url)) {
            int index = url.indexOf('?');
            if (index >= 0) {
                path = url.substring(0, index);
            } else {
                path = url;
            }
        }

        return path;
    }

    public static void remove(Map<String, String> map) {
        if (Assert.notEmpty(map)) {
            map.remove("a");
            map.remove("c");
        }
    }

    public static String urlEncode(String text) {

        return urlEncode(text, UTF_8);
    }

    public static String urlEncode(String text, Charset charset) {
        String result = "";

        if (Assert.notEmpty(text)) {
            try {
                result = URLEncoder.encode(text, (charset != null ? charset : UTF_8).name())
                        .replace("+", "%20")
                        .replace("*", "%2A")
                        .replace("~", "%7E")
                        .replace("/", "%2F");
            } catch (Exception e) {
                Log.e(TAG, e.getCause().toString());
            }
        }

        return result;
    }
}

package android.network;

import android.Loople;
import android.Manifest;
import android.assist.Assert;
import android.concurrent.ThreadPool;
import android.io.Stream;
import android.log.Log;
import android.math.Maths;
import android.network.Follow.JSONFollow;
import android.network.Follow.StreamFollow;
import android.network.Follow.StringFollow;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import static android.network.Pulley.Method.GET;
import static android.network.Pulley.Method.POST;

public class Pulley {
    private static final String TAG = "Pulley";

    private final Property mProperty = new Property();

    private Pulley(Property p) {
        mProperty.from(p);
    }

    public enum Method {
        GET,
        POST
    }

    public static final class Property {
        public int connectTimeout = 20000;
        public int readTimeout = 15000;

        public Method method = GET;


        public void from(Property p) {
            connectTimeout = p.connectTimeout;
        }
    }

    public static final class Builder {
        private final Property mProperty = new Property();

        public Builder connectTimeout(int timeout) {
            mProperty.connectTimeout = timeout;

            return this;
        }

        public Builder readTimeout(int timeout) {
            mProperty.readTimeout = timeout;

            return this;
        }

        public Pulley build() {
            HttpsURLConnection.setDefaultHostnameVerifier(Request.TSL.verifier());

            return new Pulley(mProperty);
        }
    }

    public static Pulley impl() {
        return new Builder().build();
    }

    //////////////////////////////
    //

    @RequiresPermission(Manifest.permission.INTERNET)
    public Future<Response> enqueue(@NonNull final String url, final Follow follow) {
        return ThreadPool.Impl.submit(new Callable<Response>() {
            private Response response;

            @Override
            @SuppressWarnings("unchecked")
            public Response call() {
                try {
                    response = get(url);
                    if (response.success()) {
                        final Response.Content content = response.content();
                        if (follow instanceof StreamFollow) {
                            follow.onPulled(response.stream());

                        } else if (follow instanceof StringFollow) {
                            Loople.Main.post(() -> follow.onPulled(content.text()));

                        } else if (follow instanceof JSONFollow) {
                            Loople.Main.post(() -> follow.onPulled(content.json()));
                        }
                    } else if (follow != null) {
                        Loople.Main.post(() -> follow.onError(response.status, ""));
                    }
                } catch (Exception e) {
                    Log.e(e);
                }

                return response;
            }
        });
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public Response get(@NonNull String url) throws Exception {
        Response.Builder response = new Response.Builder();

        HttpURLConnection conn = createHttpURLConnection(GET, url);
        conn.connect();

        response.set(conn);
        response.set(conn.getResponseCode());
        response.message(conn.getResponseMessage());
        response.setContentLength(Maths.valueOf(conn.getHeaderField("Content-Length"), -1));

        return response.build();
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public Response post(@NonNull String url, Map<String, String> headers, JSONObject json) throws Exception {
        Response.Builder response = new Response.Builder();

        String text = json != null ? json.toString() : "";

        HttpURLConnection conn = createHttpURLConnection(POST, url);
        conn.setRequestProperty("Content-Length", String.valueOf(text.length()));

        if (Assert.notEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        conn.connect();

        DataOutputStream out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(text);
            out.flush();
        } catch (Exception e) {
            Log.e(TAG, e);
        } finally {
            Stream.close(out);
        }

        response.set(conn);
        response.set(conn.getResponseCode());
        response.message(conn.getResponseMessage());
        response.setContentLength(Maths.valueOf(conn.getHeaderField("Content-Length"), -1));

        return response.build();
    }

    private HttpURLConnection createHttpURLConnection(@NonNull Method method, @NonNull String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod(method.name());
        conn.setDoInput(true);
        conn.setDoOutput(method == POST);
        conn.setRequestProperty("User-Agent", "RB-Auto/1.0 (Android-21;)");
        conn.setRequestProperty("Content-Type", method == GET ? "application/x-www-form-urlencoded" : "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Accept-Encoding", "gzip");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(mProperty.connectTimeout);
        conn.setReadTimeout(mProperty.readTimeout);

        if (method == POST) {
            conn.setRequestProperty("Accept", "application/json");
        }

        Request.TSL.set(conn);

        return conn;
    }
}

package android.network;

import android.assist.Assert;
import android.io.Stream;
import android.json.Json;
import android.log.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class Response {
    private static final String TAG = "Response";

    public static class Content {
        private final InputStream input;

        private Content(InputStream input) {
            this.input = input;
        }

        public String text() {

            return Stream.text(input);
        }

        public JSONObject json() {
            return Json.getJSONObject(input);
        }
    }

    public static class Builder {
        private final Response response = new Response();

        public Builder set(int status) {
            response.status = status;

            return this;
        }

        public Builder set(HttpURLConnection conn) {
            response.conn = conn;

            return this;
        }

        public Builder message(String msg) {
            response.message = msg != null ? msg : "";

            return this;
        }

        public Builder setContentLength(int len) {
            response.contentLength = len;

            return this;
        }

        public Response build() {

            return response;
        }
    }

    ////////////////////////
    //

    public HttpURLConnection conn;

    public int status;
    public String message;

    public int contentLength;

    private Response() {
        message = "";
    }

    public boolean success() {
        return status >= HTTP_OK && status < HTTP_BAD_REQUEST;
    }

    public InputStream stream() {
        InputStream input = null;

        if (conn != null && status >= HTTP_OK && status < HTTP_BAD_REQUEST) {
            try {
                input = conn.getInputStream();

                String encoding = conn.getContentEncoding();
                //首先判断服务器返回的数据是否支持gzip压缩，
                if (Assert.notEmpty(encoding) && encoding.contains("gzip")) {
                    input = new GZIPInputStream(input);
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return input;
    }

    public Content content() {

        return new Content(stream());
    }

    public void close() {
        if (conn != null) {
            try {
                conn.disconnect();
            } catch (Exception e) {
            }
        }
    }
}

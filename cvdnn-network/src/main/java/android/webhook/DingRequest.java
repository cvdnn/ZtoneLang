package android.webhook;

import android.assist.Assert;
import android.crypto.Crypto;
import android.network.Message;
import android.network.NetUtils;

import static android.crypto.Crypto.Algorithm.HmacSHA256;

public class DingRequest {
    public final String url;
    public final Message message;

    private DingRequest(String url, Message msg) {
        this.url = url;
        this.message = msg;
    }

    public final boolean check() {
        return Assert.notEmpty(url) && message != null;
    }

    public static class Builder {
        private String url;
        private String secret;
        private boolean sign;
        private Message message;

        public Builder url(String url) {
            this.url = url;

            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;

            return this;
        }

        public Builder sign(boolean sign) {
            this.sign = sign;

            return this;
        }

        public Builder message(Message msg) {
            this.message = msg;

            return this;
        }

        private String makeSignUrl() {
            long timestamp = System.currentTimeMillis();
            String text = new StringBuilder().append(timestamp).append('\n').append(secret).toString();
            String sign = NetUtils.urlEncode(Crypto.toBase64(Crypto.digest(text, secret, HmacSHA256)));

            return String.format("%s&timestamp=%d&sign=%s", url, timestamp, sign);
        }

        public DingRequest build() {

            return new DingRequest(sign ? makeSignUrl() : url, message);
        }
    }
}

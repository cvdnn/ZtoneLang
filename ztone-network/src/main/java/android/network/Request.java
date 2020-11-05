package android.network;

import android.log.Log;

import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Request {

    public interface Callback {
        void execute(Response resp);
    }

    public static class TSL {

        public static SSLContext get() throws NoSuchAlgorithmException, KeyManagementException {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, mTrustManagerArray, new SecureRandom());

            return sslContext;
        }

        public static void set(HttpURLConnection conn) {
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

                httpsConn.setHostnameVerifier(TSL.verifier());

                try {
                    SSLContext sslContext = TSL.get();
                    SSLSocketFactory sssFactory = sslContext.getSocketFactory();
                    HttpsURLConnection.setDefaultSSLSocketFactory(sssFactory);
                    httpsConn.setSSLSocketFactory(sssFactory);
                } catch (Exception e) {
                    Log.e(e);
                }
            }
        }

        public static HostnameVerifier verifier() {

            return mIgnoreHostnameVerifier;
        }

        public static TrustManager[] trust() {

            return mTrustManagerArray;
        }

        /**
         * 忽视证书HostName
         */
        private static final HostnameVerifier mIgnoreHostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String s, SSLSession sslsession) {

                return true;
            }
        };

        /**
         * Ignore Certification
         */
        private static final TrustManager[] mTrustManagerArray = {new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate certificates[], String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {

                return new X509Certificate[0];
            }
        }};
    }
}

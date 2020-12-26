package android.crypto;

import android.assist.Assert;
import android.log.Log;
import android.math.AES;
import android.math.Base64;
import android.math.MD5;
import android.math.Maths;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static android.math.Base64.DEFAULT;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Crypto {
    public static final String TAG = "Crypto";

    public enum Algorithm {
        DESedeMAC("DESedeMAC"),
        DESedeMAC_CFB8("DESedeMAC/CFB8"),
        DESedeMAC64("DESedeMAC64"),
        DESMAC("DESMAC"),
        DESMAC_CFB8("DESMAC/CFB8"),
        DESwithISO9797("DESwithISO9797"),
        MD5("MD5"),
        HmacMD5("HmacMD5"),
        HmacSHA1("HmacSHA1"),
        HmacSHA224("HmacSHA224"),
        HmacSHA256("HmacSHA256"),
        HmacSHA384("HmacSHA384"),
        HmacSHA512("HmacSHA512"),
        ISO9797ALG3MAC("ISO9797ALG3MAC"),
        PBEwithHmacSHA("PBEwithHmacSHA"),
        PBEwithHmacSHA1("PBEwithHmacSHA1");

        public String type;

        Algorithm(String t) {
            this.type = t;
        }
    }

    public static byte[] digest(String text, String secret, Algorithm algorithm) {
        byte[] bytes = null;

        if (algorithm != null && Assert.notEmpty(secret) && Assert.notEmpty(text)) {
            try {
                Mac mac = Mac.getInstance(algorithm.type);
                mac.init(new SecretKeySpec(secret.getBytes(UTF_8), algorithm.type));
                bytes = mac.doFinal(text.getBytes(UTF_8));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return Assert.notEmpty(bytes) ? bytes : new byte[0];
    }

    public static String toEncrypt(String text, String secret, Algorithm algorithm) {
        String result = "";
        byte[] bytes = digest(text, secret, algorithm);
        if (Assert.notEmpty(bytes)) {
            result = Maths.toHex(bytes);
        }

        return result != null ? result : "";
    }

    public static byte[] md5(String text) {
        return MD5.encryptToByteArray(text);
    }

    public static String toMD5(String text) {
        return MD5.encrypt(text);
    }

    public static String toAES256(String text, String secret) {
        return AES.encrypt(secret, text);
    }

    public static String toBase64(byte[] bytes) {
        return Assert.notEmpty(bytes) ? Base64.encodeToString(bytes, DEFAULT).replace("\n", "") : "";
    }

    public static String toBase64(String text) {
        return Assert.notEmpty(text) ? Base64.encodeToString(text.getBytes(UTF_8), DEFAULT) : "";
    }
}

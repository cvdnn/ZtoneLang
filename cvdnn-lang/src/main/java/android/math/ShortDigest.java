package android.math;

import android.assist.Assert;

/**
 * 数据缩短器，注被加密到数据数据差异越大，加密得到到数据越精准
 *
 * @author handy
 */
public class ShortDigest {

    public static String encrypt(String text) {
        return encrypt(MD5.encryptToByteArray(text));
    }

    public static String encrypt(byte[] byteArray) {
        String result = new String(Radix62.ELEMENTS, 0, 1);

        if (Assert.notEmpty(byteArray)) {
            result = Radix62.fromDecimal(Maths.hashCode(byteArray));
        }

        return result;
    }
}
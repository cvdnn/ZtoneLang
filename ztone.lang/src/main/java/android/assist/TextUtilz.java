package android.assist;

import android.log.Log;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.Const.BUFFER_LENGTH;
import static android.Const.ENCODING;
import static android.Const.SQL_ESCAPE;
import static android.Const.SQL_WILDCARD;
import static android.assist.TextLinker.SEPARATOR_COMMA;

/**
 * TextUtilz和TextUtils区别开
 *
 * @author handy
 */
public class TextUtilz {
    private static final String TAG = "TextUtilz";

    /**
     * 文本裁剪，不可逆
     *
     * @param text
     * @return
     */
    public static native String toTrim(String text);

    /**
     * 默认文本加密
     *
     * @param text
     * @return
     */
    public static native String toFake(String text);

    /**
     * 默认文本解密
     *
     * @param text
     * @return
     */
    public static native String fromFake(String text);

    /**
     * 默认二进制加密
     *
     * @param ba
     * @return
     */
    public static native byte[] toMix(byte[] ba);

    /**
     * 默认二进制解密
     *
     * @param ba
     * @return
     */
    public static native byte[] fromMix(byte[] ba);

    /**
     * 救值, 空字符串返回"null"
     *
     * @param value
     * @return
     */
    public static String nullTo(String value) {

        return Assert.notEmpty(value) ? value : "null";
    }

    /**
     * value to string
     *
     * @param v
     * @return
     */
    public static <V> String toString(V v) {

        return v instanceof String ? (String) v : (v != null ? v.toString() : "");
    }

    /**
     * 输入流转变成字符串
     *
     * @param streamData
     * @param charsetName 编码
     * @return
     */
    public static String toString(InputStream streamData, String charsetName) {
        String tmpStr = null;
        if (streamData != null) {
            ByteArrayOutputStream bos = null;
            int len = -1;
            byte[] buff = new byte[BUFFER_LENGTH];

            try {
                bos = new ByteArrayOutputStream();
                while ((len = streamData.read(buff, 0, BUFFER_LENGTH)) != -1) {
                    bos.write(buff, 0, len);
                }
                bos.flush();

                tmpStr = bos.toString(Assert.notEmpty(charsetName) ? charsetName : ENCODING);
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (Exception e) {
                        // do nothing
                    }
                    bos = null;
                }
            }
        }

        return tmpStr;
    }

    /**
     * 输入流转变成字符串，默认utf-8
     *
     * @param streamData
     * @return
     */
    public static String toString(InputStream streamData) {

        return toString(streamData, ENCODING);
    }

    public static int getCharsLength(CharSequence text) {

        return Assert.notEmpty(text) ? text.length() : 0;
    }

    public static int getLength(Object... objs) {
        int length = 0;
        if (Assert.notEmpty(objs)) {
            for (Object o : objs) {
                if (o != null) {
                    length += getCharsLength(o.toString());
                }
            }

        }

        return length;
    }

    /**
     * 替换sql中的参数通陪符
     *
     * @param param
     * @return
     */
    public static String escapeSQLWildcard(String param) {
        String strEscape = param;
        if (!TextUtils.isEmpty(strEscape)) {
            for (String wildcard : SQL_WILDCARD) {
                strEscape = strEscape.replaceAll(wildcard, SQL_ESCAPE + wildcard);
            }
        }

        return strEscape;
    }

    /**
     * 字符串按照分割符分组
     *
     * @param text
     * @return
     */
    public static String[] blockSort(String text) {

        return blockSort(text, SEPARATOR_COMMA);
    }

    /**
     * 字符串按照分割符分组
     *
     * @param text
     * @param boundary
     * @return
     */
    public static String[] blockSort(String text, String boundary) {
        String[] result = null;
        if (Assert.notEmpty(text)) {
            String temp = text.replace(" ", "");
            if (Assert.notEmpty(temp)) {
                result = temp.split(Assert.notEmpty(boundary) ? boundary : SEPARATOR_COMMA);
            }
        }

        return result;
    }
}

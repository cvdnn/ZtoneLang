/*
 * Maths.java
 *
 * Copyright 2011 sillar team, Inc. All rights reserved.
 *
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.math;

import android.Android;
import android.assist.Assert;
import android.crypto.Crypto;
import android.graphics.Rect;
import android.graphics.RectF;
import android.log.Log;

import androidx.annotation.NonNull;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

/**
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2012-9-3
 */
public class Maths {
    private static final String TAG = "Maths";

    public static final int BIN = 2;
    public static final int OCT = 8;
    public static final int DEC = 10;
    public static final int HEX = 16;

    public static final int R62 = 62;
    public static final int R64 = 64;

    public static final int LEN_SHORT = Short.BYTES;
    public static final int LEN_INT = Integer.BYTES;
    public static final int LEN_LONG = Long.BYTES;

    private final static String HEX_NUMS = "0123456789ABCDEF";

    private final static char[] HEX_CHARS = HEX_NUMS.toCharArray();

    /**
     * uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 32未唯一编码
     *
     * @return
     */
    public static String unique() {

        return Crypto.toMD5(Android.Build.cpuSerial() + uuid() + System.currentTimeMillis());
    }

    /**
     * 短编码，区分大小写
     *
     * @return
     */
    public static String shorts() {

        return ShortDigest.encrypt(unique());
    }

    /**
     * 线程安全,不用Random()避免线性可预测,另外在多线程下性能比较低
     *
     * @param min
     * @param max
     *
     * @return
     */
    public static int random(int min, int max) {

        return new SecureRandom().nextInt(max - min + 1) + min;
    }

    public static int min(@NonNull int... array) {
        int value = Integer.MIN_VALUE;

        if (Assert.notEmpty(array)) {
            if (array.length == 2) {
                value = Math.min(array[0], array[1]);
            } else if (array.length > 2) {
                Arrays.sort(array);

                value = array[0];
            } else {
                value = array[0];
            }
        }


        return value;
    }

    public static int max(@NonNull int... array) {
        int value = Integer.MAX_VALUE;

        if (Assert.notEmpty(array)) {
            if (array.length == 2) {
                value = Math.max(array[0], array[1]);
            } else if (array.length > 2) {
                Arrays.sort(array);

                value = array[array.length - 1];
            } else {
                value = array[0];
            }
        }


        return value;
    }

    public static long min(@NonNull long... array) {
        long value = Long.MIN_VALUE;

        if (Assert.notEmpty(array)) {
            if (array.length == 2) {
                value = Math.min(array[0], array[1]);
            } else if (array.length > 2) {
                Arrays.sort(array);

                value = array[0];
            } else {
                value = array[0];
            }
        }


        return value;
    }

    public static long max(@NonNull long... array) {
        long value = Long.MAX_VALUE;

        if (Assert.notEmpty(array)) {
            if (array.length == 2) {
                value = Math.max(array[0], array[1]);
            } else if (array.length > 2) {
                Arrays.sort(array);

                value = array[array.length - 1];
            } else {
                value = array[0];
            }
        }

        return value;
    }

    public static boolean valueOf(String strBoolean) {

        return valueOf(strBoolean, false);
    }

    public static boolean valueOf(Object value, boolean defValue) {
        boolean result = defValue;

        if (value != null) {
            if (value instanceof Boolean) {
                result = (Boolean) value;

            } else if (value instanceof String) {
                result = "true".equalsIgnoreCase((String) value) || "1".equals((String) value);

            } else if (value instanceof Number) {
                result = (1 == ((Number) value).intValue());

            }
        }

        return result;
    }

    public static int intValue(Object obtValue) {

        return valueOf(obtValue, 0, DEC);
    }

    public static int valueOf(Object obtValue, int defValue) {

        return valueOf(obtValue, defValue, DEC);
    }

    public static int valueOf(Object objValue, int defValue, int radix) {
        int value = defValue;

        if (objValue != null) {
            if (objValue instanceof Number) {
                value = ((Number) objValue).intValue();

            } else if (objValue instanceof byte[]) {
                byte[] b = (byte[]) objValue;
                if (Assert.notEmpty(b)) {
                    int lenInt = Math.min(b.length, LEN_INT);
                    for (int i = 0; i < lenInt; i++) {
                        value |= (b[lenInt - 1 - i] & 0xFF) << 8 * i;
                    }
                }

//                value = b[3] & 0xFF |
//                        (b[2] & 0xFF) << 8 |
//                        (b[1] & 0xFF) << 16 |
//                        (b[0] & 0xFF) << 24;
            } else if (objValue instanceof String && Assert.notEmpty((String) objValue)) {
                try {
                    value = Integer.parseInt((String) objValue, radix);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

    public static long longValue(Object objLong) {

        return valueOf(objLong, 0l, DEC);
    }

    public static long valueOf(Object objLong, long defValue) {

        return valueOf(objLong, defValue, DEC);
    }

    public static long valueOf(Object objValue, long defValue, int radix) {
        long value = defValue;

        if (objValue != null) {
            if (objValue instanceof Number) {
                value = ((Number) objValue).longValue();

            } else if (objValue instanceof byte[]) {
                byte[] b = (byte[]) objValue;
                if (Assert.notEmpty(b)) {
                    int lenInt = Math.min(b.length, LEN_LONG);
                    for (int i = 0; i < lenInt; i++) {
                        value |= (b[lenInt - 1 - i] & 0xFF) << 8 * i;
                    }
                }

//                value = (long) (b[7] & 0xFF) |
//                        (long) (b[6] & 0xFF) << 8 |
//                        (long) (b[5] & 0xFF) << 16 |
//                        (long) (b[4] & 0xFF) << 24 |
//                        (long) (b[3] & 0xFF) << 32 |
//                        (long) (b[2] & 0xFF) << 40 |
//                        (long) (b[1] & 0xFF) << 48 |
//                        (long) (b[0] & 0xFF) << 56;

            } else if (objValue instanceof String && Assert.notEmpty((String) objValue)) {
                try {
                    value = Long.parseLong((String) objValue, radix);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

    public static short shortValue(byte[] b) {
        short v = 0;

        if (Assert.notEmpty(b)) {
            int lenInt = Math.min(b.length, LEN_SHORT);
            for (int i = 0; i < lenInt; i++) {
                v |= (b[lenInt - 1 - i] & 0xFF) << 8 * i;
            }
        }

//        v = (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);

        return v;
    }

    public static short shortValue(Object objValue) {
        return valueOf(objValue, (short) 0);
    }

    public static short valueOf(Object objValue, short defValue) {
        short value = defValue;

        if (objValue != null) {
            if (objValue instanceof Number) {
                value = ((Number) objValue).shortValue();

            } else if (objValue instanceof String && Assert.notEmpty((String) objValue)) {
                try {
                    value = Short.parseShort((String) objValue);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

    public static float floatValue(Object objValue) {
        return valueOf(objValue, 0f);
    }

    public static float valueOf(Object objValue, float defValue) {
        float value = defValue;

        if (objValue != null) {
            if (objValue instanceof Number) {
                value = ((Number) objValue).floatValue();

            } else if (objValue instanceof String && Assert.notEmpty((String) objValue)) {
                try {
                    value = Float.parseFloat((String) objValue);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

    public static double doubleValue(Object objValue) {
        return valueOf(objValue, 0d);
    }

    public static double valueOf(Object objValue, double defValue) {
        double value = defValue;

        if (objValue != null) {
            if (objValue instanceof Number) {
                value = ((Number) objValue).doubleValue();

            } else if (objValue instanceof String && Assert.notEmpty((String) objValue)) {
                try {
                    value = Double.parseDouble((String) objValue);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

//    public static String fromHex(String hex) {
//        String strHex = null;
//
//        if (Assert.notEmpty(hex)) {
//            strHex = new String(asByte(hex));
//        }
//
//        return strHex;
//    }

    /**
     * 将十六进制字符串等同变换成十六进制byte数组
     * 例：
     * 1234 -> {0x12, 0x34}
     *
     * @param hex
     *
     * @return
     */
    public static byte[] asByte(String hex) {
        byte[] result = null;

        if (Assert.notEmpty(hex)) {
            final int len = hex.length() >> 1;
            result = new byte[len];

            try {
                for (int i = 0; i < len; i++) {
                    result[i] = Integer.valueOf(hex.substring(2 * i, 2 * i + 2), HEX).byteValue();
                }
            } catch (Exception e) {
                Log.d(TAG, e);
            }
        }

        return result;
    }

    public static String format(byte... buf) {
        String strHex = null;

        if (buf != null) {
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (int i = 0; i < buf.length; i++) {
                append(result, buf[i]);
            }

            strHex = result.toString();
        }

        return strHex;
    }

    private static void append(StringBuffer sb, byte b) {
        if (sb != null) {
            sb.append(HEX_NUMS.charAt((b >> 4) & 0x0f)).append(HEX_NUMS.charAt(b & 0x0f));
        }
    }

    /**
     * 字符串变换成ASCII码
     * 例：
     * 0 -> 0x30, A -> 0x41 ...
     *
     * @param chars
     *
     * @return 每个Byte之间空格分隔，如: [30 41]
     */
    public static String formatToASCIIHexText(String chars) {
        String strHex = null;
        if (Assert.notEmpty(chars)) {
            strHex = format(chars.getBytes());
        }

        return strHex;
    }

    /**
     * 十六进制字符串转换成 ASCII字符串
     *
     * @param hexStr String Byte字符串
     *
     * @return String 对应的字符串
     */
    public static String formASCCII(String hexStr) {
        byte[] bytes = null;
        if (Assert.notEmpty(hexStr)) {
            hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);

            char[] hexs = hexStr.toCharArray();
            bytes = new byte[hexStr.length() / 2];

            int iTmp = 0x00;

            for (int i = 0; i < bytes.length; i++) {
                iTmp = HEX_NUMS.indexOf(hexs[2 * i]) << 4;
                iTmp |= HEX_NUMS.indexOf(hexs[2 * i + 1]);
                bytes[i] = (byte) (iTmp & 0xFF);
            }
        } else {
            bytes = new byte[0];
        }

        return new String(bytes);
    }

    public static String formatByBinary(int a) {
        String sr = "";
        String sc = "";
        int count = 0;        //二进制长度
        int b = Math.abs(a);
        while (b != 0) {
            sr = (b % 2) + sr;
            b = b / 2;
            count++;
        }
        if (a == 0) {
            for (int i = 0; i < 16 - count; i++) {  //16位的二进制
                sc += "0";
            }
            return sc;
        }
        if (a > 0) {
            for (int i = 0; i < 16 - count; i++) {  //16位的二进制
                sc += "0";
            }
            return sc + sr;
        } else {
            for (int i = 0; i < 16 - count; i++) {  //16位的二进制
                sc += "1";
            }
            for (int i = 0; i < sr.length(); i++) {    //取反
                if (sr.charAt(i) == '1') {
                    sc += '0';
                } else
                    sc += '1';
            }
            //加一
            int m = sc.length();        //记录原SC的长度
            for (int i = sc.length() - 1; i >= 0; i--) {
                if (sc.charAt(i) == '1') {
                    sc = sc.substring(0, i);
                    for (int j = 0; j < m - i; j++)
                        sc += '0';
                } else {
                    sc = sc.substring(0, i);
                    sc += '1';
                    for (int j = 0; j < m - i - 1; j++)
                        sc += '0';
                    break;
                }
            }
            return sc;
        }
    }

    public static byte[] toBytes(long v, int len) {
        len = Math.min(len, LEN_LONG);

        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) ((v >> 8 * (len - 1 - i)) & 0xFF);
        }

        return bytes;
    }

    public static byte[] toBytes(long v) {
        return new byte[]{
                (byte) ((v >> 56) & 0xFF),
                (byte) ((v >> 48) & 0xFF),
                (byte) ((v >> 40) & 0xFF),
                (byte) ((v >> 32) & 0xFF),
                (byte) ((v >> 24) & 0xFF),
                (byte) ((v >> 16) & 0xFF),
                (byte) ((v >> 8) & 0xFF),
                (byte) (v & 0xFF)
        };
    }

    public static byte[] toBytes(int v, int len) {
        len = Math.min(len, LEN_INT);

        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) ((v >> 8 * (len - 1 - i)) & 0xFF);
        }

        return bytes;
    }

    public static byte[] toBytes(int v) {
        return new byte[]{
                (byte) ((v >> 24) & 0xFF),
                (byte) ((v >> 16) & 0xFF),
                (byte) ((v >> 8) & 0xFF),
                (byte) (v & 0xFF)
        };
    }

    public static byte[] toBytes(short s) {
        return new byte[]{
                (byte) ((s >> 8) & 0xFF),
                (byte) (s & 0xFF)};
    }

    public static byte xor(byte... bytes) {
        byte result = 0x00;

        if (Assert.check(bytes)) {
            for (byte b : bytes) {
                result ^= b;
            }
        }

        return result;
    }

    public static byte xor(byte[] bytes, int from, int to) {
        byte result = 0x00;

        if (Assert.check(bytes)) {
            int len = min(bytes.length, to - from);
            for (int i = from; i < len; i++) {
                result ^= bytes[i];
            }
        }

        return result;
    }

    /**
     * 判断大小月及是否闰年,用来确定"日"的数据
     *
     * @param year
     * @param month java日期月份(从0开始)
     *
     * @return
     */
    public static int getDayInMonth(int year, int month) {
        int endDay = 31;

        int tmpMonth = month + 1;
        if (tmpMonth == 1 || tmpMonth == 3 || tmpMonth == 5 || tmpMonth == 7 || tmpMonth == 8 || tmpMonth == 10
                || tmpMonth == 12) {
            endDay = 31;
        } else if (tmpMonth == 4 || tmpMonth == 6 || tmpMonth == 9 || tmpMonth == 11) {
            endDay = 30;
        } else {
            endDay = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? 29 : 28;// 闰/平年
        }

        return endDay;
    }

    /**
     * Scales up the rectF by the given scale.
     *
     * @hide
     */
    public static Rect scaleRect(Rect src, float scale, boolean isRounded) {
        Rect des = null;
        if (src != null) {
            des = new Rect(src);

            float round = isRounded ? 0.5f : 0.0f;

            if (scale != 1.0f) {
                des.left = (int) (src.left * scale + round);
                des.top = (int) (src.top * scale + round);
                des.right = (int) (src.right * scale + round);
                des.bottom = (int) (src.bottom * scale + round);
            }
        } else {
            des = new Rect();
        }

        return des;
    }

    /**
     * Scales up the rect by the given scale.
     *
     * @hide
     */
    public static RectF scaleRectF(Rect rect, float scale) {

        return scaleRectF(new RectF(rect), scale);
    }

    /**
     * Scales up the rectF by the given scale.
     *
     * @hide
     */
    public static RectF scaleRectF(RectF src, float scale) {
        RectF des = null;
        if (src != null) {
            des = new RectF(src);

            if (scale != 1.0f) {
                des.left = src.left * scale;
                des.top = src.top * scale;
                des.right = src.right * scale;
                des.bottom = src.bottom * scale;
            }
        } else {
            des = new RectF();
        }

        return des;
    }

    /**
     * Scales up the rectF by the given scale.
     *
     * @hide
     */
    public static Rect stretchRect(Rect src, int stretch) {
        Rect des = null;
        if (src != null) {
            des = new Rect(src);

            if (stretch != 0) {
                des.left = src.left - stretch;
                des.top = src.top - stretch;
                des.right = src.right + stretch;
                des.bottom = src.bottom + stretch;
            }
        } else {
            des = new Rect();
        }

        return des;
    }

    /**
     * Scales up the rect by the given scale.
     *
     * @hide
     */
    public static RectF stretchRectF(Rect rect, float stretch) {

        return stretchRectF(new RectF(rect), stretch);
    }

    /**
     * Scales up the rectF by the given scale.
     *
     * @hide
     */
    public static RectF stretchRectF(RectF src, float stretch) {
        RectF des = null;
        if (src != null) {
            des = new RectF(src);

            if (stretch != 0.0f) {
                des.left = src.left - stretch;
                des.top = src.top - stretch;
                des.right = src.right + stretch;
                des.bottom = src.bottom + stretch;
            }
        } else {
            des = new RectF();
        }

        return des;
    }

    public static String hidePhoneNum(String phoneNum) {
        if (Assert.notEmpty(phoneNum) && phoneNum.length() == 11) {
            char[] charArrays = phoneNum.toCharArray();
            Arrays.fill(charArrays, 4, 8, '*');
            phoneNum = String.copyValueOf(charArrays);
        }

        return phoneNum;
    }

    public static String getTimeShortText() {

        return Radix62.fromDecimal(System.currentTimeMillis());
    }
}

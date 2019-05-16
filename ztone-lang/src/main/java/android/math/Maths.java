/*
 * Maths.java
 *
 * Copyright 2011 sillar team, Inc. All rights reserved.
 *
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.math;

import android.assist.Assert;
import android.graphics.Rect;
import android.graphics.RectF;
import android.log.Log;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;

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

    private final static String HEX_NUMS = "0123456789ABCDEF";

    private final static char[] HEX_CHARS = HEX_NUMS.toCharArray();

    /**
     * 线程安全,不用Random()避免线性可预测,另外在多线程下性能比较低
     *
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {

        return new SecureRandom().nextInt(max - min + 1) + min;
    }

    public static boolean valueOf(String strBoolean) {

        return valueOf(strBoolean, false);
    }

    public static boolean valueOf(Object value, boolean defValue) {
        boolean result = defValue;

        if (value != null) {
            if (value instanceof String) {
                String stringValue = (String) value;

                if ("true".equalsIgnoreCase(stringValue) || "1".equals(stringValue)) {
                    result = true;
                }

            } else if (value instanceof Integer || value instanceof Long) {
                int intValue = (Integer) value;

                result = (1 == intValue);

            } else if (value instanceof Boolean) {
                result = (Boolean) value;

            }
        }

        return result;
    }

    public static int valueOf(Object obtValue, int defValue) {

        return valueOf(obtValue, defValue, DEC);
    }

    public static int valueOf(Object obtValue, int defValue, int radix) {
        int value = defValue;

        if (obtValue != null) {
            if (obtValue instanceof Integer) {
                value = (Integer) obtValue;

            } else if (obtValue instanceof String && Assert.notEmpty((String) obtValue)) {
                try {
                    value = Integer.parseInt((String) obtValue, radix);
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return value;
    }

    public static long valueOf(Object objLong, long defValue) {

        return valueOf(objLong, defValue, DEC);
    }

    public static long valueOf(Object objValue, long defValue, int radix) {
        long value = defValue;

        if (objValue != null) {
            if (objValue instanceof Long) {
                value = (Long) objValue;

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

    public static short valueOf(Object objValue, short defValue) {
        short value = defValue;

        if (objValue != null) {
            if (objValue instanceof Short) {
                value = (Short) objValue;

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

    public static float valueOf(Object objValue, float defValue) {
        float value = defValue;

        if (objValue != null) {
            if (objValue instanceof Float) {
                value = (Float) objValue;

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

    public static double valueOf(Object objValue, double defValue) {
        double value = defValue;

        if (objValue != null) {
            if (objValue instanceof Double) {
                value = (Double) objValue;

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

    public static String toHex(String txt) {
        String strHex = null;
        if (Assert.notEmpty(txt)) {
            strHex = toHex(txt.getBytes());
        }

        return strHex;
    }

    public static String fromHex(String hex) {
        String strHex = null;

        if (Assert.notEmpty(hex)) {
            strHex = new String(toByte(hex));
        }

        return strHex;
    }

    public static byte[] toByte(String hexString) {
        byte[] result = null;

        if (Assert.notEmpty(hexString)) {
            final int len = hexString.length() >> 1;
            result = new byte[len];

            try {
                for (int i = 0; i < len; i++) {
                    result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), HEX).byteValue();
                }
            } catch (Exception e) {
                Log.d(TAG, e);
            }
        }

        return result;
    }

    public static String toHex(byte... buf) {
        String strHex = null;

        if (buf != null) {
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (int i = 0; i < buf.length; i++) {
                appendHex(result, buf[i]);
            }

            strHex = result.toString();
        }

        return strHex;
    }

    private static void appendHex(StringBuffer sb, byte b) {
        if (sb != null) {
            sb.append(HEX_NUMS.charAt((b >> 4) & 0x0f)).append(HEX_NUMS.charAt(b & 0x0f));
        }
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str String 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String toHexText(String str) {
        StringBuilder sb = new StringBuilder();

        if (Assert.notEmpty(str)) {
            byte[] bs = str.getBytes();

            for (int i = 0; i < bs.length; i++) {
                sb.append(HEX_CHARS[(bs[i] & 0xFF) >> 4]);
                sb.append(HEX_CHARS[bs[i] & 0x0F]);
            }
        }

        return sb.toString().trim();
    }

    /**
     * 十六进制字符串转换成 ASCII字符串
     *
     * @param hexStr String Byte字符串
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

    public static String toIntText(int a) {
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

    public static short toShort(byte[] b) {
        return (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
    }

    public static int toInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static long toLong(byte[] b) {
        return b[7] & 0xFF |
                (b[6] & 0xFF) << 8 |
                (b[5] & 0xFF) << 16 |
                (b[4] & 0xFF) << 24 |
                (b[3] & 0xFF) << 32 |
                (b[2] & 0xFF) << 40 |
                (b[1] & 0xFF) << 48 |
                (b[0] & 0xFF) << 56;
    }

    /**
     * @param l
     * @return
     */
    public static byte[] toArray(long l) {
        return new byte[]{
                (byte) ((l >> 56) & 0xFF),
                (byte) ((l >> 48) & 0xFF),
                (byte) ((l >> 40) & 0xFF),
                (byte) ((l >> 32) & 0xFF),
                (byte) ((l >> 24) & 0xFF),
                (byte) ((l >> 16) & 0xFF),
                (byte) ((l >> 8) & 0xFF),
                (byte) (l & 0xFF)
        };
    }

    /**
     * @param i
     * @return
     */
    public static byte[] toArray(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };
    }

    public static byte[] toArray(short s) {
        return new byte[]{
                (byte) ((s >> 8) & 0xFF),
                (byte) (s & 0xFF)};
    }

    /**
     * 判断大小月及是否闰年,用来确定"日"的数据
     *
     * @param year
     * @param month java日期月份(从0开始)
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

/*
 * MD5.java
 * 
 * Copyright 2011 sillar team, Inc. All rights reserved.
 * 
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.math;

import android.assist.Assert;
import android.log.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import static android.Const.BUFFER_LENGTH;

/**
 * MD5
 *
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2012-9-8
 */
public class MD5 {
    private static final String TAG = "MD5";

    public static byte[] encryptToByteArray(String str) {
        byte[] result = null;

        if (Assert.notEmpty(str)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(str.getBytes());
                result = md5.digest();
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    public static String encrypt(String str) {
        String result = null;

        if (Assert.notEmpty(str)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(str.getBytes());
                result = Maths.toHex(md5.digest());
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return result;
    }

    public static String encrypt(File file) {
        String hash = null;

        if (Assert.exists(file)) {
            try {
                hash = encrypt(new FileInputStream(file));
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        return hash;
    }

    public static String encrypt(InputStream stream) {
        String hash = null;

        if (stream != null) {
            byte[] buffer = new byte[BUFFER_LENGTH];
            BufferedInputStream in = null;
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                in = new BufferedInputStream(stream);
                int numRead = 0;
                while ((numRead = in.read(buffer)) > 0) {
                    md5.update(buffer, 0, numRead);
                }

                hash = Maths.toHex(md5.digest());
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        Log.v(TAG, e);
                    }
                }
            }
        }

        return hash;
    }

    private MD5() {

    }
}

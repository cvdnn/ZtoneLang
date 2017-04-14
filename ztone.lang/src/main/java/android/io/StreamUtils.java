/*
 * StreamHelper.java
 * 
 * Copyright 2011 sillar team, Inc. All rights reserved.
 * 
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.io;

import android.log.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import static android.Const.BUFFER_LENGTH;
import static android.Const.ENCODING;

/**
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2013-10-20
 */
public class StreamUtils {
    private static String TAG = "StreamUtils";

    public static String getContent(InputStream inStream) {

        return getContent(inStream, ENCODING);
    }

    public static String getContent(InputStream inStream, String charSet) {
        String text = null;

        if (inStream != null) {
            BufferedInputStream binStream = null;
            ByteArrayOutputStream byteOutStream = null;

            try {
                binStream = new BufferedInputStream(inStream);
                byteOutStream = new ByteArrayOutputStream();

                int len = BUFFER_LENGTH;
                byte[] buff = new byte[BUFFER_LENGTH];
                while ((len = binStream.read(buff, 0, BUFFER_LENGTH)) != -1) {
                    byteOutStream.write(buff, 0, len);
                }

                byteOutStream.flush();
                text = byteOutStream.toString(charSet != null ? charSet : ENCODING);
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                close(byteOutStream);
                close(binStream);
            }
        }

        return text;
    }

    /**
     * 获取流,获取完关闭流
     */
    public static byte[] getByteArray(InputStream inStream) {
        byte[] byteArray = null;

        if (inStream != null) {
            BufferedInputStream binStream = null;
            ByteArrayOutputStream byteOutStream = null;

            try {
                binStream = new BufferedInputStream(inStream);
                byteOutStream = new ByteArrayOutputStream();

                int len = BUFFER_LENGTH;
                byte[] buff = new byte[BUFFER_LENGTH];
                while ((len = binStream.read(buff, 0, BUFFER_LENGTH)) != -1) {
                    byteOutStream.write(buff, 0, len);
                }

                byteOutStream.flush();
                byteArray = byteOutStream.toByteArray();
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                close(byteOutStream);
                close(binStream);
            }
        }

        return byteArray;
    }

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                Log.v(TAG, e);
            }
        }
    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                Log.v(TAG, e);
            }
        }
    }

    public static void close(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (Exception e) {
                Log.v(TAG, e);
            }
        }
    }

    public static void close(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (Exception e) {
                Log.v(TAG, e);
            }
        }
    }
}

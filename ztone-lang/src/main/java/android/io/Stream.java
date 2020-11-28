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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

import static android.Const.BUFFER_LENGTH;
import static android.Const.ENCODING;

/**
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2013-10-20
 */
public class Stream {
    private static String TAG = "StreamUtils";

    public static String text(File file) {
        String text = "";

        try {
            text = text(new FileInputStream(file), ENCODING);
        } catch (Exception e) {
            Log.d(e);
        }

        return text;
    }

    public static String text(InputStream inStream) {

        return text(inStream, ENCODING);
    }

    public static String text(File file, String charSet) {
        String text = "";

        try {
            text = text(new FileInputStream(file), charSet);
        } catch (Exception e) {
            Log.d(e);
        }

        return text;
    }

    public static String text(InputStream inStream, String charSet) {
        String text = "";

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
                Log.d(TAG, e);
            } finally {
                close(byteOutStream);
                close(binStream);
            }
        }

        return text;
    }

    public static byte[] read(File file) {
        byte[] bytes = null;

        try {
            bytes = read(new FileInputStream(file));
        } catch (Exception e) {
            Log.d(e);
        }

        return bytes;
    }

    /**
     * 获取流,获取完关闭流
     */
    public static byte[] read(InputStream inStream) {
        byte[] byteArray = new byte[]{};

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
                Log.d(TAG, e);
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

    public static void close(FileChannel chn) {
        if (chn != null) {
            try {
                chn.close();
            } catch (Exception e) {
                Log.v(TAG, e);
            }
        }
    }
}

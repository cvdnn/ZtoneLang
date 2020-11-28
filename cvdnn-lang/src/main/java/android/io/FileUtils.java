/*
 * FileHelper.java
 *
 * Copyright 2011 handyworkgroup, Inc. All rights reserved.
 * handyworkgroup PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.io;

import android.assist.Assert;
import android.log.Log;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import okio.Buffer;
import okio.ByteString;

import static android.Const.BUFFER_LENGTH;
import static android.Const.CHARSET_ENCODING;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.io.Stream.close;

/**
 * 文件工具
 *
 * @author handyworkgroup
 * @version 1.0.0
 * @since 1.0.0 Handy 2011-7-17
 */
public final class FileUtils {
    private static final String TAG = "FileUtils";

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    public static boolean exists(File file) {

        return file != null && file.exists();
    }

    public static boolean exists(String filePath) {

        return exists(new File(filePath));
    }

    public static String checkFileName(String fileName) {
        String tmpName = fileName;

        if (!TextUtils.isEmpty(fileName)) {
            tmpName = tmpName.replaceAll("[\\\\/:\\*?\"<>|]", "");

        }

        return tmpName;
    }

    public static boolean copy(File src, File des) {
        boolean result = false;
        if (src != null & src.exists() & des != null) {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(src));
                bos = new BufferedOutputStream(new FileOutputStream(des));

                int length = -1;
                byte[] buffer = new byte[BUFFER_LENGTH];

                while ((length = bis.read(buffer, 0, BUFFER_LENGTH)) != -1) {
                    bos.write(buffer, 0, length);
                }

                bos.flush();
                result = true;
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (Exception e) {
                    Log.v(TAG, e);
                }
            }
        }

        return result;
    }

    public static boolean write(File file, String text, Charset charset) {
        boolean result = false;

        if (file != null) {
            if (Assert.notEmpty(text)) {
                BufferedOutputStream boutStream = null;

                try {
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();

                        file.createNewFile();
                    }

                    ByteString byteString = ByteString.encodeString(text, charset != null ? charset : CHARSET_ENCODING);

                    boutStream = new BufferedOutputStream(new FileOutputStream(file));
                    byteString.write(boutStream);
                    boutStream.flush();

                    file.setLastModified(System.currentTimeMillis());

                    result = true;
                } catch (Exception e) {
                    Log.d(TAG, e);
                } finally {
                    close(boutStream);
                }
            } else if (file.exists()) {
                file.delete();
            }
        }

        return result;
    }

    public static boolean write(InputStream in, String filePath) {

        return write(in, filePath, -1, null);
    }

    public static boolean write(InputStream in, final String filePath, final long contentLength,
                                final OnProgressChangeListener listener) {
        boolean result = false;

        if (in != null && Assert.notEmpty(filePath)) {
            BufferedInputStream bin = null;
            BufferedOutputStream out = null;

            try {
                File outFile = new File(filePath);
                outFile.getParentFile().mkdirs();

                bin = new BufferedInputStream(in);
                out = new BufferedOutputStream(new FileOutputStream(outFile));

                long wroteLength = 0;
                int tempLen = -1;
                byte[] buffer = new byte[BUFFER_LENGTH];
                while ((tempLen = bin.read(buffer, 0, BUFFER_LENGTH)) != -1) {
                    out.write(buffer, 0, tempLen);

                    if (contentLength > 0 && listener != null) {
                        wroteLength += tempLen;

                        final long tempLength = wroteLength;
//                        LoopUtils.post(new Runnable() { // FIXME LOOP
//
//                            @Override
//                            public void run() {
//                                if (listener != null) {
//                                    listener.onProgressUpdate(filePath, contentLength, tempLength);
//                                }
//                            }
//                        });
                    }
                }

                out.flush();

                result = true;
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                close(out);
                close(bin);
            }
        }

        return result;
    }

    public static String read(String absFilePath, Charset charset) {
        String entity = null;
        if (!TextUtils.isEmpty(absFilePath)) {
            entity = read(new File(absFilePath), charset);
        }

        return entity;
    }

    public static String read(File absFile, Charset charset) {
        String data = null;

        if (absFile != null && absFile.exists() && absFile.isFile()) {
            BufferedInputStream br = null;

            try {
                br = new BufferedInputStream(new FileInputStream(absFile));
                Buffer tmpBuilder = new Buffer();
                tmpBuilder.readFrom(br);

                data = tmpBuilder.readString(charset != null ? charset : CHARSET_ENCODING);
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                close(br);
            }
        }

        return data;
    }

    public static boolean delete(File file) {

        return delete(file, null);
    }

    public static boolean delete(File file, FileFilter filter) {
        boolean result = true;

        if (exists(file)) {
            if (file.isDirectory()) {
                File[] files = filter != null ? file.listFiles(filter) : file.listFiles();
                if (Assert.notEmpty(files)) {
                    for (File subFile : files) {
                        result &= delete(subFile, filter);
                    }
                }
            } else {
                try {
                    result &= file.delete();
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
            }
        }

        return result;
    }
}

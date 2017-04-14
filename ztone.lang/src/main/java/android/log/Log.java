package android.log;

import android.assist.Assert;
import android.assist.DateFormatUtils;
import android.assist.Shell;
import android.assist.Shell.CommandResult;
import android.concurrent.ThreadUtils;
import android.io.FileUtils;
import android.math.Maths;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;

public final class Log {
    private static String TAG = "FRAMEWORK_LOG";

    public static final long OUT_OF_WORK_TIME = 2 * 24 * 3600000;
    public static final long OUT_OF_CRASH_TIME = 5 * 24 * 3600000;

    private static final String SUFFIX_LOG_FILE = ".log";
    private static final String SUFFIX_TEMP_LOG_FILE = SUFFIX_LOG_FILE + ".tmp";

    private static ArrayList<OnLoggingPrinter> mLoggingPrinter = new ArrayList<>();

    public static void register(OnLoggingPrinter log) {
        if (log != null) {
            mLoggingPrinter.add(log);
        }
    }

    public static void unregister(OnLoggingPrinter log) {
        if (log != null) {
            mLoggingPrinter.remove(log);
        }
    }

    public static void v(String msg, Object... args) {
        v(TAG, msg, args);
    }

    public static void v(Throwable t) {
        v(TAG, t);
    }

    public static void v(String tag, Throwable t) {
        v(tag, getMessage(t));

        analytics(tag, t);
    }

    public static void v(String tag, String msg, Object... args) {
        println(android.util.Log.VERBOSE, tag, msg, args);
    }

    public static void d(String msg, Object... args) {
        d(TAG, msg, args);
    }

    public static void d(Throwable t) {
        d(TAG, t);
    }

    public static void d(String tag, Throwable t) {
        d(tag, getMessage(t));

        analytics(tag, t);
    }

    public static void d(String tag, String msg, Object... args) {
        println(android.util.Log.DEBUG, tag, msg, args);
    }

    public static void i(String msg, Object... args) {
        i(TAG, msg, args);
    }

    public static void i(Throwable t) {
        i(TAG, t);
    }

    public static void i(String tag, Throwable t) {
        i(tag, getMessage(t));

        analytics(tag, t);
    }

    public static void i(String tag, String msg, Object... args) {
        println(android.util.Log.INFO, tag, msg, args);
    }

    public static void w(String msg, Object... args) {
        w(TAG, msg, args);
    }

    public static void w(Throwable t) {
        w(TAG, t);
    }

    public static void w(String tag, Throwable t) {
        w(tag, getMessage(t));

        analytics(tag, t);
    }

    public static void w(String tag, String msg, Object... args) {

        println(android.util.Log.WARN, tag, msg, args);
    }

    public static void e(String msg, Object... args) {
        e(TAG, msg, args);
    }

    public static void e(Throwable t) {
        e(TAG, t);
    }

    public static void e(String tag, Throwable t) {
        e(tag, getMessage(t));

        analytics(tag, t);
    }

    public static void e(String tag, String msg, Object... args) {
        println(android.util.Log.ERROR, tag, msg, args);
    }

    public static void println(int priority, String tag, String msg, Object... args) {
        String logMsg = msg;

        if (Assert.notEmpty(msg) && msg.indexOf('%') >= 0 && Assert.notEmpty(args)) {
            try {
                logMsg = String.format(msg, args);
            } catch (Exception e) {
                android.util.Log.d(TAG, msg);
                android.util.Log.v(TAG, "String format error", e);
            }
        }

        android.util.Log.println(priority, tag, logMsg == null ? "" : logMsg);
    }

    private static void analytics(String tag, Throwable t) {
        if (Assert.notEmpty(mLoggingPrinter)) {
            for (OnLoggingPrinter log : mLoggingPrinter) {
                if (log != null) {
                    log.print(tag, t != null ? t.getMessage() : "", t);
                }
            }
        }
    }

    public static String getMessage(Throwable t) {

        return t != null ? t.getMessage() + "\n" + android.util.Log.getStackTraceString(t) : "";
    }

    public static void printCalledStatus() {
        printCalledStatus("");
    }

    public static void printCalledStatus(String startTag) {
        StackTraceElement[] stes = new Throwable().getStackTrace();
        if (stes != null && stes.length > 1) {
            if (Assert.notEmpty(startTag)) {
                Log.d(TAG, "%s: called by: %s.%s(): %d", //
                        startTag, stes[1].getClassName(), stes[1].getMethodName(), stes[1].getLineNumber());
            } else {
                Log.d(TAG, "called by: %s.%s(): %d", //
                        stes[1].getClassName(), stes[1].getMethodName(), stes[1].getLineNumber());
            }
        }
    }

    public static void printCalledStackTrace() {
        printCalledStackTrace("");
    }

    public static void printCalledStackTrace(String startTag) {
        StackTraceElement[] stes = new Throwable().getStackTrace();
        if (stes != null && stes.length > 1) {
            if (Assert.notEmpty(startTag)) {
                Log.d(TAG, "%s: called by: ", startTag);
                String space = " ";
                for (int i = 1; i < stes.length; i++, space += " ") {
                    if (stes[i] != null) {
                        String className = stes[i].getClassName();
                        if (Assert.notEmpty(className)) {
                            if (isSystemAPI(className)) {
                                break;

                            } else {
                                Log.d(TAG, "%s: %s%s.%s(): %d", //
                                        startTag, space, className, stes[i].getMethodName(), stes[i].getLineNumber());
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "[%s]: called by: ", startTag);
                String space = " ";
                for (int i = 1; i < stes.length; i++, space += " ") {
                    if (stes[i] != null) {
                        String className = stes[i].getClassName();
                        if (Assert.notEmpty(className)) {
                            if (isSystemAPI(className)) {
                                break;

                            } else {
                                Log.d(TAG, "[%s]: %s%s.%s(): %d", //
                                        startTag, space, className, stes[i].getMethodName(), stes[i].getLineNumber());
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isSystemAPI(String className) {

        return Assert.notEmpty(className) && //
                className.startsWith("android.app") || //
                className.startsWith("android.os") || //
                className.startsWith("android.database") || //
                className.startsWith("android.reflect") || //
                className.startsWith("android.view") || //
                className.startsWith("android.widget") || //
                className.startsWith("android.content") || //
                className.startsWith("android.support") || //
                className.startsWith("java.lang") || //
                className.startsWith("java.util") || //
                className.startsWith("com.android.internal") || //
                className.startsWith("dalvik") //
                ;
    }

    @Deprecated
    private static void startLogcat(@NonNull final File logDir, final boolean isCleanLastLog) {
        if (logDir != null) {
            final long nowTime = System.currentTimeMillis();
            final String mLoggingFileName = getLogcatName(nowTime);

            // 开始新的日志
            ThreadUtils.start(new Runnable() {

                @Override
                public void run() {
                    // 归档旧日志
                    File[] logList = logDir.listFiles(mArchiveLogFileFilter);
                    if (Assert.notEmpty(logList)) {
                        try {
                            for (File archiveFile : logList) {
                                if (Assert.exists(archiveFile)) {
                                    File[] subFileList = archiveFile.listFiles(mSubTempFileFilter);
                                    if (Assert.notEmpty(subFileList)) {
                                        for (File tempSubFile : subFileList) {
                                            if (Assert.exists(tempSubFile)) {
                                                String fileName = tempSubFile.getName();
                                                int fileLength = fileName.length();

                                                int startIndex = fileName.lastIndexOf(SUFFIX_TEMP_LOG_FILE);
                                                if (startIndex >= 0) {
                                                    String newLogFileName = "";

                                                    // 去掉.tmp.x
                                                    int tmpLogLen = SUFFIX_TEMP_LOG_FILE.length();
                                                    int endIndex = startIndex + tmpLogLen + 1;// +1 index-->lenght
                                                    if (endIndex < fileLength) {
                                                        newLogFileName = fileName.substring(0, startIndex)
                                                                + ("_" + fileName.substring(startIndex + tmpLogLen + 1, fileLength) + SUFFIX_LOG_FILE);
                                                    } else {
                                                        newLogFileName = fileName.substring(0, startIndex + SUFFIX_LOG_FILE.length());
                                                    }

                                                    if (Assert.notEmpty(newLogFileName)) {
                                                        File newLogFile = new File(archiveFile, newLogFileName);
                                                        if (newLogFile.exists()) {
                                                            newLogFile = new File(archiveFile,
                                                                    fileName.substring(0, startIndex) + ("_" + Maths.random(20, 100) + SUFFIX_LOG_FILE));
                                                        }

                                                        boolean copyResult = FileUtils.copy(tempSubFile, newLogFile);
                                                        if (copyResult) {
                                                            tempSubFile.delete();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e);
                        }
                    }

                    File nowDir = new File(logDir, DateFormatUtils.format(new Date(nowTime), "yyyyMMdd"));
                    nowDir.mkdirs();

                    String logPath = new File(nowDir, mLoggingFileName + SUFFIX_TEMP_LOG_FILE).getAbsolutePath();

                    String cleanLogcat = "logcat -c";
                    String logLogcat = String.format("logcat -v time -f %s -r 3072 -n 8", logPath);
                    String[] commandArray = isCleanLastLog ? new String[]{cleanLogcat, logLogcat} : new String[]{logLogcat};
                    CommandResult cr = Shell.execute(commandArray, false);
                    if (cr != null) {
                        Log.i(TAG, cr.successMsg);
                        Log.e(TAG, cr.errorMsg);
                    }
                }

                private FileFilter mArchiveLogFileFilter = new FileFilter() {

                    @Override
                    public boolean accept(File file) {

                        return Assert.exists(file) && file.isDirectory(); //!C.file.path_crash.equals(file.getName())
                    }
                };

                private FileFilter mSubTempFileFilter = new FileFilter() {

                    @Override
                    public boolean accept(File file) {

                        return Assert.exists(file) && file.getName().lastIndexOf(SUFFIX_TEMP_LOG_FILE) >= 0;
                    }
                };

            }, "LOGCAT_THREAD");
        }
    }

    private static String getLogcatName(long time) {

        return DateFormatUtils.format(new Date(time), "HHmmss");
    }
}

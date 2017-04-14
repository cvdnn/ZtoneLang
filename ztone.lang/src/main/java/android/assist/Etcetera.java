package android.assist;

import android.app.ActivityManager;
import android.content.Context;
import android.log.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import static android.Const.LINE_SEPARATOR;
import static android.assist.PackageUtils.TAG;
import static android.assist.Shell.CommandResult.SUCCESS;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by handy on 17-3-15.
 */

public class Etcetera {
    public static final String DEFAULT_SERIAL_NUM = "0000000000000000";

    private static String sCPUSerialNum;
    private static int sCPUCoreNum;
    private static long sTotalMemoryNum;

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号
     */
    public static String cpuSerialNumber() {
        if (Assert.isEmpty(sCPUSerialNum)) {
            Shell.CommandResult result = Shell.execute("cat /proc/cpuinfo | grep Serial", false, true);
            if (result != null && result.result == SUCCESS && Assert.notEmpty(result.successMsg)) {
                String[] msgArray = result.successMsg.split(LINE_SEPARATOR);
                if (Assert.notEmpty(msgArray)) {
                    for (String line : msgArray) {
                        if (Assert.notEmpty(line)) {
                            // 查找到序列号所在行
                            if (line.indexOf("Serial") > -1) {
                                // 提取序列号
                                sCPUSerialNum = line.substring(line.indexOf(":") + 1, line.length()).trim();

                                break;
                            }
                        }
                    }
                }
            }
        }

        return sCPUSerialNum;
    }

    /**
     * 获取cpu核数
     *
     * @return int cpu核数
     */
    public static int cpuCores() {
        if (sCPUCoreNum == 0) {
            try {
                // Get directory containing CPU info
                File dir = new File("/sys/devices/system/cpu/");
                // Filter to only list the devices we care about
                File[] files = dir.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {

                        return Pattern.matches("cpu[0-9]", pathname.getName());
                    }
                });

                // Return the number of cores (virtual CPU devices)
                if (Assert.notEmpty(files)) {
                    sCPUCoreNum = files.length;
                }
            } catch (Exception e) {
                Log.e(TAG, e);

                sCPUCoreNum = 1;
            }
        }

        return sCPUCoreNum;
    }

    /**
     * 空闲内存，单位：K
     *
     * @param context
     * @return
     */
    public static long freeMemory(Context context) {
        long freeMemory = -1;

        if (context != null) {
            ActivityManager actMgr = (ActivityManager) context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            actMgr.getMemoryInfo(memoryInfo);

            freeMemory = memoryInfo.availMem / 1024;
        }

        return freeMemory;
    }

    public static long totalMemory() {
        if (sTotalMemoryNum == 0) {
            long initialMemory = -1;
            BufferedReader localBufferedReader = null;
            try {
                localBufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
                String tempLine = localBufferedReader.readLine();

                if (tempLine != null) {
                    String[] arrayOfString = tempLine.split("\\s+");
                    if (Assert.notEmpty(arrayOfString)) {
                        initialMemory = Integer.valueOf(arrayOfString[1]);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);

            } finally {
                if (localBufferedReader != null) {
                    try {
                        localBufferedReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, e);
                    }
                }
            }

            sTotalMemoryNum = initialMemory;// Byte转换为KB或者MB，内存大小规格化
        }

        return sTotalMemoryNum;
    }

    public static String uuid() {

        return UUID.randomUUID().toString();
    }
}

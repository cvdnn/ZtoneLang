package android.assist;

import static android.Const.LIB_PATH_SYSTEM;

/**
 * Created by handy on 17-3-14.
 */

public class Library {
    /**
     * 载入本地的so库
     *
     * @param libName 库文件名
     * @return 是否加载成功
     */
    public static boolean load(String libName) {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            StringBuilder temp = new StringBuilder(LIB_PATH_SYSTEM).append("lib").append(libName).append(".so");

            try {
                System.loadLibrary(temp.toString());
            } catch (Throwable t) {
                return false;
            }
        }

        return true;
    }
}

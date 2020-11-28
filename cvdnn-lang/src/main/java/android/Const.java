package android;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by handy on 17-3-14.
 */

public class Const {
    public static final Void NIL = new Void(), NULL = null;

    public static final int BUFFER_LENGTH = 2048;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String ENCODING = UTF_8.name();
    public static final Charset CHARSET_ENCODING = UTF_8;

    public static final String[] SQL_WILDCARD = new String[]{"_", "%"};
    public static final String SQL_ESCAPE = "\\\\";

    /**
     * 系统预装路径
     */
    public static final String LIB_PATH_SYSTEM = "/system/lib/";
}

package android.push;

public class Pushing {
    public static final String PERMISSION_SERIAL = "android.serial.SERIAL_PORT";

    public static final String ACTION_SERIAL_TRANSMIT = "android.serial.ACTION_SERIAL_TRANSMIT";
    public static final String ACTION_SERIAL_OTA = "android.serial.ACTION_SERIAL_OTA";

    public static final String JPUSH_ACTION_MESSAGE_RECEIVED = "cn.jpush.android.intent.MESSAGE_RECEIVED";
    public static final String JPUSH_ACTION_REGISTRATION_ID = "cn.jpush.android.intent.REGISTRATION";
    public static final String JPUSH_EXTRA_EXTRA = "cn.jpush.android.EXTRA";
    public static final String JPUSH_EXTRA_MESSAGE = "cn.jpush.android.MESSAGE";
    public static final String JPUSH_EXTRA_REGISTRATION_ID = "cn.jpush.android.REGISTRATION_ID";
    public static final String JPUSH_EXTRA_TITLE = "cn.jpush.android.TITLE";

    public static final String EXTRA_BYTE_ARRAY = "android.serial.BYTE_ARRAY";

    public static final String EXTRA_OTA_META = "android.serial.TAG_PORT";

    public static final String TP_GREEN = "CTL_GREEN";

    public static class JPath {
        public static final String ACTION = "$action";

        public static final String PACKAGE_NAME = "$package_name";

        public static final String TAG_PORT = "$tag_port";
        public static final String VERSION_CODE = "$version_code";
        public static final String VERSION_NAME = "$version_name";
        public static final String APK_URL = "$apk_url";
        public static final String BIN_URL = "$bin_url";
    }

    public enum Action {
        NONE(0),
        TRANSMIT(1),
        RELEASE(3),
        OTA(5);

        public int action;

        Action(int a) {
            action = a;
        }

        public static int valueOf(Action action) {
            return action != null ? action.action : NONE.action;
        }

        public static Action valueFrom(int action) {
            return RELEASE.action == action ? RELEASE : (TRANSMIT.action == action ? TRANSMIT : (OTA.action == action ? OTA : NONE));
        }
    }
}

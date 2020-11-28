package android.assist;

import android.assist.Shell.CommandResult;

public class ADB {
    public static final int DEFAULT_ADB_PORT = 5555;

    public static boolean adbTCP() {

        return adbTCP(DEFAULT_ADB_PORT);
    }

    public static boolean adbTCP(int port) {
        boolean result = false;

        CommandResult commandResult = Shell.execute("setprop service.adb.tcp.port " + (port == -1 ? DEFAULT_ADB_PORT : port), true);
        if (CommandResult.success(commandResult)) {
            commandResult = Shell.execute("stop adbd", true);

            if (CommandResult.success(commandResult)) {
                commandResult = Shell.execute("start adbd", true);

                result = CommandResult.success(commandResult);
            }
        }

        return result;
    }
}

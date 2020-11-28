package android.io;

import java.io.File;

import static android.C.file.path_config;
import static android.C.file.path_data;
import static android.C.file.path_temp;
import static android.os.Environment.getExternalStorageDirectory;

public class FilePaths {
    public final File root, temp, config, data;

    public FilePaths(String name) {
        root = new File(getExternalStorageDirectory(), name);
        root.mkdirs();

        temp = new File(root, path_temp);
        temp.mkdirs();

        config = new File(root, path_config);
        config.mkdirs();

        data = new File(root, path_data);
        data.mkdirs();
    }
}

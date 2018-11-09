package com.jeremy.fastsharedpreferences.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;


public final class IoUtil {

    private IoUtil() {
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
            }
        }
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }
}

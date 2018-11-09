package com.jeremy.fastsharedpreferences.io;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by hailiangliao on 2018/3/16.
 */

public class ReadWriteManager {

    private static final String DEFAULT_DIR_PATH = "fast_sp";

    private String filePath;

    public ReadWriteManager(Context context, String name) {
        this.filePath = context.getFilesDir().getAbsolutePath()
                + File.separator
                + DEFAULT_DIR_PATH
                + File.separator
                + name;
//        Log.d("ReadWriteManager", "filePath: " + filePath);
        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Object obj) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        FileChannel channel = null;
        try {
            prepare();
            fos = new FileOutputStream(filePath);
            channel = fos.getChannel();
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeSilently(channel);
            IoUtil.closeSilently(oos);
            IoUtil.closeSilently(fos);
        }
    }

    public Object read() {
        if (!IoUtil.isFileExist(filePath)) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filePath));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtil.closeSilently(ois);
        }
    }

    private void prepare() throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }
}

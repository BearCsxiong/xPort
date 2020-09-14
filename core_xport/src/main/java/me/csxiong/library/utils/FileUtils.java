package me.csxiong.library.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.text.TextUtils;

/**
 * @Desc : 文件工具
 * @Author : Bear - 2020/9/14
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean exists(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            return file.exists();
        }
        return false;
    }

    public static long length(String path) {
        if (exists(path)) {
            File file = new File(path);
            if (file.isFile()) {
                return file.length();
            }
        }
        return -1;
    }

    public static void delete(String path) {
        if (null != path && path.length() > 0) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 创建文件,此过程会判断是否有容量,路径是否合法
     * @param filePath 图片路径
     * @return 创建的文件，未创建成功时，返回null
     */
    public static File createDir(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 根据路径创建文件
     * @param path 文件路径
     * @return 创建的文件，SD卡异常等原因导致无法创建成功，返回null
     */
    public static File createNewFile(final String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return file;
            }
            if (file.isDirectory()) {
                file.mkdirs();
                return file;
            } else {
                String parentPath = getParentPath(path);
                File parentFolder = new File(parentPath);
                if (parentFolder.exists()) {
                    try {
                        file.createNewFile();
                        return file;
                    } catch (IOException e) {
                    }
                } else if (new File(parentPath).mkdirs()) {
                    try {
                        file.createNewFile();
                        return file;
                    } catch (IOException e) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过路径获得上一层路径
     * @param path 文件路径
     * @return 指定文件的上一层级文件路径，出现异常或者其他情况给出根目录
     */
    public static String getParentPath(final String path) {
        String rootPath = Environment.getExternalStorageDirectory().getPath() + "/";

        if (TextUtils.isEmpty(path)) {
            return rootPath;
        }

        File file = new File(path);
        String tempPath = file.getParent();
        if (TextUtils.isEmpty(tempPath)) {
            return rootPath;
        }
        String parentPath = tempPath + "/";
        if (!exists(parentPath)) {
            createDir(parentPath);
        }
        file = new File(parentPath);
        try {
            if (file == null || !file.exists() || file.getPath().equals("") || file.getPath().equals("/")) {
                return rootPath;
            } else {
                return parentPath;
            }
        } catch (Exception e) {
            return rootPath;
        }
    }

}

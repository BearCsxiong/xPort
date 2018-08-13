package me.csxiong.library.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class DeviceUtils {
    public DeviceUtils() {
    }

    @TargetApi(13)
    public static Point getScreenSize(Context context) {
        @SuppressLint("WrongConstant") WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        if (VERSION.SDK_INT < 13) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    public static boolean isMeizu(String manufacturer) {
        return isAndroidDevice("meizu", manufacturer);
    }

    public static boolean isXiaomi(String manufacturer) {
        return isAndroidDevice("xiaomi", manufacturer);
    }

    public static boolean isOPPO(String manufacturer) {
        return isAndroidDevice("oppo", manufacturer);
    }

    public static boolean isHuawei(String manufacturer) {
        return isAndroidDevice("huawei", manufacturer);
    }

    public static boolean isAndroidDevice(String deviceID, String manufacturer) {
        return deviceID.equalsIgnoreCase(manufacturer);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}
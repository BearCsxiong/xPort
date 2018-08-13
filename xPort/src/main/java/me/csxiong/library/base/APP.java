package me.csxiong.library.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import me.csxiong.library.base.delegate.AppDelegateManager;
import me.csxiong.library.di.component.AppComponent;
import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Application
 */
public class APP extends Application {

    protected static APP _INSTANCE;

    AppDelegateManager mAppDelegate;

    public static APP getInstance() {
        if (_INSTANCE == null) {
            synchronized (APP.class) {
                if (_INSTANCE == null)
                    _INSTANCE = new APP();
            }
        }
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!shouldInit())
            return;
        if (mAppDelegate == null) {
            mAppDelegate = new AppDelegateManager(this);
        }
        mAppDelegate.onCreate(this);

        _INSTANCE = this;

        Utils.init(this);

    }

    /**
     * 是否需要初始化
     *
     * @return
     */
    public boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public AppComponent getAppComponent() {
        return _INSTANCE.mAppDelegate.getAppComponent();
    }
}

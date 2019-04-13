package me.csxiong.library.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import me.csxiong.library.base.delegate.AppDelegateManager;
import me.csxiong.library.di.component.AppComponent;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : Application,实现dagger2注入配置,并初始化交由AppDelegateManager管理生命周期
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class APP extends Application implements HasActivityInjector,
        HasSupportFragmentInjector {

    /**
     * help for Activity inject
     */
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    /**
     * help for Fragment inject
     */
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

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
        mAppDelegate.onCreate(this);

        Utils.init(this);

    }

    /**
     * check is need init
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
        _INSTANCE = this;
        if (mAppDelegate == null) {
            mAppDelegate = new AppDelegateManager(this);
        }
        mAppDelegate.attachBaseContext(base);
    }

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

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAppDelegate.onTerminate(this);
    }

    /**
     * provide appComponent's content for all UIComponent
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return _INSTANCE.mAppDelegate.getAppComponent();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}

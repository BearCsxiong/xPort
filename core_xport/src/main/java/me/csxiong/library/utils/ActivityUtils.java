package me.csxiong.library.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @Desc : 活动栈管理
 * @Author : csxiong create on 2019/7/16
 */
public class ActivityUtils {

    private static Stack<Activity> activityStack;

    private ActivityUtils() {
    }

    public static class ActivityLifcyclerForActivityUtils implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            get().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            get().removeActivity(activity);
        }
    }

    private volatile static ActivityUtils singleton;

    public static ActivityUtils get() {
        synchronized (ActivityUtils.class) {
            if (singleton == null) {
                singleton = new ActivityUtils();
            }
        }
        return singleton;
    }

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLifcyclerForActivityUtils());
    }

    public static Activity getTopActivity() {
        return get()._getTopActivity();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取ActivityStack
     *
     * @return
     */
    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity _currentActivity() {
        Activity activity = null;
        try {
            activity = activityStack.lastElement();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return activity;
    }

    public Activity _getTopActivity() {
        return _currentActivity();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }


    /**
     * 结束指定类名的所有Activity
     */
    public void finishSameActivity(Class<?> cls) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 是否存在Activity
     */
    public boolean hasActivity(Class<?> cls) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 除了cls，结束所有Activity
     */
    public void finishAllActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)
                    && !activityStack.get(i).getClass().equals(cls)) {
                activityStack.get(i).finish();
                activityStack.remove(i);
                i--;
            }
        }
    }

    public boolean isActivityRunning(Class<?> cls) {
        boolean isRunning = false;
        if (activityStack == null) {
            return false;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)
                    && activityStack.get(i).getClass().equals(cls)) {
                isRunning = true;
                return isRunning;
            }
        }
        return isRunning;
    }

}

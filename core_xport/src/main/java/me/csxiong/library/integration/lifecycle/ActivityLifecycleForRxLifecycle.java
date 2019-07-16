package me.csxiong.library.integration.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.Subject;

/**
 * @Desc : 活动生命周期监控主要的Rxlifecycle发射{@link me.csxiong.library.utils.RxLifecycleUtil}
 * @Author : csxiong create on 2019/7/17
 */
@Singleton
public class ActivityLifecycleForRxLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    public ActivityLifecycleForRxLifecycle() {
    }

    /**
     * 通过桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     * 在每个 Activity 的生命周期中发出对应的生命周期事件
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE);
            Log.e("RxLifecycle", "onActivityCreated");
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.START);
            Log.e("RxLifecycle", "onActivityStarted");
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME);
            Log.e("RxLifecycle", "onActivityResumed");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE);
            Log.e("RxLifecycle", "onActivityPaused");
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.STOP);
            Log.e("RxLifecycle", "onActivityStopped");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("RxLifecycle", "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof IActivityLifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY);
            Log.e("RxLifecycle", "onActivityDestroyed");
        }
    }

    private Subject<ActivityEvent> obtainSubject(Activity activity) {
        return ((IActivityLifecycle) activity).provideLifecycle();
    }
}

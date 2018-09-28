package me.csxiong.library.utils;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.annotations.NonNull;
import me.csxiong.library.base.IView;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;
import me.csxiong.library.integration.lifecycle.ILifecycle;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : RxLifecycle绑定对应生命周期工具类
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/9/28 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class RxLifecycleUtil {

    private RxLifecycleUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 绑定 Activity 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final ActivityEvent event) {
        XPreconditions.checkNotNull(view, "view == null");
        if (view instanceof IActivityLifecycle) {
            return bindUntilEvent((IActivityLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't ActivityLifecycleable");
        }
    }

    /**
     * 绑定 Fragment 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final FragmentEvent event) {
        XPreconditions.checkNotNull(view, "view == null");
        if (view instanceof IFragmentLifecycle) {
            return bindUntilEvent((IFragmentLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't FragmentLifecycleable");
        }
    }

    /**
     * @param lifecycleable
     * @param event
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> LifecycleTransformer<T> bindUntilEvent(@NonNull final ILifecycle<R> lifecycleable,
                                                                final R event) {
        XPreconditions.checkNotNull(lifecycleable, "lifecycleable == null");
        return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycle(), event);
    }


    /**
     * 绑定 Activity/Fragment 的生命周期
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        XPreconditions.checkNotNull(view, "view == null");
        if (view instanceof ILifecycle) {
            return bindToLifecycle((ILifecycle) view);
        } else {
            throw new IllegalArgumentException("view isn't Lifecycleable");
        }
    }

    /**
     * @param lifecycleable
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull ILifecycle lifecycleable) {
        XPreconditions.checkNotNull(lifecycleable, "lifecycleable == null");
        if (lifecycleable instanceof IActivityLifecycle) {
            return RxLifecycleAndroid.bindActivity(((IActivityLifecycle) lifecycleable).provideLifecycle());
        } else if (lifecycleable instanceof IFragmentLifecycle) {
            return RxLifecycleAndroid.bindFragment(((IFragmentLifecycle) lifecycleable).provideLifecycle());
        } else {
            throw new IllegalArgumentException("Lifecycleable not match");
        }
    }

}
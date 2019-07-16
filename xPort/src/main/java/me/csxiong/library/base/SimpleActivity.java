package me.csxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.base.delegate.ViewDelegate;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : base on {@link SupportActivity} to build all Activity
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/20 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class SimpleActivity extends SupportActivity implements IView, IPage, IActivityLifecycle {

    private final BehaviorSubject<ActivityEvent> mSubject = BehaviorSubject.create();

    protected ViewDelegate mViewDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //design for MVP or MVVM change
        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId());
        }
        //default viewDelegate help A or F or DF instance proxy method
        if (mViewDelegate == null) {
            mViewDelegate = new ViewDelegate(this);
        }

        //default UI method
        initUI(savedInstanceState);

        //default Data method
        initData(savedInstanceState);
    }

    public void cancelFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public AppComponent getAppComponent() {
        return APP.get().getAppComponent();
    }

    @Override
    public void startLoading(String loadingMsg) {
        mViewDelegate.startLoading(loadingMsg);
    }

    @Override
    public void stopLoading() {
        mViewDelegate.stopLoading();
    }

    @Override
    public Subject<ActivityEvent> provideLifecycle() {
        return mSubject;
    }
}

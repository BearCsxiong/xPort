package me.csxiong.library.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : MVP-Activity基本使用和使用dagger2注入Presenter,实现PV加解绑
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class MVPActivity<T extends IPresenter> extends SimpleActivity implements IMVP, IActivityLifecycle {

    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();

    @Inject
    T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        if (mPresenter != null) mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public Subject<ActivityEvent> provideLifecycle() {
        return mLifecycleSubject;
    }
}
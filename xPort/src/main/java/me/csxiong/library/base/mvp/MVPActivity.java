package me.csxiong.library.base.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.base.SimpleActivity;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;

/**-------------------------------------------------------------------------------
*|
*| desc : base on MVP mode to build A
*|
*|--------------------------------------------------------------------------------
*| on 2019/4/15 created by csxiong
*|--------------------------------------------------------------------------------
*/
public abstract class MVPActivity<T extends IPresenter> extends SimpleActivity implements IActivityLifecycle {

    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();

    @Inject
    T mPresenter;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if (mPresenter != null) mPresenter.attachView(this);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public Subject<ActivityEvent> provideLifecycle() {
        return mLifecycleSubject;
    }
}
package me.csxiong.library.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.base.IMVP;
import me.csxiong.library.base.IPresenter;
import me.csxiong.library.base.SimpleFragment;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : MVP-Fragment基本使用和使用dagger2注入Presenter,实现PV加解绑
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class MVPFragment<T extends IPresenter> extends SimpleFragment implements IMVP, IFragmentLifecycle {

    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    private boolean isInjected;

    @Inject
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!isInjected) {
            initInject();
            isInjected = true;
        }
        if (mPresenter != null) mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public Subject<FragmentEvent> provideLifecycle() {
        return mLifecycleSubject;
    }
}
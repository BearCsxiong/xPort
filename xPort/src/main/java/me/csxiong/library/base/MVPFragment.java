package me.csxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import javax.inject.Inject;

/**-------------------------------------------------------------------------------
*|
*| desc : MVP-Fragment基本使用和使用dagger2注入Presenter,实现PV加解绑
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong
*|--------------------------------------------------------------------------------
*/
public abstract class MVPFragment<T extends IPresenter> extends SimpleFragment {

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

}
package me.csxiong.library.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

/**-------------------------------------------------------------------------------
*|
*| desc : MVP-Activity基本使用和使用dagger2注入Presenter,实现PV加解绑
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong
*|--------------------------------------------------------------------------------
*/
public abstract class MVPActivity<T extends IPresenter> extends SimpleActivity {

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

}
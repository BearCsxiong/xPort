package com.base.multi.business.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.multi.di.component.DaggerActivityComponent;
import com.kehua.pile.R;

import me.csxiong.library.base.MVPActivity;

/**
 * Created by csxiong on 2018/8/8.
 */

@Route(path = "/multi/test")
public class TestActivity extends MVPActivity<TestPresenter> implements TestContract.View {


    @Override
    public int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initInject() {
        DaggerActivityComponent.builder()
                .appComponent(getAppComponent())
                .build().inject(this);
    }
}

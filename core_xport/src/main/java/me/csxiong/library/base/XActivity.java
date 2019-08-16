package me.csxiong.library.base;

import android.arch.lifecycle.AndroidViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * @Desc : MVVM基类结构
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XActivity<T extends ViewDataBinding, K extends AndroidViewModel> extends BaseActivity implements IView {

    public T mBinding;

    @Inject
    public K mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView();
        initData();
    }

}

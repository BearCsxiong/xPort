package me.csxiong.library.base;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;

import dagger.android.AndroidInjection;

/**
 * @Desc : MVVM基类结构
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XActivity<T extends ViewDataBinding, K extends XViewModel> extends BaseActivity<T> implements IPage {

    public K mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        Class<K> classK = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mViewModel = ViewModelProviders.of(this).get(classK);
        mViewModel.setView(this);
        getLifecycle().addObserver(mViewModel);
        super.onCreate(savedInstanceState);
    }
}

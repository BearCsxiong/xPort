package me.csxiong.library.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.AndroidInjection;

/**
 * @Desc : MVVM基类结构
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XActivity<T extends ViewDataBinding, K extends XViewModel> extends BaseActivity<T> implements IPage {

    @Inject
    public Lazy<K> lazyViewModelCreator;

    public K mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        Class<K> classK = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mViewModel = DaggerViewModelProviders.of(this).get(classK, lazyViewModelCreator);
        mViewModel.setView(this);
        getLifecycle().addObserver(mViewModel);
        super.onCreate(savedInstanceState);
    }
}

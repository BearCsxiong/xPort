package me.csxiong.library.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.AndroidSupportInjection;

/**
 * @Desc : MVVM基类Fragment
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XFragment<T extends ViewDataBinding, K extends XViewModel> extends BaseFragment<T> implements IPage {

    @Inject
    public Lazy<K> lazyViewModelCreator;

    public K mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        Class<K> classK = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mViewModel = DaggerViewModelProviders.of(getActivity()).get(classK, lazyViewModelCreator);
        mViewModel.setView(this);
        getLifecycle().addObserver(mViewModel);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}

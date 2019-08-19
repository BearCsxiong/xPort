package me.csxiong.library.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;

import dagger.android.support.AndroidSupportInjection;

/**
 * @Desc : MVVM基类Fragment
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XFragment<T extends ViewDataBinding, K extends AndroidViewModel> extends BaseFragment implements IView {

    public T mBinding;

    public K mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        View view = inflater.inflate(getLayoutId(), null);
        mBinding = DataBindingUtil.bind(view);
        Class<K> classK = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mViewModel = ViewModelProviders.of(getActivity()).get(classK);
        return view;
    }

}

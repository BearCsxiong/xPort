package me.csxiong.library.base;

import android.arch.lifecycle.AndroidViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * @Desc : MVVM基类Fragment
 * @Author : csxiong create on 2019/7/22
 */
public abstract class XFragment<T extends ViewDataBinding, K extends AndroidViewModel> extends BaseFragment implements IView {

    public T mBinding;

    @Inject
    public K mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        mBinding = DataBindingUtil.bind(view);
        return view;
    }

}

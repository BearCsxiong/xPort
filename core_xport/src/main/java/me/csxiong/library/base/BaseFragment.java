package me.csxiong.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Desc : 基类Fragment
 * @Author : csxiong create on 2019/7/22
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements IPage, IView {

    public T mBinding;

    private ViewDelegate mViewDelegate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null, false);
        mBinding = DataBindingUtil.bind(view);
        if (mViewDelegate != null) {
            mViewDelegate.release();
        } else {
            mViewDelegate = new ViewDelegate(getContext());
        }
        initView();
        initData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void startLoading(String msg) {
        if (mViewDelegate != null) {
            mViewDelegate.startLoading(msg);
        }
    }

    @Override
    public void stopLoading() {
        if (mViewDelegate != null) {
            mViewDelegate.stopLoading();
        }
    }

    @Override
    public void startProcessing(int progress, String des) {
        if (mViewDelegate != null) {
            mViewDelegate.startProcessing(progress, des);
        }
    }

    @Override
    public void stopProcessing() {
        if (mViewDelegate != null) {
            mViewDelegate.stopProcessing();
        }
    }
}

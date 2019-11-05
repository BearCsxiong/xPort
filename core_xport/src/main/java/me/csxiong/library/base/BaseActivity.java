package me.csxiong.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @Desc : 基类活动
 * @Author : csxiong create on 2019/7/22
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements IPage,IView {

    public T mViewBinding;

    private ViewDelegate mViewDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (mViewDelegate != null) {
            mViewDelegate.release();
        } else {
            mViewDelegate = new ViewDelegate(this);
        }
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewDelegate != null) {
            mViewDelegate.release();
        }
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

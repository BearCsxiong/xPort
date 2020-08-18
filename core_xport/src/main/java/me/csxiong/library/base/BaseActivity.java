package me.csxiong.library.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @Desc : 基类活动
 * @Author : csxiong create on 2019/7/22
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements IPage, IView {

    /**
     * 基础的Binding
     */
    public T mViewBinding;

    /**
     * View代理
     */
    private ViewDelegate mViewDelegate;

    /**
     * 获取基础的ViewModel
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends AndroidViewModel> T getViewModel(Class<T> clazz) {
        return ViewModelProviders.of(this).get(clazz);
    }

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
        if (mViewDelegate != null) {
            mViewDelegate.release();
        }
        super.onDestroy();
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

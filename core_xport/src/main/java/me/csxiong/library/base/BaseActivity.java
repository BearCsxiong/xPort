package me.csxiong.library.base;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import me.csxiong.library.utils.ImmersiveModeUtil;

/**
 * @Desc : 基类活动
 * @Author : csxiong create on 2019/7/22
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements IPage, IView {

    /**
     * 默认的界面UI配置
     */
    public static ActivityImmersiveConfig config;

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
        if (config != null) {
            config.beforeContentView(this);
        }
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (config != null) {
            config.afterContentView(this);
        }
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

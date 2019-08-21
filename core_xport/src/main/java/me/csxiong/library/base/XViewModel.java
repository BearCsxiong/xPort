package me.csxiong.library.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;

/**
 * @Desc : 基础ViewModel
 * @Author : csxiong create on 2019/7/22
 */
public class XViewModel extends AndroidViewModel implements IView, LifecycleObserver {

    protected IView mView;

    public XViewModel(@NonNull Application application) {
        super(application);
    }

    public void setView(IView iView) {
        mView = iView;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mView = null;
    }

    @Override
    public void startLoading(String msg) {
        if (mView != null) {
            mView.startLoading(msg);
        }
    }

    @Override
    public void stopLoading() {
        if (mView != null) {
            mView.stopLoading();
        }
    }

    @Override
    public void startProcessing(int progress, String des) {
        if (mView != null) {
            mView.startProcessing(progress, des);
        }
    }

    @Override
    public void stopProcessing() {
        if (mView != null) {
            mView.stopProcessing();
        }
    }
}

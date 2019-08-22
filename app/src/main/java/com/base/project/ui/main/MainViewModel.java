package com.base.project.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import io.reactivex.observers.ResourceObserver;
import me.csxiong.library.base.XViewModel;
import me.csxiong.library.integration.ApiProvider;
import me.csxiong.library.utils.RxUtils;
import me.csxiong.library.utils.XToast;

/**
 * @Desc   : 由dagger提供注入的ViewModel
 * @Author : csxiong create on 2019/8/22
 */
public class MainViewModel extends XViewModel {

    @Inject
    ExecutorService executorService;

    @Inject
    ApiProvider apiProvider;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void showLoading(View view) {
        apiProvider.get(MainApi.class)
                .login()
                .compose(RxUtils.onRxThread())
                .subscribeWith(new ResourceObserver<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        XToast.show("ShowLoading");
    }
}

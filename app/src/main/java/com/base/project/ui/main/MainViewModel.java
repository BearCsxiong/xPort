package com.base.project.ui.main;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import me.csxiong.library.base.XViewModel;
import me.csxiong.library.utils.XToast;

/**
 * @Desc   : 由dagger提供注入的ViewModel
 * @Author : csxiong create on 2019/8/22
 */
public class MainViewModel extends XViewModel {

    @Inject
    ExecutorService executorService;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void showLoading(View view) {
        XToast.show("ShowLoading");
    }
}

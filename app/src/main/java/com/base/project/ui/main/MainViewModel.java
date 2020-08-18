package com.base.project.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import me.csxiong.library.utils.XToast;

/**
 * @Desc   : 由dagger提供注入的ViewModel
 * @Author : csxiong create on 2019/8/22
 */
public class MainViewModel extends AndroidViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void showLoading(View view) {
        XToast.show("ShowLoading");
    }
}

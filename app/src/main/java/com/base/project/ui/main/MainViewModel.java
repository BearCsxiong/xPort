package com.base.project.ui.main;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import me.csxiong.library.base.XViewModel;
import me.csxiong.library.utils.XToast;

public class MainViewModel extends XViewModel {

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void showLoading(View view) {
        XToast.show("ShowLoading");
    }
}

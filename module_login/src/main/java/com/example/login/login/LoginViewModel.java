package com.example.login.login;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.csxiong.library.base.XViewModel;

/**
 * @Desc   : 登录ViewModel
 * @Author : csxiong create on 2019/7/22
 */
public class LoginViewModel extends XViewModel {

    @Inject
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
}

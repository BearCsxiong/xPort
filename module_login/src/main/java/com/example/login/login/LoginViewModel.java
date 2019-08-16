package com.example.login.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * @Desc   : 登录ViewModel
 * @Author : csxiong create on 2019/7/22
 */
public class LoginViewModel extends AndroidViewModel {

    @Inject
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
}

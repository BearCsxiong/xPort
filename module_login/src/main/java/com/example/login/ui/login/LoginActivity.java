package com.example.login.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;

import javax.inject.Inject;

import me.csxiong.library.base.SimpleActivity;
import me.csxiong.library.utils.XToast;
import retrofit2.Retrofit;

@Route(path = "/login/login")
public class LoginActivity extends SimpleActivity {

    @Inject
    Retrofit retrofit;

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        XToast.show(retrofit == null ? "LoginModule 注入失败" : "LoginModule 注入成功");
        finish();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}

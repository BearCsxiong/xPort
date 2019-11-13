package com.example.login.login;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivityLoginBinding;

import me.csxiong.library.base.XActivity;

/**
 * @Desc : 登录界面
 * @Author : csxiong create on 2019/7/22
 */
@Route(path = "/login/login", name = "登陆页面")
public class LoginActivity extends XActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }
}

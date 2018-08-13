package com.base.project.main;

import javax.inject.Inject;

import me.csxiong.library.base.RxPresenter;

/**
 * Created by csxiong on 2018/8/10.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter()
    {

    }
}

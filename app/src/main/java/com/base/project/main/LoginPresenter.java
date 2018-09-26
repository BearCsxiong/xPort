package com.base.project.main;

import javax.inject.Inject;

import me.csxiong.library.base.XPresenter;

/**
 * Created by csxiong on 2018/8/10.
 */

public class LoginPresenter extends XPresenter<LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter()
    {

    }
}

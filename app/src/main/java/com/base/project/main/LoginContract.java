package com.base.project.main;

import me.csxiong.library.base.IPresenter;
import me.csxiong.library.base.IView;

/**
 * Created by csxiong on 2018/8/10.
 */

public class LoginContract {
    public interface View extends IView
    {

    }

    public interface Presenter extends IPresenter<LoginContract.View>
    {

    }
}



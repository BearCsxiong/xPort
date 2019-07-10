package com.base.project.main;


import me.csxiong.library.base.IView;
import me.csxiong.library.base.mvp.IPresenter;

/**
 * User: Walten
 * Date: 16/6/28下午6:16
 * Email: wtf55@vip.qq.com
 */
public interface MainContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter<View> {
    }
}
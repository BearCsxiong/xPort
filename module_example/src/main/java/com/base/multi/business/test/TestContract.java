package com.base.multi.business.test;

import me.csxiong.library.base.IPresenter;
import me.csxiong.library.base.IView;

/**
 * Created by csxiong on 2018/8/8.
 */

public interface TestContract {

    interface View extends IView {

    }

    interface Presenter extends IPresenter<TestContract.View> {

    }

}

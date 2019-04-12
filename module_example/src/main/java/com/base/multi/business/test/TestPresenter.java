package com.base.multi.business.test;

import javax.inject.Inject;

import me.csxiong.library.base.mvp.XPresenter;


/**
 * Created by csxiong on 2018/8/8.
 */

public class TestPresenter extends XPresenter<TestContract.View> implements TestContract.Presenter {

    @Inject
    public TestPresenter() {

    }
}

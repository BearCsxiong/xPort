package com.base.project.main;

import javax.inject.Inject;

import me.csxiong.library.base.RxPresenter;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    public MainPresenter() {
    }

}
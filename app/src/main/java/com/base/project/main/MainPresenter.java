package com.base.project.main;

import javax.inject.Inject;

import me.csxiong.library.base.XPresenter;

public class MainPresenter extends XPresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    public MainPresenter() {
    }

}
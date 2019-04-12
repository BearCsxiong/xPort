package me.csxiong.library.base.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;

import javax.inject.Inject;

import me.csxiong.library.base.IMVP;
import me.csxiong.library.base.IPresenter;
import me.csxiong.library.base.SimpleDialog;

/**
 * Created by csxiong on 2018/11/2.
 */

public abstract class MVPDialog<T extends IPresenter> extends SimpleDialog implements IMVP {

    @Inject
    protected T mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        initInject();
        mPresenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

}

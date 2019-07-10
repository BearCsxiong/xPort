package me.csxiong.library.base.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import me.csxiong.library.base.SimpleBottomSheetDialog;

/**
 * ---------------------------------------------------------------------------------
 * |
 * | desc : base on MVP to build bottomSheetDialogFragment
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/4/12 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class MVPBottomSheetDialog<T extends IPresenter> extends SimpleBottomSheetDialog {

    @Inject
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroyView();
    }

}

package me.csxiong.library.base;

import android.content.Context;

import java.lang.ref.WeakReference;

import me.csxiong.library.utils.ThreadExecutor;
import me.csxiong.library.widget.XTipDialog;

/**
 * @Desc : View基础操作的代理
 * @Author : csxiong create on 2019/8/21
 */
public class ViewDelegate implements IView {

    private WeakReference<Context> contextReference;

    private XTipDialog mLoadingDialog;

    public ViewDelegate(Context context) {
        contextReference = new WeakReference<>(context);
    }

    @Override
    public void startLoading(String msg) {
        if (!ThreadExecutor.isUIThread()) {
            return;
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.setTitle(msg);
            return;
        }
        mLoadingDialog = new XTipDialog.Builder(contextReference.get())
                .setIconType(XTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        mLoadingDialog.show();
    }

    @Override
    public void stopLoading() {
        if (!ThreadExecutor.isUIThread()) {
            return;
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    @Override
    public void startProcessing(int progress, String des) {

    }

    @Override
    public void stopProcessing() {

    }

    /**
     * 释放资源
     */
    public void release() {
        stopLoading();
        stopProcessing();
        if (contextReference != null) {
            contextReference.clear();
        }
        contextReference = null;
    }
}

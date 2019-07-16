package me.csxiong.library.base.delegate;

import android.content.Context;

import me.csxiong.library.base.IView;
import me.csxiong.library.base.SimpleActivity;
import me.csxiong.library.base.SimpleFragment;
import me.csxiong.library.utils.XPreconditions;
import me.csxiong.library.widget.XTipDialog;

/**
 * @Desc : IView代理接口,提供多组件默认实现
 * @Author : csxiong create on 2019/7/17
 */
public class ViewDelegate implements IView {

    private XTipDialog mDialog;

    private Context context;

    public ViewDelegate(IView view) {
        XPreconditions.checkNotNull(view, "IView == null , check ViewDelegate!!!");
        if (view instanceof SimpleActivity) {
            context = (Context) view;
        } else if (view instanceof SimpleFragment) {
            context = ((SimpleFragment) view).getActivity();
        } else {
            throw new IllegalArgumentException("check your'Activity or your'Fragment is extends from SimpleActivity or SimpleFragment");
        }
    }

    @Override
    public void startLoading(String loadingMsg) {
        stopLoading();
        mDialog = new XTipDialog.Builder(context)
                .setIconType(XTipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(loadingMsg)
                .create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    @Override
    public void stopLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}

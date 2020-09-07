package me.csxiong.library.integration.sys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import me.csxiong.library.R;
import me.csxiong.library.base.BaseDialog;
import me.csxiong.library.databinding.DialogXBinding;
import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 一个Android原生的Dialog样式 修复Dialog的bug 提供比较基础的构造方式
 * @Author : Bear - 2020/9/7
 */
public class XDialog extends BaseDialog<DialogXBinding> {

    public static int APP_MAIN_COLOR = 0xff000000;

    public static int ELEVATOR = XDisplayUtil.dpToPxInt(1f);

    public static int MARIN_LEFT = XDisplayUtil.dpToPxInt(10f);

    public static int MARIN_RIGHT = XDisplayUtil.dpToPxInt(10f);

    private Builder data;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_x;
    }

    @Override
    public void initView() {
        if (data == null) {
            // 异常情况，应该时销毁重建问题。
            dismiss();
            return;
        }
        mViewBinding.setData(data);
        setCancelable(data.isCancelable());
        getDialog().setOnKeyListener((arg0, keyCode, arg2) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && !data.isCancelable()) {
                return true;
            }
            if (data.getCancelListener() != null) {
                data.getCancelListener().onCancel(this);
            }
            return false;
        });
        mViewBinding.tvCommonDialogANegative.setOnClickListener(v -> {
            if (data.getNegativeClick() != null) {
                data.getNegativeClick().onClick(this);
            } else {
                dismiss();
            }
        });
        mViewBinding.tvCommonDialogAPositive.setOnClickListener(v -> {
            if (data.getPositiveClick() != null) {
                data.getPositiveClick().onClick(this);
            } else {
                dismiss();
            }
        });
        mViewBinding.rlCommonDialogARoot.setOnClickListener(v -> {
            if (data.isCancelable()) {
                dismiss();
                if (data.cancelListener != null) {
                    data.cancelListener.onCancel(this);
                }
            }
        });
        mViewBinding.rlCommonDialogAContent.setOnClickListener(v -> {
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected Integer getAnimations() {
        return R.style.XDialog;
    }

    public void setData(Builder data) {
        this.data = data;
    }

    public static class Builder {
        private String title;
        private String content;
        private String positiveText;
        private String negativeText;
        private PositiveClickListener positiveClick;
        private NegativeClickListener negativeClick;
        private boolean cancelable;
        private DismissListener dismissListener;
        private CancelListener cancelListener;
        private boolean isNeedExtraCloseIcon;

        private int appMainColor = APP_MAIN_COLOR;

        private int elevator = ELEVATOR;

        private int marginLeft = MARIN_LEFT;

        private int marginRight = MARIN_RIGHT;

        public Builder() {

        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getContent() {
            return content;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public String getPositiveText() {
            return positiveText;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public String getNegativeText() {
            return negativeText;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public boolean isNeedExtraCloseIcon() {
            return isNeedExtraCloseIcon;
        }

        public Builder setNeedExtraCloseIcon(boolean needExtraCloseIcon) {
            isNeedExtraCloseIcon = needExtraCloseIcon;
            return this;
        }

        public PositiveClickListener getPositiveClick() {
            return positiveClick;
        }

        public Builder setPositiveClick(PositiveClickListener positiveClick) {
            this.positiveClick = positiveClick;
            return this;
        }

        public NegativeClickListener getNegativeClick() {
            return negativeClick;
        }

        public Builder setNegativeClick(NegativeClickListener negativeClick) {
            this.negativeClick = negativeClick;
            return this;
        }

        public DismissListener getDismissListener() {
            return dismissListener;
        }

        public Builder setDismissListener(DismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public boolean isCancelable() {
            return cancelable;
        }


        public CancelListener getCancelListener() {
            return cancelListener;
        }

        public Builder setCancelListener(CancelListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public int getAppMainColor() {
            return appMainColor;
        }

        public void setAppMainColor(int appMainColor) {
            this.appMainColor = appMainColor;
        }

        public int getElevator() {
            return elevator;
        }

        public void setElevator(int elevator) {
            this.elevator = elevator;
        }

        public int getMarginLeft() {
            return marginLeft;
        }

        public void setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
        }

        public int getMarginRight() {
            return marginRight;
        }

        public void setMarginRight(int marginRight) {
            this.marginRight = marginRight;
        }

        public XDialog build() {
            XDialog XDialog = new XDialog();
            XDialog.setData(this);
            return XDialog;
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (data != null && data.getDismissListener() != null) {
            data.getDismissListener().onDismiss(this);
        }
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     String negative, NegativeClickListener negativeClickListener, DismissListener dismissListener) {
        showWithParam(null, content, positive, positiveClickListener, negative, negativeClickListener, false,
                dismissListener);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     String negative, boolean cancelAble) {
        showWithParam(null, content, positive, positiveClickListener, negative, null, cancelAble, null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener, String negative, NegativeClickListener negativeClickListener) {
        showWithParam(title, content, positive, positiveClickListener, negative, negativeClickListener, false, null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener, String negative, NegativeClickListener negativeClickListener,
                                     boolean cancelAble) {
        showWithParam(title, content, positive, positiveClickListener, negative, negativeClickListener, cancelAble,
                null);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     DismissListener dismissListener) {
        showWithParam(null, content, positive, positiveClickListener, null, null, false, dismissListener);
    }

    public static void showWithParam(String content, String positive) {
        showWithParam(null, content, positive, null, null, null, false, null);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     String negative) {
        showWithParam(null, content, positive, positiveClickListener, negative, null, true, null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener, DismissListener dismissListener) {
        showWithParam(title, content, positive, positiveClickListener, null, null, false, dismissListener);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener) {
        showWithParam(null, content, positive, positiveClickListener, null, null, true, null);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     boolean cancelable) {
        showWithParam(null, content, positive, positiveClickListener, null, null, cancelable, null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener) {
        showWithParam(title, content, positive, positiveClickListener, null, null, true, null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener, String negative) {
        showWithParam(title, content, positive, positiveClickListener, negative, null, false, null);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     String negative, NegativeClickListener negativeClickListener) {
        showWithParam(null, content, positive, positiveClickListener, negative, negativeClickListener, true, null);
    }

    public static void showWithParam(String content, String positive, PositiveClickListener positiveClickListener,
                                     String negative, NegativeClickListener negativeClickListener, boolean cancelAble) {
        showWithParam(null, content, positive, positiveClickListener, negative, negativeClickListener, cancelAble,
                null);
    }

    public static void showWithParam(String title, String content, String positive,
                                     PositiveClickListener positiveClickListener, String negative, NegativeClickListener negativeClickListener,
                                     boolean cancelable, DismissListener dismissListener) {
        XDialog build = new XDialog.Builder().setTitle(title)
                .setContent(content)
                .setPositiveText(positive)
                .setPositiveClick(positiveClickListener)
                .setNegativeText(negative)
                .setNegativeClick(negativeClickListener)
                .setCancelable(cancelable)
                .setDismissListener(dismissListener)
                .build();
        if (!build.isVisible()) {
            build.show();
        }
    }

    public static boolean isActivityFinishing(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity.isFinishing() || activity.isDestroyed();
        }
        return false;
    }

}

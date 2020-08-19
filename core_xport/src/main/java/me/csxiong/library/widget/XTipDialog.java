package me.csxiong.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.csxiong.library.R;
import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 可定制的提示框
 * @Author : csxiong create on 2019/7/17
 */
public class XTipDialog extends Dialog {

    public XTipDialog(Context context) {
        this(context, R.style.BaseDialog);
    }

    public XTipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }

    /**
     * 生成默认的 {@link XTipDialog}
     * <p>
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 {@link IconType}
     * </p>
     */
    public static class Builder {
        /**
         * 不显示任何icon
         */
        public static final int ICON_TYPE_NOTHING = 0;
        /**
         * 显示 Loading 图标
         */
        public static final int ICON_TYPE_LOADING = 1;
        /**
         * 显示成功图标
         */
        public static final int ICON_TYPE_SUCCESS = 2;
        /**
         * 显示失败图标
         */
        public static final int ICON_TYPE_FAIL = 3;
        /**
         * 显示信息图标
         */
        public static final int ICON_TYPE_INFO = 4;

        @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }

        private @IconType
        int mCurrentIconType = ICON_TYPE_NOTHING;

        private Context mContext;

        private CharSequence mTipWord;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置 icon 显示的内容
         *
         * @see IconType
         */
        public Builder setIconType(@IconType int iconType) {
            mCurrentIconType = iconType;
            return this;
        }

        /**
         * 设置显示的文案
         */
        public Builder setTipWord(CharSequence tipWord) {
            mTipWord = tipWord;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public XTipDialog create() {
            XTipDialog dialog = new XTipDialog(mContext);
            //创建基础View
            FrameLayout flContent = new FrameLayout(mContext);
            LinearLayout linearLayout = new LinearLayout(mContext, null);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setPadding(0, XDisplayUtil.dpToPxInt(18), 0, XDisplayUtil.dpToPxInt(16));
            linearLayout.setBackground(GradientDrawableFactory.createDrawable(0xc0222222, XDisplayUtil.dpToPx(10f)));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(XDisplayUtil.dpToPxInt(150), -2);
            lp.gravity = Gravity.CENTER;
            flContent.addView(linearLayout, lp);
            //设置内容
            dialog.setContentView(flContent);
            switch (mCurrentIconType) {
                case ICON_TYPE_LOADING:
                    XLoadingView loadingView = new XLoadingView(mContext);
                    loadingView.setColor(Color.WHITE);
                    loadingView.setSize(XDisplayUtil.dpToPxInt(32));
                    LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    loadingView.setLayoutParams(loadingViewLP);
                    linearLayout.addView(loadingView);
                    break;
                case ICON_TYPE_SUCCESS:
                case ICON_TYPE_FAIL:
                case ICON_TYPE_INFO:
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(imageViewLP);

                    if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qmui_icon_notify_done));
                    } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qmui_icon_notify_error));
                    } else {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.qmui_icon_notify_info));
                    }

                    linearLayout.addView(imageView);
                    break;
            }

            if (mTipWord != null && mTipWord.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (mCurrentIconType != ICON_TYPE_NOTHING) {
                    tipViewLP.topMargin = XDisplayUtil.dpToPxInt(12);
                }
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(Color.WHITE);
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(mTipWord);

                linearLayout.addView(tipView);
            }
            return dialog;
        }

    }
}

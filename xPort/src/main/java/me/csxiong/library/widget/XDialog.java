package me.csxiong.library.widget;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.csxiong.library.R;
import me.csxiong.library.R2;
import me.csxiong.library.base.SimpleDialog;
import me.csxiong.library.utils.XDisplayUtil;
import me.csxiong.library.utils.XResUtils;
import me.csxiong.library.widget.round.RoundTextView;
import me.csxiong.library.widget.round.RoundViewDelegate;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : simple message and tip dialog for user
 * |  TODO : 增加垂直对话选项
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/3/11 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class XDialog extends SimpleDialog {

    @BindView(R2.id.tv_dialog_title)
    RoundTextView title;

    @BindView(R2.id.content)
    RelativeLayout content;

    @BindView(R2.id.ll_dialog_actions)
    LinearLayout mLlActions;

    public Builder builder;

    public RelativeLayout.LayoutParams getLayoutParams() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        return lp;
    }

    @Override
    public int getDialogStyle() {
        if (builder.style != 0) {
            return builder.style;
        }
        return R.style.dialog_zoom;
    }

    @Override
    public boolean enableCancelOutside() {
        if (builder != null) {
            return builder.cancelOutSide;
        }
        return super.enableCancelOutside();
    }

    public static XDialog.Builder create() {
        return new Builder();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_more_action;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        if (builder.title == null && builder.titleStr == null) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            if (builder.title != null) {
                builder.title.onTitile(title);
            } else {
                title.setText(builder.titleStr);
            }
        }

        //content添加
        if (builder.content == null) {
        } else if (builder.content instanceof Builder.TextContent) {
            RoundTextView tv = new RoundTextView(getActivity());
            RelativeLayout.LayoutParams lp = getLayoutParams();
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(lp);
            ((Builder.TextContent) builder.content).onTextContent(tv);
            content.addView(tv);
        }


        //Actions按钮添加
        //添加Action
        int index = 0;
        for (final Builder.Action action : builder.actionList) {
            index++;
            RoundTextView textView = new RoundTextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lp.gravity = Gravity.CENTER;
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(lp);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.onClick(XDialog.this);
                    dismiss();
                }
            });

            RoundViewDelegate delegate = textView.getDelegate();
            //可配置颜色
            textView.setTextColor(Color.parseColor("#007AFF"));
            textView.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x32)));
            delegate.setBackgroundColor(Color.WHITE);
            delegate.setBackgroundPressColor(Color.parseColor("#dddddd"));
            action.setContent(textView);
            if (index == 1) {
                delegate.setCornerRadius_BL((int) getActivity().getResources().getDimension(R.dimen.x20));
            }
            if (index == builder.actionList.size()) {
                delegate.setCornerRadius_BR((int) getActivity().getResources().getDimension(R.dimen.x20));
            }

            mLlActions.addView(textView);

            if (index != builder.actionList.size()) {
                // 非最后一条添加分割线
                View divideLine = new View(getContext());
                LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
                divideLine.setBackgroundColor(Color.parseColor("#dddddd"));
                divideLine.setLayoutParams(l);
                mLlActions.addView(divideLine);
            }
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * Created by csxiong on 2018/4/27.
     */

    public static class Builder {

        private XDialog dialog;

        private Content content;

        private Title title;

        private String titleStr;

        private int style;

        private boolean cancelOutSide = true;

        private ArrayList<Action> actionList = new ArrayList<>();

        public Builder() {
            dialog = new XDialog();
        }

        public Builder setTitle(Title title) {
            this.title = title;
            return this;
        }

        public Builder setStyle(int style) {
            this.style = style;
            return this;
        }

        public Builder setTitle(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public Builder setContent(Content content) {
            this.content = content;
            return this;
        }

        public Builder setContent(String content) {
            setContent(content, Color.parseColor("#FF333333"));
            return this;
        }

        public Builder setContent(String content, int color) {
            this.content = new TextContent() {
                @Override
                public void onTextContent(RoundTextView tv) {
                    tv.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x32)));
                    tv.setText(content);
                    tv.setTextColor(color);
                }
            };
            return this;
        }

        public Builder cancelOutSide(boolean cancelOutSide) {
            this.cancelOutSide = cancelOutSide;
            return this;
        }

        public Builder addAction(Action action) {
            actionList.add(action);
            return this;
        }

        public Builder addActions(List<Action> actions) {
            actionList.addAll(actions);
            return this;
        }

        public Builder addCancelBtn(final String desStr) {
            actionList.add(new Action() {
                @Override
                public void setContent(TextView tv) {
                    tv.setText(desStr);
                    tv.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x34)));
                    tv.setTextColor(Color.parseColor("#333333"));
                }

                @Override
                public void onClick(XDialog dialog) {

                }
            });
            return this;
        }

        public Builder addSureBtn(final String desStr) {
            actionList.add(new Action() {
                @Override
                public void setContent(TextView tv) {
                    tv.setText(desStr);
                    tv.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x34)));
                    tv.setTextColor(Color.parseColor("#007AFF"));
                }

                @Override
                public void onClick(XDialog dialog) {

                }
            });
            return this;
        }

        public Builder addSureBtn(SureAction sureAction) {
            actionList.add(sureAction);
            return this;
        }

        public Builder addCancelBtn(CancelAction cancelAction) {
            actionList.add(cancelAction);
            return this;
        }

        public XDialog build() {
            dialog.builder = this;
            return dialog;
        }

        public void show() {
            if (ActivityUtils.getTopActivity() instanceof FragmentActivity) {
                build().show(((FragmentActivity) ActivityUtils.getTopActivity()).getSupportFragmentManager(), "");
            }

        }

        public interface Action {

            void setContent(TextView tv);

            void onClick(XDialog dialog);
        }

        public interface Title {
            void onTitile(RoundTextView tv);
        }

        public interface Content {
        }

        public interface TextContent extends Content {
            void onTextContent(RoundTextView tv);
        }

        public interface EditContent extends Content {
            void onEditContent(EditText et);
        }

    }

    public abstract static class SureAction implements Builder.Action {
        @Override
        public void setContent(TextView tv) {
            tv.setText("确定");
            tv.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x34)));
            tv.setTextColor(Color.parseColor("#007AFF"));
        }
    }

    public abstract static class CancelAction implements Builder.Action {
        @Override
        public void setContent(TextView tv) {
            tv.setText("取消");
            tv.setTextSize(XDisplayUtil.pxToDp(XResUtils.getDimenInt(R.dimen.x34)));
            tv.setTextColor(Color.parseColor("#333333"));
        }
    }
}

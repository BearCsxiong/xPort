package me.csxiong.library.widget.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.R;

/**
 * @Desc : 非常简易的TitleBar
 * @Author : Bear - 2020/8/23
 */
public class XTitleBar extends FrameLayout {

    private String title = "";

    private boolean isDivideLine;

    private int mainColor;

    private List<Action> actions = new ArrayList<>();

    private TextView mTvTitle;

    private View mVDivideLine;

    public XTitleBar(Context context) {
        this(context, null);
    }

    public XTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs,
                    R.styleable.XTitleBar);
            title = ta.getString(R.styleable.XTitleBar_xtb_title);
            isDivideLine = ta.getBoolean(R.styleable.XTitleBar_xtb_divide_enable, true);
            mainColor = ta.getColor(R.styleable.XTitleBar_xtb_theme_color, 0xffF25C7F);
            ta.recycle();
        }
        initView();
        initData();
    }

    private void initData() {
        mVDivideLine.setVisibility(isDivideLine ? VISIBLE : GONE);
        mTvTitle.setText(title);
    }

    /**
     * 初始化View
     */
    private void initView() {
        //主title
        mTvTitle = new TextView(getContext());
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mTvTitle.setLayoutParams(new LayoutParams(-2, -2, Gravity.CENTER));
//        mTvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTvTitle.setTextColor(Color.BLACK);
        addView(mTvTitle);

        //分割线
        mVDivideLine = new View(getContext());
        mVDivideLine.setBackgroundColor(0xffcccccc);
        mVDivideLine.setLayoutParams(new LayoutParams(-1, 1, Gravity.BOTTOM));
        addView(mVDivideLine);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //直接在内部适配TitleBar
//        if (StudioSizeUtils.isFullDisplay()) {
//            StatusBarUtil.adjustStatusView(this);
//        }
//        //设置高度
//        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                getViewTreeObserver().removeOnPreDrawListener(this);
//                if (StudioSizeUtils.isFullDisplay()) {
//                    ViewUtils.setHeight(XTitleBar.this, StatusBarUtil.getStatusBarHeight(PomeloApplication.getApplication()) + DeviceUtils.dip2px(48));
//                } else {
//                    ViewUtils.setHeight(XTitleBar.this, DeviceUtils.dip2px(48));
//                }
//                return false;
//            }
//        });
    }

    /**
     * 设置Title
     *
     * @param title
     * @return
     */
    public XTitleBar setTitle(String title) {
        this.title = title;
        if (mTvTitle != null && !TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置分割线是否可用
     *
     * @param isDivideLineEnable
     * @return
     */
    public XTitleBar setDivideLineEnable(boolean isDivideLineEnable) {
        this.isDivideLine = isDivideLineEnable;
        if (mVDivideLine != null) {
            mVDivideLine.setVisibility(isDivideLine ? VISIBLE : GONE);
        }
        return this;
    }

    /**
     * 移除Action中的View
     *
     * @param action
     * @return
     */
    public XTitleBar removeAction(Action action) {
        if (action != null) {
            actions.remove(action);
            View view = action.onCreateAction(getContext());
            if (view != null && view.getParent() == this) {
                removeView(view);
            }
        }
        return this;
    }

    /**
     * 添加Action{@link #addAction(Action, int)}
     *
     * @param action
     */
    public XTitleBar addAction(Action action) {
        return addAction(action, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 添加Action 目前仅提供添加一个左侧或者右侧Action
     *
     * @param action
     */
    public XTitleBar addAction(Action action, int gravity) {
        if (action != null && !actions.contains(action)) {
            View view = action.onCreateAction(getContext());
            if (view != null && view.getParent() == null) {
                actions.add(action);
                view.setOnClickListener(onClickListener);
                LayoutParams lp = new LayoutParams(-2, -2, gravity);
                int[] margins = action.marginLTRB();
                if (margins != null) {
                    lp.setMargins(margins[0], margins[1], margins[2], margins[3]);
                }
                addView(view, lp);
            }
        }
        return this;
    }

    /**
     * 内部Action释放方法
     */
    private OnClickListener onClickListener = v -> {
        for (Action action : actions) {
            View view = action.onCreateAction(getContext());
            if (view == v) {
                action.onActionClick(v);
                return;
            }
        }
    };


}

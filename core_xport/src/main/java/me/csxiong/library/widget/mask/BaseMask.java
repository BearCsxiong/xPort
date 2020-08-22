package me.csxiong.library.widget.mask;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * @Desc : BaskMask
 * @Author : csxiong - 2020-02-13
 */
public abstract class BaseMask implements ViewStub.OnInflateListener {

    /**
     * 布局容器
     */
    private ViewGroup container;

    /**
     * 内容viewStub
     */
    private ViewStub viewStub;

    /**
     * 被渲染View
     */
    private View inflateView;

    /**
     * 布局帮助类
     */
    private MaskContainerHelper maskHelper;

    /**
     * 获取布局ID
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 布局tag
     *
     * @return
     */
    public abstract String getTag();

    /**
     * 创建目标View
     *
     * @param container
     * @param maskContainerHelper
     */
    public void onCreateViewStub(ViewGroup container, MaskContainerHelper maskContainerHelper) {
        //保存parent
        this.container = container;
        this.maskHelper = maskContainerHelper;
        //创建目标ViewStub
        viewStub = new ViewStub(container.getContext(), getLayoutId());
        //对目标创建监听
        viewStub.setOnInflateListener(this);
        //默认不渲染
        viewStub.setVisibility(View.GONE);
        //添加如目标ViewStub
        container.addView(viewStub, -1, -1);
    }

    @Override
    public void onInflate(ViewStub stub, View view) {
        inflateView = view;
        if (maskHelper != null && maskHelper.getMaskMap() != null) {
            MaskActions maskActions = maskHelper.getMaskMap().get(getTag());
            if (maskActions != null) {
                if (maskActions.getOnInflateListener() != null) {
                    maskActions.getOnInflateListener().onInflate(view);
                }
                for (MaskAction action : maskActions.getMaskActions()) {
                    View _view = view.findViewById(action.getId());
                    if (_view != null && maskHelper.getOnClickListener() != null) {
                        _view.setOnClickListener(maskHelper.getOnClickListener());
                    }
                }
            }
        }
    }

    public ViewStub getViewStub() {
        return viewStub;
    }

    public View getView() {
        return inflateView;
    }

    public boolean isInLayout() {
        return viewStub.isInLayout();
    }
}

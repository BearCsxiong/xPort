package me.csxiong.library.widget.mask;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc : 数据蒙版帮助类
 * @Author : csxiong - 2020-02-13
 */
public class MaskContainerHelper {

    private Map<String, MaskActions> maskMap;

    private View.OnClickListener onClickListener;

    public MaskContainerHelper() {
    }

    private Builder builder;

    public Builder newBuilder() {
        if (builder == null) {
            builder = new Builder(this);
        }
        if (maskMap != null) {
            maskMap.clear();
        }
        if (onClickListener != null) {
            onClickListener = null;
        }
        return builder;
    }

    public static class Builder {

        private MaskContainerHelper maskContainerHelper;

        public Builder(MaskContainerHelper helper) {
            maskContainerHelper = helper;
            maskContainerHelper.maskMap = new HashMap<>();
        }

        public Builder bindView(String tag, int resId) {
            MaskActions maskActions = maskContainerHelper.maskMap.get(tag);
            if (maskActions == null) {
                MaskActions _maskActions = new MaskActions(new ArrayList<>());
                _maskActions.getMaskActions().add(new MaskAction(resId, tag));
                maskContainerHelper.maskMap.put(tag, _maskActions);
            } else {
                maskActions.getMaskActions().add(new MaskAction(resId, tag));
            }
            return this;
        }

        public Builder setOnInflateViewListener(String tag, OnInflateListener listener) {
            MaskActions maskActions = maskContainerHelper.maskMap.get(tag);
            if (maskActions == null) {
                MaskActions _maskActions = new MaskActions(new ArrayList<>());
                _maskActions.setOnInflateListener(listener);
                maskContainerHelper.maskMap.put(tag, _maskActions);
            } else {
                maskActions.setOnInflateListener(listener);
            }
            return this;
        }

        public Builder bindClickListener(View.OnClickListener listener) {
            maskContainerHelper.onClickListener = listener;
            return this;
        }

        public MaskContainerHelper build() {
            return maskContainerHelper;
        }
    }

    public Map<String, MaskActions> getMaskMap() {
        return maskMap;
    }

    public void setMaskMap(Map<String, MaskActions> maskMap) {
        this.maskMap = maskMap;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

package me.csxiong.library.widget.mask;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc : 数据蒙版
 * @Author : csxiong - 2020-02-13
 */
public class MaskContainer extends RelativeLayout {

    /**
     * 蒙版
     */
    private HashMap<String, BaseMask> masks = new HashMap<String, BaseMask>();

    /**
     * 当前Mask类型
     */
    private String currentMaskType;

    /**
     * 数据蒙版帮助类
     */
    private MaskContainerHelper maskContainerHelper;

    public MaskContainer(@NonNull Context context) {
        super(context);
    }

    public MaskContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    /**
     * 获取数据蒙版帮助类
     * 主要添加部分蒙版内部的
     * 1.监听注册
     * 2.事件注册
     *
     * @return
     */
    public MaskContainerHelper getMaskContainerHelper() {
        if (maskContainerHelper == null) {
            maskContainerHelper = new MaskContainerHelper();
        }
        return maskContainerHelper;
    }

    /**
     * 初始化
     */
    private void init() {
        //添加所有默认的Masks
        if (masksDelegate != null) {
            masksDelegate.onCreateMasks(masks);
        }
    }

    /**
     * 获取Mask
     *
     * @param tag 目标tag的Mask
     * @return
     */
    public BaseMask getMask(String tag) {
        return masks.get(tag);
    }

    /**
     * 仅显示目标tag Mask
     *
     * @param tag
     */
    public void showMaskOnly(String tag) {
        for (String _tag : masks.keySet()) {
            //默认立即隐藏
            hideMask(_tag);
        }
        showMask(tag);
    }

    /**
     * 显示目标Mask
     *
     * @param tag
     */
    public void showMask(String tag) {
        currentMaskType = tag;
        BaseMask mask = masks.get(tag);
        if (mask != null) {
            int index = indexOfChild(mask.getViewStub());
            if (index < 0) {
                mask.onCreateViewStub(this, maskContainerHelper);
            }
            mask.getViewStub().setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏目标Mask
     *
     * @param tag
     */
    public void hideMask(String tag) {
        currentMaskType = null;
        BaseMask mask = masks.get(tag);
        if (mask != null && mask.getViewStub() != null) {
            int index = indexOfChild(mask.getView());
            if (index >= 0) {
                mask.getViewStub().setVisibility(GONE);
            }
        }
    }

    /**
     * 隐藏所有Mask
     */
    public void hideAll() {
        for (String _tag : masks.keySet()) {
            //默认立即隐藏
            hideMask(_tag);
        }
    }

    public String getCurrentMaskType() {
        return currentMaskType;
    }

    private static MasksDelegate masksDelegate;

    public interface MasksDelegate {

        void onCreateMasks(Map<String, BaseMask> masks);
    }

    public static void setMasksDelegate(MasksDelegate masksDelegate) {
        MaskContainer.masksDelegate = masksDelegate;
    }
}

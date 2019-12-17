package me.csxiong.camera;

import android.content.Context;
import android.graphics.RectF;
import android.view.View;

/**
 * @Desc : 所有的Cover
 * @Author : csxiong - 2019-11-22
 */
public interface ICover {

    /**
     * 获取主View
     *
     * @return
     */
    View getRootView();

    /**
     * 在和组挂钩时
     *
     * @param context
     * @param iCoverGroup
     */
    void onAttachGroup(Context context, ICoverGroup iCoverGroup);

    /**
     * 在和组解绑
     */
    void onDetachGroup();

    /**
     * Cover事件回调
     *
     * @param eventCode
     * @param datas
     */
    void onCoverEvent(int eventCode, Object... datas);

    /**
     * 在相机预览尺寸变化
     *
     * @param rectF 相机当前的预览尺寸
     */
    void onCameraRectChange(RectF rectF);

    /**
     * 相机事件回调
     *
     * @param cameraEventCode
     */
    void onCameraEvent(int cameraEventCode);

}

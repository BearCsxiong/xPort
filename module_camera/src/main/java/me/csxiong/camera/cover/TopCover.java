package me.csxiong.camera.cover;

import android.graphics.RectF;

import me.csxiong.camera.BaseCover;


/**
 * @Desc : 顶部状态栏Cover
 * @Author : csxiong - 2019-11-22
 */
public class TopCover extends BaseCover {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onCoverEvent(int eventCode, Object... datas) {

    }

    @Override
    public void onCameraRectChange(RectF rectF) {

    }

    @Override
    public void onCameraEvent(int cameraEventCode) {

    }
}

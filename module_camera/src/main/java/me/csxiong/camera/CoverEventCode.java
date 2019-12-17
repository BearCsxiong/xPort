package me.csxiong.camera;

/**
 * @Desc : Cover事件码 发送到Cover内部的消息
 * @Author : csxiong - 2019-11-22
 */
public interface CoverEventCode {

    interface CameraEventCode {
        /**
         * 在相机开始预览前
         */
        int CAMERA_BEFORE_START_PREVIEW = 0;
        /**
         * 在相机开始预览后
         */
        int CAMERA_AFTER_START_PREVIEW = 1;
        /**
         * 在相机截止预览前
         */
        int CAMERA_BEFORE_END_PREVIEW = 2;
        /**
         * 在相机截止预览后
         */
        int CAMERA_AFTER_END_PREVIEW = 3;
        /**
         * 在相机拍照开始前
         */
        int CAMERA_BEFORE_START_TAKING_CAPTURE = 4;
        /**
         * 在相机拍照开始后
         */
        int CAMERA_AFTER_START_TAKING_CAPTURE = 5;
        /**
         * 在相机拍照结束前
         */
        int CAMERA_BEFORE_END_TAKING_CAPTURE = 6;
        /**
         * 在相机拍照结束后
         */
        int CAMERA_AFTER_END_TAKING_CAPTURE = 7;
        /**
         * 相机拍照结果
         */
        int CAMERA_CAPTURE_RESULT = 8;
    }

    interface DataCode {
        /**
         * 是否在预览
         */
        int IS_IN_PREVIEW = 0;
        /**
         * 是否在拍照中
         */
        int IS_IN_TAKING_CAPTURE = 1;
    }

}

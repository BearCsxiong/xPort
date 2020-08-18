package me.csxiong.library.base;

import androidx.annotation.IntRange;

/**
 * @Desc : 部分抽象View操作
 * @Author : csxiong create on 2019/8/21
 */
public interface IView {

    /**
     * 开始Loading
     *
     * @param msg loading描述
     */
    void startLoading(String msg);

    /**
     * 停止Loading
     */
    void stopLoading();

    /**
     * 开始进度
     *
     * @param progress 进度
     * @param des      描述
     */
    void startProcessing(@IntRange(from = 0, to = 100) int progress, String des);

    /**
     * 停止进度
     */
    void stopProcessing();

}

package me.csxiong.library.base;

/**
 * @Desc : 基本View的操作
 * @Author : csxiong create on 2019/7/22
 */
public interface IView {

    /**
     * 获取布局ID
     *
     * @return
     */
    int getLayoutId();

    /**
     * 初始化View
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

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
    void startProcessing(int progress, String des);

    /**
     * 停止进度
     */
    void stopProcessing();

}

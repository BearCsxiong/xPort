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
}

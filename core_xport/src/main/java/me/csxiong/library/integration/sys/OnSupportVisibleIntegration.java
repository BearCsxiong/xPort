package me.csxiong.library.integration.sys;

/**
 * @Desc : Fragment支持的集成接口
 * @Author : csxiong - 2020-03-12
 */
public interface OnSupportVisibleIntegration {

    /**
     * 可见状态
     */
    void onSupportVisible();

    /**
     * 不可见状态
     */
    void onSupportInvisible();

    /**
     * 是否可见
     *
     * @return
     */
    boolean isSupportVisible();

    /**
     * 获取Fragment可见检测类
     *
     * @return
     */
    FragmentSupportVisibleDetector getFragmentSupportVisibleHelper();
}

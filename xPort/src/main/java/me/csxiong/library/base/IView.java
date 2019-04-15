package me.csxiong.library.base;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : default view behavior
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public interface IView {

    /**
     * default loading with msg
     *
     * @param loadingMsg
     */
    void startLoading(String loadingMsg);

    /**
     * stop loading
     */
    void stopLoading();

}

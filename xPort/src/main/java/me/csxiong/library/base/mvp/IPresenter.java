package me.csxiong.library.base.mvp;

import me.csxiong.library.base.IView;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : default presenter behavior
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public interface IPresenter<T extends IView> {

    /**
     * bind target view
     *
     * @param view
     */
    void attachView(T view);

    /**
     * unbind target view
     */
    void detachView();
}

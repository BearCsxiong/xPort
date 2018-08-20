package me.csxiong.library.base;

/**-------------------------------------------------------------------------------
*|
*| desc : MVP模式基础Presenter待实现方法
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong
*|--------------------------------------------------------------------------------
*/
public interface IPresenter<T extends IView>{

    void attachView(T view);

    void detachView();
}

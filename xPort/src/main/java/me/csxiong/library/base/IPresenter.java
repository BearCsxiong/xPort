package me.csxiong.library.base;

/**
 * Presenter基类
 */
public interface IPresenter<T extends IView>{

    void attachView(T view);

    void detachView();
}

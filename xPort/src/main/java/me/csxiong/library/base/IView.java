package me.csxiong.library.base;

public interface IView {

    void showErrorTip(String errorMsg);

    void showWarnTip(String warnTip);

    void showSuccessTip(String successTip);

    void dismissTip();

    void startWaiting();

    void stopWaiting();

}

package me.csxiong.library.base;

/**-------------------------------------------------------------------------------
*| 
*| desc : MVP模式基础View待实现的方法
*| 
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong 
*|--------------------------------------------------------------------------------
*/
public interface IView {

    void showErrorTip(String errorMsg);

    void showWarnTip(String warnTip);

    void showSuccessTip(String successTip);

    void dismissTip();

    void startWaiting();

    void stopWaiting();

}

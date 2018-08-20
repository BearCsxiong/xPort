package me.csxiong.library.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.csxiong.library.base.IPage;


/**-------------------------------------------------------------------------------
*|
*| desc : Activity生命周期实现部分回调
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong
*|--------------------------------------------------------------------------------
*/
public class ActivityDelegateImpl implements IActivityDelegate {
    private IPage page;

    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.page = (IPage) activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroy() {

    }
}

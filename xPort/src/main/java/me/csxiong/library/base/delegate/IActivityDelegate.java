package me.csxiong.library.base.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**-------------------------------------------------------------------------------
*|
*| desc : Activity生命周期回调
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong
*|--------------------------------------------------------------------------------
*/
public interface IActivityDelegate {
    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onDestroy();
}

package me.csxiong.library.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import me.csxiong.library.base.IPage;

/**-------------------------------------------------------------------------------
*| 
*| desc : Fragment实现部分回调内容
*| 
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong 
*|--------------------------------------------------------------------------------
*/
public class FragmentDelegateImpl implements IFragmentDelegate {
    private android.support.v4.app.FragmentManager mFragmentManager;
    private android.support.v4.app.Fragment mFragment;
    private IPage page;


    public FragmentDelegateImpl(@NonNull android.support.v4.app.FragmentManager fragmentManager, @NonNull android.support.v4.app.Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.page = (IPage) fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(@Nullable View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {

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
    public void onDestroyView() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDetach() {

    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}

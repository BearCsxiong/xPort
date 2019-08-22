package me.csxiong.library.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import dagger.internal.Preconditions;

/**
 * @Desc : 适用于Dagger创建ViewModel的方式
 * @Author : csxiong create on 2019/8/22
 */
public class DaggerViewModelProviders {

    /**
     * 提供dagger创建的方法
     *
     * @param activity
     * @return
     */
    public static DaggerViewModelStore of(FragmentActivity activity) {
        Preconditions.checkNotNull(activity);
        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm.isDestroyed()) {
            throw new RuntimeException("intent to create viewModel on destory");
        }
        return DaggerViewModelHolderFragment.findDaggerViewModelStore(fm);
    }

    /**
     * 提供dagger创建的方法
     *
     * @param fragment
     * @return
     */
    public static DaggerViewModelStore of(Fragment fragment) {
        Preconditions.checkNotNull(fragment);
        FragmentManager fm = fragment.getChildFragmentManager();
        if (fm.isDestroyed()) {
            throw new RuntimeException("intent to create viewModel on destory");
        }
        return DaggerViewModelHolderFragment.findDaggerViewModelStore(fm);
    }
}

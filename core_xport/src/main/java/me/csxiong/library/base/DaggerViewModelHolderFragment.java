package me.csxiong.library.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import dagger.internal.Preconditions;

/**
 * @Desc : 提供ViewModel的提供，使用同{@link android.arch.lifecycle.ViewModelProviders}类似
 * 替代{@link android.arch.lifecycle.ViewModelProviders}
 * 实质实现同{@link android.arch.lifecycle.HolderFragment}和{@link android.arch.lifecycle.HolderFragment.HolderFragmentManager}
 * @Author : csxiong create on 2019/8/22
 */
public class DaggerViewModelHolderFragment extends Fragment {

    private static String HolderFragmentTag = "DaggerViewModelHolderFragment";

    /**
     * 实现一个由dagger提供创建的ViewModel仓库
     */
    private DaggerViewModelStore daggerViewModelStore = new DaggerViewModelStore();

    public DaggerViewModelStore getDaggerViewModelStore() {
        return daggerViewModelStore;
    }

    public static DaggerViewModelStore findDaggerViewModelStore(FragmentManager fragmentManager) {
        Preconditions.checkNotNull(fragmentManager);
        Fragment fg = fragmentManager.findFragmentByTag(HolderFragmentTag);
        if (fg == null) {
            fg = new DaggerViewModelHolderFragment();
            fragmentManager.beginTransaction().add(fg, HolderFragmentTag)
                    .commitNowAllowingStateLoss();
        }
        return ((DaggerViewModelHolderFragment) fg).getDaggerViewModelStore();
    }

}

package com.base.project.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.base.project.R;
import com.base.project.databinding.FragmentTestBinding;

import me.csxiong.library.base.BaseFragment;

public class FragmentGroup extends BaseFragment<FragmentTestBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {
        mViewBinding.vp.setAdapter(new VPAdapter(getChildFragmentManager()));
        mViewBinding.vp.setOffscreenPageLimit(20 - 1);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSupportVisible() {
        Log.e("Visible", "visible" + getTag());
    }

    @Override
    public void onSupportInvisible() {
        Log.e("Visible", "invisible" + getTag());
    }

    public class VPAdapter extends FragmentPagerAdapter {

        public VPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fg;
            if (i == 1) {
                fg = new FragmentChild2();
            } else {
                fg = new FragmentChild();
            }
            Bundle bd = new Bundle();
            bd.putInt("Position", i);
            fg.setArguments(bd);
            return fg;
        }

        @Override
        public int getCount() {
            return 20;
        }
    }
}

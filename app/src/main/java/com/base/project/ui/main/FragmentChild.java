package com.base.project.ui.main;

import android.util.Log;

import com.base.project.R;
import com.base.project.databinding.FragmentTestBinding;

import me.csxiong.library.base.BaseFragment;

public class FragmentChild extends BaseFragment<FragmentTestBinding> {

    public int position = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {
        position = getArguments().getInt("Position");
        mViewBinding.tv.setText(position + "");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSupportVisible() {
        Log.e("Visible", "visible " + position);
    }

    @Override
    public void onSupportInvisible() {
        Log.e("Visible", "invisible " + position);
    }
}

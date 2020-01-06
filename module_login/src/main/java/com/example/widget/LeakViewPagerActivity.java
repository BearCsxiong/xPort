package com.example.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.login.R;
import com.example.login.databinding.ActivityViewpagerBinding;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.base.BaseActivity;

/**
 * @Desc : 泄漏界面检测
 * @Author : csxiong - 2020-01-03
 */
@Route(path = "/login/leakViewPager", name = "ViewPager泄漏检测界面")
public class LeakViewPagerActivity extends BaseActivity<ActivityViewpagerBinding> {

    private List<String> titles = new ArrayList<>();

    private List<Fragment> fgs = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_viewpager;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        titles.add("1");
        titles.add("2");
        titles.add("3");
        titles.add("4");
        titles.add("5");
        titles.add("6");
        titles.add("7");
        titles.add("8");
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());
        fgs.add(new EmptyFragment());

        mViewBinding.ftb.setup(this,getSupportFragmentManager());
        mViewBinding.vp.setAdapter(new Adapter(getSupportFragmentManager()));
    }

    class Adapter extends FragmentStatePagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new EmptyFragment();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}

package me.csxiong.camera.ui.photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.camera.album.ImageEntity;

/**
 * @Desc : 图片Pager
 * @Author : csxiong - 2020-01-31
 */
public class ImagePagerAdpter extends FragmentPagerAdapter {

    private List<ImageEntity> data = new ArrayList<>();

    public ImagePagerAdpter(FragmentManager fm) {
        super(fm);
    }

    public void updateData(List<ImageEntity> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        PhotoFragment fg = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        fg.setArguments(bundle);
        return fg;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}

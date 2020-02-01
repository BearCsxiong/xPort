package me.csxiong.library.integration.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @Desc : 简便的ViewPager包装适配器
 * @Author : csxiong - 2020-02-02
 */
public abstract class XPagerAdapter<T> extends PagerAdapter {

    /**
     * 缓存View队列
     */
    private LinkedList<View> cacheViews = new LinkedList<>();

    /**
     * 数据队列
     */
    public List<T> dataList;

    /**
     * 数据更新方法
     *
     * @param dataList
     */
    public void updateDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if (cacheViews.isEmpty()) {
            view = LayoutInflater.from(container.getContext()).inflate(getLayoutId(), container, false);
        } else {
            view = cacheViews.remove();
        }
        view.setTag(dataList.get(position));
        container.addView(view);
        onBindView(view, position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
            cacheViews.add((View) object);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof View && dataList != null) {
            View itemView = (View) object;
            int itemPosition = dataList.indexOf(itemView.getTag());
            if (itemPosition >= 0 && itemView.getTag() == dataList.get(itemPosition)) {
                return itemPosition;
            }
        }
        return POSITION_NONE;
    }

    /**
     * 目标ID
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 绑定目标View
     *
     * @param view
     * @param position
     */
    public abstract void onBindView(View view, int position);
}

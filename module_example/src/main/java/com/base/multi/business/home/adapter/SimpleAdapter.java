package com.base.multi.business.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import me.csxiong.library.base.APP;

/**
 * Created by chensx on 2018/9/13.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter {

    private int itemLayoutId;

    private LayoutInflater inflater;

    public List<T> mDataList;

    public SimpleAdapter(int itemLayoutId, List<T> list) {
        this.itemLayoutId = itemLayoutId;
        this.inflater = LayoutInflater.from(APP.getInstance());
        this.mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SimpleAdapterHelper helper = null;
        T t = mDataList.get(position);
        if (view == null) {
            view = inflateView(viewGroup,getInflateLayoutId(position));
            helper = new SimpleAdapterHelper(view, onItemChildClickListener);
            view.setTag(helper);
        } else {
            helper = (SimpleAdapterHelper) view.getTag();
        }
        helper.curPosition = position;
        convert(helper, t, position);
        return view;
    }

    protected int getInflateLayoutId(int position) {
        return itemLayoutId;
    }

    protected View inflateView(ViewGroup viewGroup,int itemLayoutId) {
        return inflater.inflate(itemLayoutId, viewGroup, false);
    }

    public abstract void convert(SimpleAdapterHelper helper, T t, int position);

    private OnItemChildClickListener onItemChildClickListener;

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

}

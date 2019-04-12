package com.base.multi.business.home.adapter;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by chensx on 2018/9/13.
 */

public abstract class SimpleMultiAdapter<T extends MultiData> extends SimpleAdapter<T> {

    public Map<Integer, Integer> types = new WeakHashMap<>();

    private void addItemType(int type, int itemLayoutId) {
        types.put(type, itemLayoutId);
    }

    public SimpleMultiAdapter(int itemLayoutId, List<T> list) {
        super(itemLayoutId, list);
    }

    @Override
    protected int getInflateLayoutId(int position) {
        return types.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return types.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();
    }

}

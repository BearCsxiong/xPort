package com.base.project.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.base.project.R;
import com.base.project.databinding.ItemFeatureBinding;

import java.util.List;

import me.csxiong.library.integration.adapter.XItem;
import me.csxiong.library.integration.adapter.XViewHolder;

/**
 * @Desc : 功能点的ViewHolder
 * @Author : csxiong - 2019-11-13
 */
public class FeatureViewHolder extends XViewHolder<FeatureBean> {

    private ItemFeatureBinding mViewBinding;

    public FeatureViewHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.item_feature);
        mViewBinding = DataBindingUtil.bind(itemView);
    }

    @Override
    public void onBindViewHolder(int position, XItem<FeatureBean> item, List<Object> payloads) {
        super.onBindViewHolder(position, item, payloads);
        mViewBinding.tv.setText(item.getEntity().getName());
        addOnChildClickListener(mViewBinding.tv);
    }
}

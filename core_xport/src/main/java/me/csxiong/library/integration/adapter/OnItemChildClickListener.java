package me.csxiong.library.integration.adapter;

import android.view.View;

/**
 * @Desc : 内部子控件点击事件
 * @Author : csxiong - 2019-11-13
 */
public interface OnItemChildClickListener {

    void onItemChildClick(int position, XItem<? extends Object> item, View view);

}
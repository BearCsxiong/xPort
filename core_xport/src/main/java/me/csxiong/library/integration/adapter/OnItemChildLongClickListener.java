package me.csxiong.library.integration.adapter;

import android.view.View;

/**
 * @Desc : 内部长点击事件
 * @Author : csxiong - 2019-11-13
 */
public interface OnItemChildLongClickListener<T> {

    void onItemChildLongClick(int position, XItem<T> item, View view);

}
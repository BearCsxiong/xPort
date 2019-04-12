package com.base.multi.business.home.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.WeakHashMap;

import me.csxiong.library.base.APP;

/**
 * Created by chensx on 2018/9/13.
 */

public class SimpleAdapterHelper implements View.OnClickListener {
    Map<Integer, View> views = new WeakHashMap<>();

    public View rootView;

    protected int curPosition;

    public SimpleAdapterHelper(View rootView, OnItemChildClickListener onItemChildClickListener) {
        this.rootView = rootView;
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setText(int viewId, int tag) {
        setText(viewId, tag + "");
    }

    public void setText(int viewId, String text) {
        TextView tv = (TextView) views.get(viewId);
        if (tv == null) {
            tv = rootView.findViewById(viewId);
            views.put(viewId, tv);
        }
        tv.setText(text);
    }

    public void setImageResources(int viewId, int drawableId) {
        Drawable drawable = APP.getInstance().getDrawable(drawableId);
        setImageDrawable(viewId, drawable);
    }

    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = (ImageView) views.get(viewId);
        if (iv == null) {
            iv = rootView.findViewById(viewId);
            views.put(viewId, iv);
        }
        iv.setImageDrawable(drawable);
    }

    public View getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = rootView.findViewById(viewId);
            views.put(viewId, view);
        }
        return view;
    }

    public void bindChildClick(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = rootView.findViewById(viewId);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (!view.hasOnClickListeners()) {
                view.setOnClickListener(this);
            }
        } else {
            Boolean isBind = (boolean) view.getTag(99);
            if (isBind == null || !isBind) {
                view.setTag(99, true);
                view.setOnClickListener(this);
            }
        }
    }

    private OnItemChildClickListener onItemChildClickListener;

    @Override
    public void onClick(View view) {
        if (onItemChildClickListener != null) {
            onItemChildClickListener.onItemChildClick(this, view, curPosition);
        }
    }
}

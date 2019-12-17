package me.csxiong.library.integration.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Desc :
 * @Author : csxiong - 2019-11-13
 */
public abstract class XViewHolder<K extends ViewDataBinding, T> extends RecyclerView.ViewHolder {

    public interface OnItemClickListener {

        boolean onClick(int position, XItem item);
    }

    /**
     * 监听。
     */
    private OnItemClickListener itemClickListener;
    /**
     * 对应的数据Item。
     */
    private XItem<T> item;
    /**
     * 界面ViewBinding
     */
    public K mViewBinding;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 适配器。
     */
    protected XRecyclerViewAdapter adapter;

    public XViewHolder(Context context, ViewGroup parent) {
        this(context, parent, 0);
    }

    /**
     * 子类必须要调用的构造方法。
     *
     * @param context
     * @param parent
     * @param layoutId 当前ViewHolder使用的布局。
     */
    protected XViewHolder(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        super(inflateView(context, parent, layoutId));
        mViewBinding = DataBindingUtil.bind(itemView);
        mContext = context;
        // 创建的时候需要设置监听。
        itemView.setOnClickListener(v -> {
            if (!onItemClick(getAdapterPosition(), item) && itemClickListener != null) {
                itemClickListener.onClick(getAdapterPosition(), item);
            }
        });
    }

    private View.OnClickListener onChildClickListener;

    private View.OnLongClickListener onLongClickListener;

    /**
     * 添加子控件点击
     *
     * @param childView
     */
    public void addOnChildClickListener(@NonNull View childView) {
        if (onChildClickListener == null) {
            onChildClickListener = v -> {
                if (getAdapter() != null && getAdapter().getOnItemChildClickListener() != null) {
                    getAdapter().getOnItemChildClickListener().onItemChildClick(getAdapterPosition(), item, v);
                }
            };
        }
        childView.setOnClickListener(onChildClickListener);
    }

    /**
     * 添加子控件长点击
     *
     * @param childView
     */
    public void addOnChildLongClickListener(View childView) {
        if (onLongClickListener == null) {
            onLongClickListener = v -> {
                if (getAdapter() != null && getAdapter().getOnItemChildLongClickListener() != null) {
                    getAdapter().getOnItemChildLongClickListener().onItemChildLongClick(getAdapterPosition(), item, v);
                }
                return false;
            };
        }
        childView.setOnLongClickListener(onLongClickListener);
    }

    public <T extends XRecyclerViewAdapter> T getAdapter() {
        return (T) adapter;
    }

    public void setAdapter(XRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void onBindViewHolder(int position, XItem<T> item, List<Object> payloads) {
        this.item = item;
    }


    /**
     * 获取当前数据源。
     *
     * @return
     */
    public XItem<T> getItem() {
        return item;
    }

    /**
     * 类中自己实现的点击监听，方便一个特殊操作。
     *
     * @param position
     * @param entity
     * @return
     */
    protected boolean onItemClick(int position, XItem<T> entity) {
        return false;
    }

    /**
     * 设置点击监听。
     *
     * @param itemClickListener
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 工具方法，解析布局。
     *
     * @param context
     * @param parent
     * @param res
     * @return
     */
    public static View inflateView(Context context, ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(context).inflate(res, parent, false);
    }
}

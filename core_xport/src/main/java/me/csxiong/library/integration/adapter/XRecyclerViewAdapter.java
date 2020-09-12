package me.csxiong.library.integration.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import me.csxiong.library.utils.CollectionUtils;

/**
 * @Desc : 基础适配器
 * @Author : Bear - 2020/8/18
 */
public class XRecyclerViewAdapter extends RecyclerView.Adapter<XViewHolder> {
    /**
     * Payload产生，通知变换。
     */
    public static final String PAYLOAD_CONTENT_CHANGE = "CONTENT_CHANGE";
    /**
     * 上下文。
     */
    protected Context mContext;
    /**
     * 数据源。
     */
    protected List<? extends XItem> mItems;
    /**
     * itemType与holder的映射
     */
    private SparseArray<Class<? extends XViewHolder>> typeHolderArray = new SparseArray<>();

    /**
     * Item的点击事件。
     *
     * @param <T>
     */
    public interface OnEntityClickListener<T> {
        boolean onClick(int position, T entity);
    }

    /**
     * 点击事件map
     */
    private HashMap<Class, OnEntityClickListener> mClickListenerMap = new HashMap<>(16);
    /**
     * 当前选中的Item。
     */
    private XItem currentSelectItem;
    /**
     * 附属的RV。
     */
    protected RecyclerView mAttachRecyclerView;

    public XRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Deprecated
    public <T> void setOnEntityClickListener(OnEntityClickListener<T> listener, Class<T> tClass) {
        mClickListenerMap.put(tClass, listener);
    }

    public <T> void setOnEntityClickListener(Class<T> tClass, OnEntityClickListener<T> listener) {
        mClickListenerMap.put(tClass, listener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachRecyclerView = recyclerView;
    }

    public <T, E extends Class<? extends XViewHolder>> void setSingleItemEntities(List<T> itemEntities,
                                                                                  E holderClass) {
        setSingleItemEntities(itemEntities, holderClass, false);
    }

    /**
     * 设置单一数据源。
     *
     * @param itemEntities 数据源。
     * @param holderClass  绑定的holderClass
     * @param selectable   是否是可选中的。
     * @param <T>          实体类型。
     */
    public <T, E extends Class<? extends XViewHolder>> void setSingleItemEntities(List<T> itemEntities,
                                                                                  E holderClass, boolean selectable) {
        List<XItem> items = new LinkedList<>();
        if (itemEntities != null) {
            for (T entity : itemEntities) {
                XItem<T> item = new XItem<>(entity);
                item.setSelectable(selectable);
                item.setViewHolderClass(holderClass);
                // 缓存itemType与holderClass的对应关系。
                if (typeHolderArray.get(item.getItemType()) == null) {
                    typeHolderArray.put(item.getItemType(), holderClass);
                }
                items.add(item);
            }
        }
        updateItemEntities(items, false);
    }

    /**
     * 设置数据源（含动画）。
     *
     * @param items
     */
    public void updateItemEntities(List<? extends XItem> items) {
        updateItemEntities(items, true);
    }

    /**
     * 设置数据源。
     *
     * @param items
     * @param withAnim
     */
    public void updateItemEntities(List<? extends XItem> items, boolean withAnim) {
        if (items != null) {
            for (XItem item : items) {
                if (typeHolderArray.get(item.getItemType()) == null) {
                    // 记忆ItemType与Holder的对应关系。
                    Class<? extends XViewHolder> holderClass = item.getViewHolderClass();
                    if (typeHolderArray.get(item.getItemType()) == null) {
                        typeHolderArray.put(item.getItemType(), holderClass);
                    }
                }
            }
        }
        refreshData(items, withAnim);
    }

    /**
     * 刷新列表。
     *
     * @param animation
     */
    public void refreshData(List<? extends XItem> items, boolean animation) {
        if (animation) {
            DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItems == null ? 0 : mItems.size();
                }

                @Override
                public int getNewListSize() {
                    return items != null ? items.size() : 0;
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mItems.get(oldItemPosition).equals(items.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    return PAYLOAD_CONTENT_CHANGE;
                }
            }, false).dispatchUpdatesTo(this);
        } else {
            notifyDataSetChanged();
        }
        mItems = items;
    }

    /**
     * 设置当前选中的item。
     *
     * @param currentSelectItem
     */
    private void setCurrentSelectItem(XItem currentSelectItem) {
        setCurrentSelectItem(currentSelectItem, true);
    }

    /**
     * 设置当前选中的item。
     *
     * @param currentSelectItem
     * @param isRefresh         是否刷新
     */
    private void setCurrentSelectItem(XItem currentSelectItem, boolean isRefresh) {
        if (currentSelectItem != this.currentSelectItem) {
            if (this.currentSelectItem != null) {
                // 选中要切换。
                this.currentSelectItem.setSelect(false);
                if (isRefresh) {
                    notifyItemChanged(this.currentSelectItem);
                }
            }
            if (currentSelectItem != null) {
                currentSelectItem.setSelect(true);
                if (isRefresh) {
                    notifyItemChanged(currentSelectItem);
                }
            }
            this.currentSelectItem = currentSelectItem;
        }
    }

    /**
     * 设置当前选中的实体。
     *
     * @param entity
     */
    public void setCurrentSelectEntity(Object entity) {
        setCurrentSelectEntity(entity, true);
    }

    /**
     * 设置当前选中的实体
     *
     * @param entity
     * @param isRefresh 是否刷新数据
     */
    public void setCurrentSelectEntity(Object entity, boolean isRefresh) {
        if (CollectionUtils.isEmpty(mItems)) {
            return;
        }
        if (entity != null) {
            for (XItem item : mItems) {
                if (entity.equals(item.getEntity())) {
                    setCurrentSelectItem(item, isRefresh);
                    return;
                }
            }
        } else {
            setCurrentSelectItem(null, isRefresh);
        }
    }

    /**
     * 刷新一个Item。
     *
     * @param item
     */
    public void notifyItemChanged(XItem item) {
        notifyItemChanged(item, PAYLOAD_CONTENT_CHANGE);
    }

    /**
     * 刷新一个Item，自定义payloads。
     *
     * @param currentSelectEntity
     */
    public void notifyItemChanged(XItem currentSelectEntity, Object payloads) {
        if (mItems == null) {
            return;
        }
        int position = mItems.indexOf(currentSelectEntity);
        if (position > -1) {
            notifyItemChanged(position, payloads);
        }
    }

    /**
     * 根据entity刷新一个Item。
     *
     * @param entity
     * @param payloads
     */
    public void notifyItemChanged(Object entity, Object payloads) {
        if (CollectionUtils.isEmpty(mItems)) {
            return;
        }
        for (XItem item : mItems) {
            if (entity.equals(item.getEntity())) {
                notifyItemChanged(item, payloads);
                break;
            }
        }
    }

    /**
     * 通知所有Item内容变更。
     */
    public void notifyAllItemChanged() {
        notifyAllItemChanged(PAYLOAD_CONTENT_CHANGE);
    }

    /**
     * 通知所有Item内容变更。
     */
    public void notifyAllItemChanged(Object payload) {
        notifyItemRangeChanged(0, getItemCount(), payload);
    }

    /**
     * 滚动到对应的实体。
     *
     * @param entity
     */
    public void scrollToEntity(@NonNull Object entity) {
        if (mItems == null || mAttachRecyclerView == null) {
            return;
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (entity.equals(mItems.get(i).entity)) {
                mAttachRecyclerView.scrollToPosition(i);
                return;
            }
        }
    }

    /**
     * 平滑滚动到一个Entity。
     *
     * @param entity
     */
    public void smoothScrollToEntity(@NonNull Object entity) {
        if (mItems == null || mAttachRecyclerView == null) {
            return;
        }
        mAttachRecyclerView.smoothScrollToPosition(findEntityPosition(entity));
    }

    /**
     * 删除某个Item。
     *
     * @param entity
     */
    public void removeItem(@NonNull Object entity) {
        if (mItems == null || mAttachRecyclerView == null) {
            return;
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (entity.equals(mItems.get(i).entity)) {
                mItems.remove(i);
                break;
            }
        }
    }

    /**
     * 查找一个Entity的位置。
     *
     * @param entity
     * @return
     */
    public int findEntityPosition(@NonNull Object entity) {
        if (mItems == null) {
            return 0;
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (entity.equals(mItems.get(i).entity)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * item点击回调，给子类使用。
     *
     * @param item
     */
    protected void onItemClick(XItem item) {
        // 子类实现。
    }

    public XItem getCurrentSelectItem() {
        return currentSelectItem;
    }

    public Object getCurrentSelectEntity() {
        return currentSelectItem != null ? currentSelectItem.getEntity() : null;
    }

    @Override
    public XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends XViewHolder> holderClass = typeHolderArray.get(viewType);
        if (holderClass != null) {
            try {
                // 通过type生成不同的ViewHolder。
                XViewHolder viewHolder =
                        holderClass.getConstructor(Context.class, ViewGroup.class).newInstance(mContext, parent);
                // 设置点击监听。
                viewHolder.setItemClickListener((position, item) -> {
                    if (item.getEntity() == null) {
                        return false;
                    }
                    onItemClick(item);
                    OnEntityClickListener listener = mClickListenerMap.get(item.getEntity().getClass());
                    if (listener == null || !listener.onClick(position, item.getEntity())) {
                        // 如果事件没有被拦截，切换选中状态。
                        if (item.isSelectable()) {
                            setCurrentSelectItem(item);
                        }
                    }
                    return false;
                });
                viewHolder.setAdapter(this);
                return viewHolder;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(XViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.onBindViewHolder(position, mItems.get(position), payloads);
        }
    }

    @Override
    public void onBindViewHolder(XViewHolder holder, int position) {
        holder.onBindViewHolder(position, mItems.get(position), null);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems != null && mItems.size() > position) {
            return mItems.get(position).getItemType();
        }
        return 0;
    }

    /**
     * 内部点击事件
     */
    private OnItemChildClickListener onItemChildClickListener;

    /**
     * 内部长点击事件
     */
    private OnItemChildLongClickListener onItemChildLongClickListener;

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return onItemChildLongClickListener;
    }

    public XItem<?> getItemEntityByAdapterPosition(int position) {
        if (CollectionUtils.inRange(mItems, position)) {
            return mItems.get(position);
        }
        return null;
    }
}

package me.csxiong.library.integration.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.csxiong.library.utils.CollectionUtils;

public class AdapterDataBuilder {
    /**
     * 数据源。
     */
    private List<XItem> mItemList = new ArrayList<>();
    /**
     * 对应的HolderClass储藏。
     */
    private List<Class<? extends XViewHolder>> mHolderClassList = new LinkedList<>();

    public static AdapterDataBuilder create() {
        // 这样写格式比较好看。
        return new AdapterDataBuilder();
    }

    /**
     * 添加普通数据。
     * @param itemEntities
     * @param holderClass
     * @param <T>
     * @param <E>
     */
    public <T, E extends Class<? extends XViewHolder>> AdapterDataBuilder addEntities(List<T> itemEntities,
                                                                                      E holderClass) {
        addEntitiesImpl(itemEntities, holderClass, false, null);
        return this;
    }

    /**
     * 添加普通数据。
     * @param itemEntities
     * @param holderClass
     * @param <T>
     * @param <E>
     */
    public <T, E extends Class<? extends XViewHolder>> AdapterDataBuilder addEntities(List<T> itemEntities,
                                                                                      E holderClass, DataConvertor<T> convertor) {
        addEntitiesImpl(itemEntities, holderClass, false, convertor);
        return this;
    }

    /**
     * 添加可选中数据。
     * @param itemEntities
     * @param holderClass
     * @param <T>
     * @param <E>
     */
    public <T, E extends Class<? extends XViewHolder>> AdapterDataBuilder addSelectableEntities(List<T> itemEntities,
                                                                                                E holderClass) {
        addEntitiesImpl(itemEntities, holderClass, true, null);
        return this;
    }

    /**
     * 添加数据源。
     * @param itemEntities
     * @param holderClass
     * @param isSelectable
     * @param <T>
     * @param <E>
     */
    private <T, E extends Class<? extends XViewHolder>> void addEntitiesImpl(List<T> itemEntities, E holderClass,
                                                                             boolean isSelectable, DataConvertor<T> convertor) {
        if (CollectionUtils.isEmpty(itemEntities)) {
            return;
        }
        // 通过HolderClass的不同，创建HolderType。
        int itemType = holderClass.hashCode();
        if (mHolderClassList.contains(holderClass)) {
            itemType = mHolderClassList.indexOf(holderClass);
        } else {
            mHolderClassList.add(holderClass);
        }
        // 创建Item。
        for (T entity : itemEntities) {
            if (convertor != null && !convertor.enable(entity)) {
                continue;
            }
            XItem<T> item = new XItem<>(entity);
            item.setItemType(itemType);
            item.setViewHolderClass(holderClass);
            item.setSelectable(isSelectable);
            mItemList.add(item);
        }
    }

    /**
     * 数据转化
     * @param <T>
     */
    public interface DataConvertor<T> {

        boolean enable(T t);
    }

    public List<XItem> build() {
        return mItemList;
    }
}

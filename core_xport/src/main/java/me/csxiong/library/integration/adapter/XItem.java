package me.csxiong.library.integration.adapter;

import java.util.Objects;

/**
 * @Desc : 被包装的数据
 * @Author : Bear - 2020/8/18
 */
public class XItem<E> {
    /**
     * 是否正在被选中。
     */
    private boolean isSelect;
    /**
     * 是否是可选中的。
     */
    private boolean isSelectable;
    /**
     * 真实的数据体。
     */
    protected E entity;
    /**
     * itemType.
     */
    private int itemType;
    /**
     * 这个Item对应的ViewHolder。
     */
    private Class<? extends XViewHolder> viewHolderClass;

    public XItem(E entity) {
        this.entity = entity;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public E getEntity() {
        return entity;
    }

    Class<? extends XViewHolder> getViewHolderClass() {
        return viewHolderClass;
    }

    void setViewHolderClass(Class<? extends XViewHolder> viewHolderClass) {
        this.viewHolderClass = viewHolderClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XItem<?> xItem = (XItem<?>) o;
        return Objects.equals(entity, xItem.entity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(entity);
    }
}

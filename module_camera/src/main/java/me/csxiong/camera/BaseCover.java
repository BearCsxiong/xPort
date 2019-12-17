package me.csxiong.camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @Desc : 基础Cover
 * @Author : csxiong - 2019-11-22
 */
public abstract class BaseCover implements ICover {

    public ICoverGroup mCoverGroup;

    public Context mContext;

    @Override
    public View getRootView() {
        return LayoutInflater.from(mContext).inflate(getLayoutId(), null, false);
    }

    @Override
    public void onAttachGroup(Context context, ICoverGroup iCoverGroup) {
        mContext = context;
        mCoverGroup = iCoverGroup;
    }

    @Override
    public void onDetachGroup() {
        mCoverGroup = null;
    }

    /**
     * 发送Cover的消息
     *
     * @param coverEventCode
     * @param datas
     */
    public void postEvent(int coverEventCode, Object... datas) {
        if (mCoverGroup != null) {
            mCoverGroup.postCoverEvent(this, coverEventCode, datas);
        }
    }

    protected abstract int getLayoutId();

}

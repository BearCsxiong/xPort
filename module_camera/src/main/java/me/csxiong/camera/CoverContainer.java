package me.csxiong.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.camera.cover.CameraCover;

/**
 * @Desc : 保存所有Cover
 * @Author : csxiong - 2019-11-22
 */
public class CoverContainer extends FrameLayout implements ICoverGroup {

    private List<ICover> covers = new ArrayList<>();

    private DataProviders dataProviders = new DataProviders();

    private CameraCover cameraCover;

    public CoverContainer(Context context) {
        this(context, null);
    }

    public CoverContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoverContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 添加基础Cover
     */
    private void init() {
        cameraCover = new CameraCover();
        addCover(cameraCover);
    }

    /**
     * 添加Cover
     *
     * @param cover
     */
    public void addCover(ICover cover) {
        if (cover != null && !covers.contains(cover)) {
            covers.add(cover);
            cover.onAttachGroup(getContext(), this);
            addView(cover.getRootView());
        }
    }

    /**
     * 移除Cover
     *
     * @param cover
     */
    public void removeCover(ICover cover) {
        if (cover != null && covers.contains(cover)) {
            covers.remove(cover);
            cover.onDetachGroup();
            removeView(cover.getRootView());
        }
    }

    @Override
    public DataProviders getDataProviders() {
        return dataProviders;
    }

    @Override
    public void postCoverEvent(ICover postCover, int coverEventCode, Object... datas) {
        for (ICover cover : covers) {
            if (cover != null && cover != postCover) {
                cover.onCoverEvent(coverEventCode, datas);
            }
        }
    }
}

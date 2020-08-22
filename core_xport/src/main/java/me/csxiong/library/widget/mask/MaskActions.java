package me.csxiong.library.widget.mask;


import java.util.List;

/**
 * Created by csxiong on 2018/10/22.
 */

public class MaskActions {

    private List<MaskAction> maskActions;

    private OnInflateListener onInflateListener;

    public List<MaskAction> getMaskActions() {
        return maskActions;
    }

    public void setMaskActions(List<MaskAction> maskActions) {
        this.maskActions = maskActions;
    }

    public MaskActions(List<MaskAction> maskActions) {
        this.maskActions = maskActions;
    }

    public OnInflateListener getOnInflateListener() {
        return onInflateListener;
    }

    public void setOnInflateListener(OnInflateListener onInflateListener) {
        this.onInflateListener = onInflateListener;
    }
}

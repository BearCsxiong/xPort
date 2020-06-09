package me.csxiong.library.integration.sys;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * @Desc : Fragment支持可见检测类
 * @Author : csxiong - 2020-03-12
 */
public class FragmentSupportVisibleDetector {

    /**
     * 是否是第一次Resume
     */
    private boolean isFirstResume = true;

    /**
     * 是否可见
     */
    private boolean isSupportVisible;

    /**
     * 是否进入过onPause
     */
    private boolean isFromPause;

    /**
     * 目标Fragment
     */
    private Fragment fragment;

    /**
     * Visible集成
     */
    private OnSupportVisibleIntegration onSupportVisibleIntegration;

    public FragmentSupportVisibleDetector(@NonNull Fragment fragment) {
        this.fragment = fragment;
        if (fragment instanceof OnSupportVisibleIntegration) {
            this.onSupportVisibleIntegration = (OnSupportVisibleIntegration) fragment;
        }
    }

    public void onResume() {
        if (fragment == null || !fragment.isAdded()) {
            return;
        }
        if (isFirstResume) {
            isFirstResume = false;
        }
        checkVisible();
        isFromPause = false;
    }

    public void onPause() {
        if (fragment == null) {
            return;
        }
        isFromPause = true;
        if (isSupportVisible) {
            isSupportVisible = false;
            if (onSupportVisibleIntegration != null) {
                onSupportVisibleIntegration.onSupportInvisible();
            }
        }
    }

    public void onHiddenChange() {
        if (fragment == null || !fragment.isAdded() || isFirstResume) {
            return;
        }
        checkVisible();
    }

    public void setUserVisibleHint() {
        if (fragment == null || !fragment.isAdded() || isFirstResume) {
            return;
        }
        checkVisible();
    }

    /**
     * 检查可见并且延续通知
     */
    public void checkVisible() {
        boolean supportVisible = isSupportVisible();
        if (supportVisible && !isSupportVisible) {
            isSupportVisible = true;
            if (onSupportVisibleIntegration != null) {
                onSupportVisibleIntegration.onSupportVisible();
            }
        } else if (!supportVisible && isSupportVisible) {
            isSupportVisible = false;
            if (onSupportVisibleIntegration != null) {
                onSupportVisibleIntegration.onSupportInvisible();
            }
        }
        List<Fragment> fgs = fragment.getChildFragmentManager().getFragments();
        for (int i = 0; i < fgs.size(); i++) {
            Fragment fg = fgs.get(i);
            if (fg instanceof OnSupportVisibleIntegration) {
                ((OnSupportVisibleIntegration) fg).getFragmentSupportVisibleHelper().checkVisible();
            }
        }
    }

    /**
     * 判断是否可见方法 真实是否可见
     *
     * @return
     */
    public boolean isSupportVisible() {
        if (fragment == null) {
            return false;
        }
        // parent不可见
        boolean isParentInvisible = fragment.getParentFragment() != null
                && (!fragment.getParentFragment().getUserVisibleHint() || fragment.getParentFragment().isHidden());
        if (isParentInvisible) {
            return false;
        } else {
            // self不可见
            boolean isForwardInvisible = (!fragment.getUserVisibleHint() || fragment.isHidden());
            if (isForwardInvisible) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否来自onPause
     * @return
     */
    public boolean isFromPause() {
        return isFromPause;
    }

}

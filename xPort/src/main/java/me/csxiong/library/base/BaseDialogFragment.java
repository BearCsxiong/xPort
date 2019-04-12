//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.csxiong.library.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : from fragmentation fragment to create dialogfragment
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/4/12 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class BaseDialogFragment extends DialogFragment implements ISupportFragment {
    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected FragmentActivity _mActivity;

    public BaseDialogFragment() {
    }

    public SupportFragmentDelegate getSupportDelegate() {
        return this.mDelegate;
    }

    public ExtraTransaction extraTransaction() {
        return this.mDelegate.extraTransaction();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mDelegate.onAttach(activity);
        this._mActivity = this.mDelegate.getActivity();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDelegate.onCreate(savedInstanceState);
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return this.mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mDelegate.onActivityCreated(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mDelegate.onSaveInstanceState(outState);
    }

    public void onResume() {
        super.onResume();
        this.mDelegate.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mDelegate.onPause();
    }

    public void onDestroyView() {
        this.mDelegate.onDestroyView();
        super.onDestroyView();
    }

    public void onDestroy() {
        this.mDelegate.onDestroy();
        super.onDestroy();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mDelegate.onHiddenChanged(hidden);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mDelegate.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void enqueueAction(Runnable runnable) {
        this.mDelegate.enqueueAction(runnable);
    }

    public void post(Runnable runnable) {
        this.mDelegate.post(runnable);
    }

    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        this.mDelegate.onEnterAnimationEnd(savedInstanceState);
    }

    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        this.mDelegate.onLazyInitView(savedInstanceState);
    }

    public void onSupportVisible() {
        this.mDelegate.onSupportVisible();
    }

    public void onSupportInvisible() {
        this.mDelegate.onSupportInvisible();
    }

    public final boolean isSupportVisible() {
        return this.mDelegate.isSupportVisible();
    }

    public FragmentAnimator onCreateFragmentAnimator() {
        return this.mDelegate.onCreateFragmentAnimator();
    }

    public FragmentAnimator getFragmentAnimator() {
        return this.mDelegate.getFragmentAnimator();
    }

    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        this.mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    public boolean onBackPressedSupport() {
        return this.mDelegate.onBackPressedSupport();
    }

    public void setFragmentResult(int resultCode, Bundle bundle) {
        this.mDelegate.setFragmentResult(resultCode, bundle);
    }

    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        this.mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    public void onNewBundle(Bundle args) {
        this.mDelegate.onNewBundle(args);
    }

    public void putNewBundle(Bundle newBundle) {
        this.mDelegate.putNewBundle(newBundle);
    }

    protected void hideSoftInput() {
        this.mDelegate.hideSoftInput();
    }

    protected void showSoftInput(View view) {
        this.mDelegate.showSoftInput(view);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        this.mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        this.mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    public void loadMultipleRootFragment(int containerId, int showPosition, ISupportFragment... toFragments) {
        this.mDelegate.loadMultipleRootFragment(containerId, showPosition, toFragments);
    }

    public void showHideFragment(ISupportFragment showFragment) {
        this.mDelegate.showHideFragment(showFragment);
    }

    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        this.mDelegate.showHideFragment(showFragment, hideFragment);
    }

    public void start(ISupportFragment toFragment) {
        this.mDelegate.start(toFragment);
    }

    public void start(ISupportFragment toFragment, int launchMode) {
        this.mDelegate.start(toFragment, launchMode);
    }

    public void startForResult(ISupportFragment toFragment, int requestCode) {
        this.mDelegate.startForResult(toFragment, requestCode);
    }

    public void startWithPop(ISupportFragment toFragment) {
        this.mDelegate.startWithPop(toFragment);
    }

    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        this.mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        this.mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        this.mDelegate.pop();
    }

    public void popChild() {
        this.mDelegate.popChild();
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        this.mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        this.mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        this.mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        this.mDelegate.popToChild(targetFragmentClass, includeTargetFragment);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        this.mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        this.mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(this.getFragmentManager());
    }

    public ISupportFragment getTopChildFragment() {
        return SupportHelper.getTopFragment(this.getChildFragmentManager());
    }

    public ISupportFragment getPreFragment() {
        return SupportHelper.getPreFragment(this);
    }

    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(this.getFragmentManager(), fragmentClass);
    }

    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(this.getChildFragmentManager(), fragmentClass);
    }

    //fix for dialog show or dismiss

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    // pro error for multi show

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isShowing()) {
            //正常显示
            if (!isAdded()) {
                super.show(manager, tag);
            } else {
                FragmentTransaction ft = manager.beginTransaction();
                ft.show(this);
                ft.commit();
            }
        }
    }

    @Override
    public void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getActivity() != null && (getActivity().isDestroyed() || getActivity().isFinishing())) {
            return;
        } else if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }
        if (isResumed() && isShowing()) {
            super.dismiss();
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (isShowing()) {
            super.dismissAllowingStateLoss();
        }
    }

}

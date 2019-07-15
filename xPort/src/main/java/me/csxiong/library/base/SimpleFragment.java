package me.csxiong.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.R;
import me.csxiong.library.base.delegate.ViewDelegate;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;
import me.csxiong.library.utils.XPreconditions;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : base on {@link SupportFragment} to build all Fragment
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/20 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class SimpleFragment extends SupportFragment implements IView, IPage, IFragmentLifecycle {

    private final BehaviorSubject<FragmentEvent> mSubject = BehaviorSubject.create();

    protected View view;
    protected SimpleActivity mActivity;
    protected Context mContext;

    protected ViewDelegate mViewDegate;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        XPreconditions.checkActicityState(getActivity(), "simpleFragment必须寄生在simpleActivity上");
        mActivity = (SimpleActivity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        if (mViewDegate == null) {
            mViewDegate = new ViewDelegate(this);
        }
        initUI(savedInstanceState);
        initData(savedInstanceState);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mViewDegate = null;
        super.onDestroy();
    }

    public FragmentAnimator getFragmentAnimator() {
        FragmentAnimator fragmentAnimator = super.onCreateFragmentAnimator();
        fragmentAnimator.setEnter(R.anim.window_left_in);
        fragmentAnimator.setExit(R.anim.window_right_out);
        fragmentAnimator.setPopEnter(R.anim.window_right_in);
        fragmentAnimator.setPopExit(R.anim.window_left_out);
        return fragmentAnimator;
    }

    @Override
    public AppComponent getAppComponent() {
        return APP.getInstance().getAppComponent();
    }

    @Override
    public void startLoading(String loadingMsg) {
        mViewDegate.startLoading(loadingMsg);
    }

    @Override
    public void stopLoading() {
        mViewDegate.stopLoading();
    }

    @Override
    public Subject<FragmentEvent> provideLifecycle() {
        return mSubject;
    }
}

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
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IFragmentLifecycle;
import me.csxiong.library.utils.XPreconditions;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**-------------------------------------------------------------------------------
*| 
*| desc : 基础Fragment实现,基于Fragmentation
*| 
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong 
*|--------------------------------------------------------------------------------
*/
public abstract class SimpleFragment extends SupportFragment implements IView, IPage, IFragmentLifecycle {

    private final BehaviorSubject<FragmentEvent> mSubject = BehaviorSubject.create();

    protected View mView;
    protected SimpleActivity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected boolean isInitUI;

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
        mView = inflater.inflate(getLayoutResId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!isInitUI) {
            initUI(savedInstanceState);
            initData(savedInstanceState);
            isInitUI = true;
        }
    }

    @Override
    public void onDestroy() {
        mUnBinder.unbind();
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
    public void showErrorTip(String errorMsg) {

    }

    @Override
    public void showWarnTip(String warnTip) {

    }

    @Override
    public void showSuccessTip(String successTip) {

    }

    @Override
    public void dismissTip() {

    }

    @Override
    public void startWaiting() {

    }

    @Override
    public void stopWaiting() {

    }

    @Override
    public Subject<FragmentEvent> provideLifecycle() {
        return mSubject;
    }
}

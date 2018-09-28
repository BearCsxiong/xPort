package me.csxiong.library.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.trello.rxlifecycle2.android.ActivityEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.csxiong.library.base.delegate.ViewDelegate;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : 基础Activity实现类,基于Fragmentation
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/20 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public abstract class SimpleActivity extends SupportActivity implements IView, IPage, IActivityLifecycle {

    private final BehaviorSubject<ActivityEvent> mSubject = BehaviorSubject.create();

    protected Activity mContext;

    private Unbinder mUnBinder;

    protected boolean immersive = false;

    protected ViewDelegate mViewDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;

        if (immersive) {
            setFullScreen();
            cancelFullScreen();
        }
        if (mViewDelegate == null) {
            mViewDelegate = new ViewDelegate(this);
        }
        /*初始化UI*/
        initUI(savedInstanceState);
        /*初始化数据*/
        initData(savedInstanceState);
    }

    public void cancelFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public AppComponent getAppComponent() {
        return APP.getInstance().getAppComponent();
    }

    @Override
    public void startLoading(String loadingMsg) {
        mViewDelegate.startLoading(loadingMsg);
    }

    @Override
    public void stopLoading() {
        mViewDelegate.stopLoading();
    }

    @Override
    public Subject<ActivityEvent> provideLifecycle() {
        return mSubject;
    }
}

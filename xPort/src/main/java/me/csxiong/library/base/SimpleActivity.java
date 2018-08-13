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
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.integration.lifecycle.IActivityLifecycle;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 无MVP的Activity基类
 */
public abstract class SimpleActivity extends SupportActivity implements IView, IPage, IActivityLifecycle {

    private final BehaviorSubject<ActivityEvent> mSubject = BehaviorSubject.create();

    protected Activity mContext;

    private Unbinder mUnBinder;

    protected boolean immersive = false;

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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public Subject<ActivityEvent> provideLifecycle() {
        return mSubject;
    }
}

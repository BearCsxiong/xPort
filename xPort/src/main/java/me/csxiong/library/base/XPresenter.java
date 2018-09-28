package me.csxiong.library.base;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : 基本Presenter帮助控制订阅事件生命周期
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/20 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class XPresenter<T extends IView> implements IPresenter<T> {

    protected WeakReference<T> mView;
    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.mView = new WeakReference<T>(view);
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}

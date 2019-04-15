package me.csxiong.library.base.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.csxiong.library.base.IView;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : default Presenter for using
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/20 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class XPresenter<T extends IView> implements IPresenter<T> {

    protected T mView;
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
        this.mView = view;
    }

    @Override
    public void detachView() {
        unSubscribe();
        this.mView = null;
    }
}

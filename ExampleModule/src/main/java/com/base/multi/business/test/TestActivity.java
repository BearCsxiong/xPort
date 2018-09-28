package com.base.multi.business.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.multi.business.next.NextActivity;
import com.base.multi.di.component.DaggerActivityComponent;
import com.kehua.pile.R;
import com.kehua.pile.R2;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.subscribers.ResourceSubscriber;
import me.csxiong.library.base.IView;
import me.csxiong.library.base.MVPActivity;
import me.csxiong.library.integration.http.Response;
import me.csxiong.library.utils.RxLifecycleUtil;
import me.csxiong.library.utils.RxUtils;
import me.csxiong.library.utils.XUtils;

/**
 * Created by csxiong on 2018/8/8.
 */

@Route(path = "/multi/test")
public class TestActivity extends MVPActivity<TestPresenter> implements TestContract.View {


    @BindView(R2.id.btn_close)
    Button btn;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        XUtils.init(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Flowable.interval(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtil.bindUntilEvent((IView) this, ActivityEvent.STOP))
                .subscribeWith(new ResourceSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        startLoading("加载中");
                        Observable<Response<String>> observable = new Observable<Response<String>>() {
                            @Override
                            protected void subscribeActual(Observer<? super Response<String>> observer) {
                                Response<String> response = new Response<>();
                                response.setErrcode(199);
                                response.setMessage("失败");
                                response.setData("from test http request");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                observer.onNext(response);
                            }
                        };

                        observable.compose(RxUtils.doDefaultHttpTransformer(TestActivity.this, String.class))
                                .subscribeWith(new ResourceObserver<String>() {
                                    @Override
                                    public void onNext(String s) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void initInject() {
        DaggerActivityComponent.builder()
                .appComponent(getAppComponent())
                .build().inject(this);
    }

    @OnClick(R2.id.btn_close)
    public void onClose() {
        Intent intent = new Intent();
        intent.setClass(TestActivity.this, NextActivity.class);
        startActivity(intent);
    }
}

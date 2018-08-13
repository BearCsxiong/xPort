package com.base.project.interceptor;

import android.Manifest;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.blankj.utilcode.util.ActivityUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @des:权限拦截
 */

@Interceptor(priority = 1)
public class PermissionInterceptor implements IInterceptor {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public PermissionInterceptor() {
    }

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if (postcard.getPath().equals("/pile/toScan")) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new RxPermissions(ActivityUtils.getTopActivity()).request(Manifest.permission.CAMERA)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
//                                        DialogUtils.showPermissionWarning(ActivityUtils.getTopActivity(), "需要开启相机权限");
                                        callback.onInterrupt(null);
                                    } else {
                                        callback.onContinue(postcard);
                                    }
                                }

                            });
                }
            });


        } else if (postcard.getPath().equals("/pile/toMap")) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new RxPermissions(ActivityUtils.getTopActivity()).request(Manifest.permission.ACCESS_COARSE_LOCATION)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
//                                        DialogUtils.showPermissionWarning(ActivityUtils.getTopActivity(), "需要开启定位权限");
                                        callback.onInterrupt(null);
                                    } else {
                                        callback.onContinue(postcard);
                                    }
                                }

                            });
                }
            });


//        } else if (postcard.getPath().equals("/main/toMain")) {
//            new RxPermissions(ActivityUtils.getTopActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe(new Consumer<Boolean>() {
//                        @Override
//                        public void accept(Boolean aBoolean) throws Exception {
//                            if (!aBoolean) {
//                                DialogUtils.showPermissionWarning(ActivityUtils.getTopActivity(), "需要开启读写权限");
//                                callback.onInterrupt(null);
//                            } else {
//                                callback.onContinue(postcard);
//                            }
//                        }
//                    });
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}

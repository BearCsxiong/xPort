package com.base.project.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * @des:需要登录验证的拦截器 Created by Administrator on 2018/5/3.
 */

@Interceptor(priority = 2)
public class LoginInterceptor implements IInterceptor {


    public LoginInterceptor() {
    }

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }
}

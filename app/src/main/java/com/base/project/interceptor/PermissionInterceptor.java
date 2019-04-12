package com.base.project.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : design for permissions
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/4/12 created by csxiong
 * |--------------------------------------------------------------------------------
 */
@Interceptor(priority = 1)
public class PermissionInterceptor implements IInterceptor {

    public PermissionInterceptor() {
    }

    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }
}

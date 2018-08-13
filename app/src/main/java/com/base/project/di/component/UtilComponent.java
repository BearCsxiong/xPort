package com.base.project.di.component;


import com.base.project.interceptor.LoginInterceptor;
import com.base.project.interceptor.PermissionInterceptor;

import dagger.Component;

@Component
public interface UtilComponent {
    void inject(LoginInterceptor interceptor);

    void inject(PermissionInterceptor interceptor);
}

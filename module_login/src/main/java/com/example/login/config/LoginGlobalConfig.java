package com.example.login.config;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

import me.csxiong.library.base.GlobalConfig;
import me.csxiong.library.base.IAppDelegate;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;
import me.csxiong.library.integration.http.JsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by csxiong on 2018/5/23.
 */

public class LoginGlobalConfig implements GlobalConfig {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.retrofitConfiguration(new ClientModule.RetrofitConfiguration() {
            @Override
            public void configRetrofit(Context context, Retrofit.Builder builder) {
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(JsonConverterFactory.create());
            }
        });

    }

    @Override
    public void injectAppLifecycle(Context context, List<IAppDelegate> lifecycles) {
        lifecycles.add(new LoginAppDelegate());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}

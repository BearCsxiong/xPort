package me.csxiong.library.di.module;


import android.app.Application;

import me.csxiong.library.integration.ApiProvider;
import me.csxiong.library.integration.lifecycle.ActivityLifecycleForRxLifecycle;
import me.csxiong.library.integration.lifecycle.FragmentLifecycleActivityProxyer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    @Named("ActivityLifecycle")
    Application.ActivityLifecycleCallbacks provideActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle) {
        return activityLifecycleForRxLifecycle;
    }

    @Provides
    @Singleton
    @Named("FragmentLifecycle")
    Application.ActivityLifecycleCallbacks provideFragmentLifecycleForRxLifecycle(FragmentLifecycleActivityProxyer fragmentLifecycleActivityProxyer) {
        return fragmentLifecycleActivityProxyer;
    }

    @Provides
    @Singleton
    ApiProvider provideServiceProvider(ApiProvider provider) {
        return provider;
    }
}

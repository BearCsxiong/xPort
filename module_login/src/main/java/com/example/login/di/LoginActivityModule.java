package com.example.login.di;

import com.example.login.ui.login.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.ActivityScope;

@Module
public  abstract class LoginActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract LoginActivity injectTest();

}

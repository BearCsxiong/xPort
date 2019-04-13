package com.example.login.di;

import com.example.login.ui.register.RegisterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.library.di.scope.FragmentScope;

@Module
public abstract class LoginFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract RegisterFragment injectNext();
}

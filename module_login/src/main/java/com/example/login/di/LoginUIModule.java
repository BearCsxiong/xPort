package com.example.login.di;

import dagger.Module;

@Module(includes = {
        LoginActivityModule.class,
        LoginFragmentModule.class}
)
public abstract class LoginUIModule {
}

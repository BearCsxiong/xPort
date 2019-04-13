package com.base.project.di.module;

import dagger.Module;

@Module(includes = {
        MainActivityModule.class,
        MainFragmentModule.class
})
public class MainUIModule {
}

package com.base.project.di;

import dagger.Module;

/**
 * @Desc : 所有组件模块
 * @Author : csxiong create on 2019/7/17
 */
@Module(includes = {
        MainActivityModule.class,
        MainFragmentModule.class
})
public class MainUIModule {
}

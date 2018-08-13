package com.base.project.di.component;

import android.app.Activity;

import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.di.scope.ActivityScope;
import com.base.project.di.module.ActivityModule;
import com.base.project.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity activity);

}

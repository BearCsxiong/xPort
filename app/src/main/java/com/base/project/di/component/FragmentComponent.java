package com.base.project.di.component;

import android.app.Activity;

import me.csxiong.library.di.component.AppComponent;
import com.base.project.di.module.FragmentModule;
import me.csxiong.library.di.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

}

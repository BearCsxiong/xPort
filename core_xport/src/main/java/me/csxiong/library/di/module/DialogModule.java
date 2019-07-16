package me.csxiong.library.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.base.SimpleDialog;
import me.csxiong.library.di.scope.FragmentScope;

/**
 * @Desc : 所有弹框模块
 * @Author : csxiong create on 2019/7/16
 */
@Module
public class DialogModule {

    private SimpleDialog dialog;

    public DialogModule(SimpleDialog dialog) {
        this.dialog = dialog;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return dialog.getActivity();
    }
}

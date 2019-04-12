package me.csxiong.library.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.base.SimpleDialog;
import me.csxiong.library.di.scope.FragmentScope;

/**
 * Created by csxiong on 2018/11/2.
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

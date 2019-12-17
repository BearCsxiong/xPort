package me.csxiong.camera.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import me.csxiong.camera.ui.album.AlbumActivity;
import me.csxiong.library.di.scope.ActivityScope;

@Module
public  abstract class CameraActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract AlbumActivity injectAlbum();

}

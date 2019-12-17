package me.csxiong.camera.di;

import dagger.Module;

@Module(includes = {
        CameraActivityModule.class,
        CameraFragmentModule.class}
)
public abstract class CameraUIModule {
}

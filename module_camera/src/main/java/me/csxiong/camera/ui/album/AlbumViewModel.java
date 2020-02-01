package me.csxiong.camera.ui.album;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.csxiong.library.base.XViewModel;

/**
 * @Desc : 相册ViewModel
 * @Author : csxiong create on 2019/12/17
 */
public class AlbumViewModel extends XViewModel {

    @Inject
    public AlbumViewModel(@NonNull Application application) {
        super(application);
    }

}
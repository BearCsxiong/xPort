package me.csxiong.camera.ui.album;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.album.OnProgressListener;
import me.csxiong.camera.album.OnResultListener;
import me.csxiong.camera.album.XAlbum;
import me.csxiong.library.base.XViewModel;

/**
 * @Desc : 相册ViewModel
 * @Author : csxiong create on 2019/12/17
 */
public class AlbumViewModel extends XViewModel {

    XAlbum album;

    private MutableLiveData<List<ImageEntity>> albumDataEvent = new MutableLiveData<>();

    public MutableLiveData<List<ImageEntity>> getAlbumDataEvent() {
        return albumDataEvent;
    }

    @Inject
    public AlbumViewModel(@NonNull Application application) {
        super(application);
        album = new XAlbum(application);
        album.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(float progress) {

            }
        });

        album.setOnResultListener(new OnResultListener() {
            @Override
            public void onResult(List<ImageEntity> imageEntities) {
                getAlbumDataEvent().setValue(imageEntities);
            }
        });
    }

    /**
     * 加载数据
     */
    public void loadData() {
        album.execute();
    }
}

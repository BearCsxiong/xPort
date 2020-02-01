package me.csxiong.camera.ui.album;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import me.csxiong.camera.album.ImageEntity;
import me.csxiong.camera.album.OnResultListener;
import me.csxiong.camera.album.XAlbum;
import me.csxiong.library.base.APP;

/**
 * @Desc : 相册数据仓库
 * @Author : csxiong - 2020-01-31
 */
public class AlbumRepository {

    private AlbumRepository() {

    }

    private static AlbumRepository instance;

    private MutableLiveData<List<ImageEntity>> albumDataEvent = new MutableLiveData<>();

    public static AlbumRepository getInstance() {
        if (instance == null) {
            synchronized (AlbumRepository.class) {
                if (instance == null) {
                    instance = new AlbumRepository();
                }
            }
        }
        return instance;
    }

    public ImageEntity entity;

    public MutableLiveData<List<ImageEntity>> getAlbumDataEvent() {
        return albumDataEvent;
    }

    public void loadData() {
        loadData(false);
    }

    /**
     * 加载相册数据
     *
     * @param isReload
     */
    public void loadData(boolean isReload) {
        if (albumDataEvent.getValue() == null || isReload) {
            new XAlbum(APP.get())
                    .setOnResultListener(new OnResultListener() {
                        @Override
                        public void onResult(List<ImageEntity> imageEntities) {
                            albumDataEvent.setValue(imageEntities);
                        }
                    })
                    .execute();
        }
    }

    /**
     * 保存当前观看的图片
     *
     * @param image
     */
    public void setCurrentVisiableImage(ImageEntity image) {
        this.entity = image;
    }

    /**
     * 获取当前观看的图片
     *
     * @return
     */
    public ImageEntity getCurrentVisiableImage() {
        return entity;
    }
}

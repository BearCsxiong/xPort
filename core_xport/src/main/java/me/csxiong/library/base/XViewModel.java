package me.csxiong.library.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 * @Desc : 基础ViewModel
 * @Author : csxiong create on 2019/7/22
 */
public class XViewModel extends AndroidViewModel {

    /**
     * loading事件
     */
    MutableLiveData<String> loadingEvent = new MutableLiveData<>();

    /**
     * progress事件
     */
    MutableLiveData<Integer> progressEvent = new MutableLiveData<>();

    public XViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getLoadingEvent() {
        return loadingEvent;
    }

    public MutableLiveData<Integer> getProgressEvent() {
        return progressEvent;
    }



}

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
     * 等待loading文案事件
     * null关闭
     */
    private MutableLiveData<String> showLoadingEvent;

    /**
     * 进度progress弹框
     * 默认0～100
     * null关闭
     */
    private MutableLiveData<ProgressEvent> showProgressEvent;

    public XViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getShowLoadingEvent() {
        if (showLoadingEvent == null) {
            showLoadingEvent = new MutableLiveData<>();
        }
        return showLoadingEvent;
    }

    public MutableLiveData<ProgressEvent> getShowProgressEvent() {
        if (showProgressEvent == null) {
            showProgressEvent = new MutableLiveData<>();
        }
        return showProgressEvent;
    }

}

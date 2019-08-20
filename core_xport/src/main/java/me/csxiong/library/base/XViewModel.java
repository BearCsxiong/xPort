package me.csxiong.library.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * @Desc : 基础ViewModel
 * @Author : csxiong create on 2019/7/22
 */
public class XViewModel extends AndroidViewModel {

    public XViewModel(@NonNull Application application) {
        super(application);
    }

}

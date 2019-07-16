package me.csxiong.library.integration.lifecycle;

import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.Subject;

/**
 * @Desc : 生命周期提供
 * @Author : csxiong create on 2019/7/17
 */
public interface ILifecycle<T> {

    @NonNull
    Subject<T> provideLifecycle();
}

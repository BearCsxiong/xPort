package me.csxiong.library.integration.lifecycle;

import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.Subject;

public interface ILifecycle<T> {

    @NonNull
    Subject<T> provideLifecycle();
}

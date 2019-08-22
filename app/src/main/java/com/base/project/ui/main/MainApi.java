package com.base.project.ui.main;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MainApi {

    @GET("http://www.baidu.com")
    Observable<String> login();
}

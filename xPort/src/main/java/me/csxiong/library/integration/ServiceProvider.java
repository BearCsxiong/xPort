/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.csxiong.library.integration;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import dagger.Lazy;
import me.csxiong.library.integration.cache.LruCache;
import me.csxiong.library.utils.XPreconditions;
import retrofit2.Retrofit;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : retrofit中service缓存
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/9/27 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class ServiceProvider {
    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Application mApplication;
    @Inject
    Lazy<LruCache<String, Object>> mCache;

    private final String CACHE_KEY_RETROFIT = "RETROFIT";

    @Inject
    public ServiceProvider() {
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    public synchronized <T> T get(Class<T> service) {
        XPreconditions.checkNotNull(service, "retrofit service不允许为空");
        if (mCache.get().get(CACHE_KEY_RETROFIT + service.getCanonicalName()) == null) {
            mCache.get().put(CACHE_KEY_RETROFIT + service.getCanonicalName(), mRetrofit.get().create(service));
        }
        return (T) mCache.get().get(CACHE_KEY_RETROFIT + service.getCanonicalName());
    }

    public Context getContext() {
        return mApplication;
    }
}

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
package me.csxiong.library.di.module;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.utils.Constants;
import okhttp3.HttpUrl;

@Module
public class GlobalConfigModule {
    private HttpUrl mApiUrl;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.LoggerConfiguration mLoggerConfiguration;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mLoggerConfiguration = builder.loggerConfiguration;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? new File(application.getCacheDir().getAbsolutePath()+File.separator+ Constants.FILE_NAME_NET_CACHE) : mCacheFile;
    }


    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.LoggerConfiguration provideLoggerConfiguration() {
        return mLoggerConfiguration;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.LoggerConfiguration loggerConfiguration;

        private Builder() {
        }

        public Builder baseurl(String baseUrl) {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder loggerConfiguration(ClientModule.LoggerConfiguration loggerConfiguration) {
            this.loggerConfiguration = loggerConfiguration;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }


    }

}

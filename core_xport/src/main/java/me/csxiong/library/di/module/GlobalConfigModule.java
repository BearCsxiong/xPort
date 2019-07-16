package me.csxiong.library.di.module;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.BuildConfig;
import me.csxiong.library.base.GlobalConfig;
import okhttp3.HttpUrl;

/**
 * @Desc : 全局化配置模块 所有内部提供可配置选项,使用{@link GlobalConfig}
 * @Author : csxiong create on 2019/7/16
 */
@Module
public class GlobalConfigModule {
    private HttpUrl mApiUrl;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.LoggerConfiguration mLoggerConfiguration;
    private ClientModule.GsonConfiguration mGsonConfiguration;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mLoggerConfiguration = builder.loggerConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
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
        return mApiUrl == null ? HttpUrl.parse(BuildConfig.MAIN_HOST) : mApiUrl;
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? new File(application.getCacheDir().getAbsolutePath() + File.separator + "NetCache") : mCacheFile;
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

    @Singleton
    @Provides
    @Nullable
    ClientModule.GsonConfiguration provideGsonModule() {
        return mGsonConfiguration;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.LoggerConfiguration loggerConfiguration;
        private ClientModule.GsonConfiguration gsonConfiguration;

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

        public Builder gsonConfiguration(ClientModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }

    }

}

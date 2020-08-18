package me.csxiong.library.integration.http;

import android.annotation.SuppressLint;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Desc : 简单封装OKHttp
 * 1.目的在于简化逻辑
 * 2.满足需求简化 主要服务ContentType是Json的数据返回
 * <p>
 * 超级轻便 维护简单
 * Author : csxiong - 2019/6/21
 */
public class XHttp {

    private static volatile XHttp instance;

    private Config config;

    public static XHttp getInstance() {
        synchronized (XHttp.class) {
            if (instance == null) {
                instance = new XHttp();
            }
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        checkInit();
        return config.mOkHttpClient;
    }

    /**
     * 拉取本地配置
     *
     * @return
     */
    public Config getConfig() {
        checkInit();
        return config;
    }

    /**
     * http 服务拉取
     * tip:provider内部缓存service,外部获取可不必缓存
     *
     * @param serviceClasses
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> serviceClasses) {
        return ServiceProvider.getInstance().get(serviceClasses);
    }

    /**
     * 发起get
     *
     * @param requestMapping
     * @return
     */
    public GetRequest get(String requestMapping) {
        checkInit();
        return new GetRequest(requestMapping);
    }

    /**
     * 发起post
     *
     * @param requestMapping
     * @return
     */
    public PostRequest post(String requestMapping) {
        checkInit();
        return new PostRequest(requestMapping);
    }

    /**
     * 取消对应Tag的任务
     *
     * @param tag
     */
    public void cancelRequests(Object tag) {
        if (tag == null) {
            return;
        }
        List<Call> calls = getHttpClient().dispatcher().queuedCalls();
        for (Call call : calls) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 是否在请求中
     *
     * @param tag
     */
    public boolean isRequesting(String tag) {
        if (tag == null) {
            return false;
        }
        List<Call> calls = getHttpClient().dispatcher().queuedCalls();
        for (Call call : calls) {
            if (tag.equals(call.request().tag())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("RestrictedApi")
    public static void init(Config builder) {
        Preconditions.checkNotNull(builder);
        getInstance().config = builder;
    }

    public boolean isInit() {
        return config != null && config.mOkHttpClient != null;
    }

    private void checkInit() {
        if (!isInit()) {
            Preconditions.error("check has init before use it!");
        }
    }

    /**
     * XHttp config 可再扩展需要参数
     */
    public static class Config {

        private OkHttpClient.Builder mOkBuilder;

        private OkHttpClient mOkHttpClient;

        private AbsRequest.RequestBuildAdapter mRequestBuildAdapter;

        private String mainHost;

        public Config() {
            mOkBuilder = new OkHttpClient.Builder();
        }

        public Config setDefaultRequestBuildAdapter(AbsRequest.RequestBuildAdapter requestBuildAdapter) {
            this.mRequestBuildAdapter = requestBuildAdapter;
            return this;
        }

        public Config addInterceptors(Interceptor interceptor) {
            mOkBuilder.addNetworkInterceptor(interceptor);
            return this;
        }

        public Config mainHost(String mainHost) {
            this.mainHost = mainHost;
            return this;
        }

        public Config connectTimeout(long seconds) {
            mOkBuilder.connectTimeout(seconds, TimeUnit.SECONDS);
            return this;
        }

        public AbsRequest.RequestBuildAdapter getDefaultRequestBuildAdapter() {
            return mRequestBuildAdapter;
        }

        public String getMainHost() {
            return mainHost;
        }

        public Config apply() {
            mOkBuilder.cache(null);
            mOkHttpClient = mOkBuilder.build();
            return this;
        }
    }

}

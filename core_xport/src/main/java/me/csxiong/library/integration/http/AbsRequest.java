package me.csxiong.library.integration.http;

import android.util.Log;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import me.csxiong.library.BuildConfig;
import me.csxiong.library.utils.GsonUtils;
import me.csxiong.library.utils.ThreadExecutor;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Desc : 抽象请求,并扩展构造
 * @Author : Bear - 2020/8/18
 */
public abstract class AbsRequest {

    private static final String TAG = AbsRequest.class.getSimpleName();

    /**
     * okhttp请求构造
     */
    private Request.Builder requestBuilder;

    /**
     * okhttp请求实体
     */
    private Request request;

    /**
     * 请求mapping-带host检测替换
     */
    private String requestMapping;

    /**
     * type for T
     */
    private Type typeT;

    /**
     * 参数集
     */
    private HashMap<String, Object> parameters = new HashMap<>(4);

    /**
     * 响应默认回到主线程
     */
    private boolean isResponseOnMain = true;

    /**
     * 是否同步请求
     */
    private boolean isSyncRequest = false;

    /**
     * 是否应用默认参数适配器
     */
    private boolean isApplyDefaultParametersAdapter = true;

    /**
     * 响应监听
     */
    private ResponseListener responseListener;


    public AbsRequest(String requestMapping) {
        Preconditions.checkNotNull(requestMapping);
        requestBuilder = new Request.Builder();
        this.requestMapping = requestMapping;
    }

    /**
     * 添加Tag
     *
     * @param tag
     * @return
     */
    public AbsRequest addTag(String tag) {
        requestBuilder.tag(tag);
        return this;
    }

    /**
     * 请求头部参数拼接
     *
     * @param key
     * @param value
     * @return
     */
    public AbsRequest addHeader(String key, String value) {
        requestBuilder.addHeader(key, value);
        return this;
    }

    /**
     * 请求头部参数拼接
     *
     * @return
     */
    public AbsRequest addHeaders(Headers headers) {
        requestBuilder.headers(headers);
        return this;
    }


    /**
     * 参数添加
     *
     * @param key
     * @param value
     * @return
     */
    public AbsRequest addParameters(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    /**
     * 参数添加
     *
     * @param multiParameters
     * @return
     */
    public AbsRequest addParameters(MultiParameters multiParameters) {
        multiParameters.onMultiParameters(parameters);
        return this;
    }

    /**
     * 构建Adapter
     *
     * @param requestBuildAdapter
     * @return
     */
    public AbsRequest addRequestAdapter(RequestBuildAdapter requestBuildAdapter) {
        if (requestBuildAdapter != null) {
            requestBuildAdapter.onBuild(requestBuilder, parameters);
        }
        return this;
    }

    /**
     * 移除默认的请求适配器
     *
     * @return
     */
    public AbsRequest removeDefaultRequestAdapter() {
        isApplyDefaultParametersAdapter = false;
        return this;
    }

    /**
     * 在UI线程订阅
     *
     * @return
     */
    public AbsRequest observeOnMainThread() {
        isResponseOnMain = true;
        return this;
    }

    /**
     * 在子线程订阅
     *
     * @return
     */
    public AbsRequest observeOnBackground() {
        isResponseOnMain = false;
        return this;
    }

    public AbsRequest syncRequest(boolean isSync) {
        isSyncRequest = isSync;
        return this;
    }

    /**
     * 发起请求主要流程和控制
     *
     * @param responseListener 响应参数
     */
    public <T> void execute(@Nullable ResponseListener<T> responseListener) {
        if (isApplyDefaultParametersAdapter) {
            addRequestAdapter(XHttp.getInstance().getConfig().getDefaultRequestBuildAdapter());
        }
        Runnable executable = () -> {
            // 1.response receiver
            AbsRequest.this.responseListener = responseListener;
            if (responseListener != null && responseListener.getClass().getGenericSuperclass() instanceof ParameterizedType) {
                ParameterizedType parameterizedType =
                        (ParameterizedType) responseListener.getClass().getGenericSuperclass();
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                typeT = actualTypeArguments[0];
            } else {
                typeT = Object.class;
            }
            String methodType = getMethodType();
            checkMethodTypeEnable(methodType);
            // 2.request create
            request = onCreateRequest(requestBuilder, checkAutoHostEnable(requestMapping) ? requestMapping
                    : XHttp.getInstance().getConfig().getMainHost() + requestMapping, parameters);
            if (request != null && BuildConfig.DEBUG) {
                Logger.e(request.url().toString());
            }
            Preconditions.checkNotNull(request);
            // 3.execute request
            Response response = onExecuteRequest(request);
            // 4.parse response
            onParseResponse(response);
        };
        // 5.execute
        if (!isSyncRequest) {
            ThreadExecutor.runOnSlowBackgroundThread(executable);
        } else {
            executable.run();
        }
    }

    /**
     * 当前默认子线程发起
     * 可修改配置 修改订阅线程
     *
     * @param request
     */
    private Response onExecuteRequest(Request request) {
        Response response;
        try {
            if (XHttp.getInstance().getConfig().isLogFullPath()) {
                Log.e(Preconditions.TAG, "XHttp request -> " + request.toString());
            }
            response = XHttp.getInstance().getHttpClient().newCall(request).execute();
            return response;
        } catch (Exception e) {
            onParseError(e);
            return null;
        }
    }

    /**
     * 解析异常
     * 1.线程切换
     *
     * @param throwable 异常
     */
    private void onParseError(Throwable throwable) {
        if (responseListener == null) {
            return;
        }
        if (throwable != null) {
            if (BuildConfig.DEBUG) {
                Logger.e(throwable.getMessage());
            }
        }
        if (isResponseOnMain && !ThreadExecutor.isUIThread()) {
            ThreadExecutor.runOnUiThread(() -> responseListener.onError(throwable));
            return;
        }
        responseListener.onError(throwable);
    }

    /**
     * 解析数据
     * 1.线程切换
     *
     * @param o 目前已解析的数据
     */
    private void onParseData(Object o) {
        // 子线程切换
        if (responseListener == null) {
            return;
        }
        if (isResponseOnMain && !ThreadExecutor.isUIThread()) {
            ThreadExecutor.runOnUiThread(() -> {
                responseListener.onNext(o);
                responseListener.onComplete();
            });
            return;
        }
        responseListener.onNext(o);
        responseListener.onComplete();
    }

    /**
     * 解析响应 对原生response不再包装 返回实体 可自定义操作
     * 1.目前功能仅添加线程切换
     * <p>
     * update by csxiong : 做完完整数据解析,切换线程
     *
     * @param response
     */
    private void onParseResponse(Response response) {
        if (response == null || response.body() == null) {
            return;
        }
        if (responseListener == null) {
            return;
        }
        // 直接返回解析响应值
        if (responseListener.onConvertResponse(response)) {
            return;
        }
        try {
            // 解析响应
            if (response.code() != ApiCode.OK) {
                onParseError(new HttpException(response.code(), response.message()));
                return;
            }
            String jsonString = response.body().string();
            Logger.json(jsonString);
            if (String.class.equals(typeT)) {
                onParseData(jsonString);
            } else {
                Object o = GsonUtils.fromJson(jsonString, typeT);
                onParseData(o);
            }
        } catch (Exception e) {
            onParseError(e);
        }
    }

    /**
     * 方法检测
     * 目前开放GET or POST
     * DELETE 、UPDATE可补充
     *
     * @param methodType
     */
    private void checkMethodTypeEnable(String methodType) {
        if (!methodType.equalsIgnoreCase("GET") && !methodType.equalsIgnoreCase("POST")) {
            Preconditions.error("only support get or post method request");
        }
    }

    /**
     * 服务器地址 or IP检查,后期有需要提供whiteListCheck
     *
     * @param requestMapping 请求的Mapping
     * @return
     */
    private boolean checkAutoHostEnable(String requestMapping) {
        if (requestMapping.startsWith("http://") || requestMapping.startsWith("https://")) {
            return true;
        }
        return false;
    }

    protected abstract String getMethodType();

    protected abstract Request onCreateRequest(Request.Builder requestBuilder, String fullRequestMapping,
                                               Map<String, Object> parameters);

    /**
     * 直接提供参数Map提供操作参数集
     */
    public interface MultiParameters {
        void onMultiParameters(Map<String, Object> parameters);
    }

    /**
     * 直接提供构造,目的在于提供对参数集的默认配置
     * 例如：默认部分API接入参数配置
     * 保证加签过程对开发者隐藏，避错
     */
    public interface RequestBuildAdapter {
        void onBuild(Request.Builder builder, Map<String, Object> parameters);
    }

}

package me.csxiong.library.utils;


import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import me.csxiong.library.base.APP;
import me.csxiong.library.integration.http.ApiException;
import me.csxiong.library.integration.http.Response;

/**
 * @Desc : RxJava工具,主要是线程切换和数据解析,主要针对Http请求
 * @Author : csxiong create on 2019/7/16
 */
public class RxUtils {

    /**
     * 切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> onRxThread() {
        return observable -> observable.subscribeOn(APP.get().getAppComponent().provideScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 订阅事件源触发方式
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Observable<T> formatData(final T t) {
        return Observable.create(emitter -> emitter.onNext(t));
    }

    /**
     * 获取结果转换
     *
     * @param transformer
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Response<T>, T> onHandleResult(ObservableTransformer<Response<T>, T> transformer) {
        return transformer;
    }

    /**
     * 默认http消息转换 就是Response<T>转T,期间对response进行预处理,如果请求码不正确,抛出ApiException
     * 框架内默认Response结构
     * 按需求,可逐步修改Response为具体业务项目中Response响应的实体
     * 转换逻辑也不相同
     * ps : 这里只是提供默认,不广适用
     *
     * @param <T>
     */
    public static class DefaultApiHandleResult<T> implements ObservableTransformer<Response<T>, T> {
        @Override
        public Observable<T> apply(Observable<Response<T>> httpResponseFlowable) {
            return httpResponseFlowable.flatMap((Function<Response<T>, Observable<T>>) response -> {
                if (response.getErrcode() == 200) {
                    return formatData(response.getData());
                } else {
                    return Observable.error(new ApiException(TextUtils.isEmpty(response.getMessage())
                            ? "服务器错误" : response.getMessage()));
                }
            });
        }
    }

}

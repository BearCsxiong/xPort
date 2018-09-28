package me.csxiong.library.utils;

import org.jsoup.helper.StringUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.csxiong.library.base.IView;
import me.csxiong.library.integration.http.ApiException;
import me.csxiong.library.integration.http.Response;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : rxjava封装方法
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/9/28 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class RxUtils {

    /**
     * Http默认请求转换模板
     * 1.线程切换
     * 2.生命周期管理
     * 3.结果接收转换
     * <p>
     * ps : 此方法不广适用,此为设计业务系统请求转换的模板,待开发者自己整理一套自己的转换逻辑
     * <p>
     * 含有多个请求嵌套的情况最适合使用FlowableTransformer进行内容转换链式请求结果
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Response<T>, T> doDefaultHttpTransformer(final IView view,Class<T> classes) {
        return new ObservableTransformer<Response<T>, T>() {
            @Override
            public Observable<T> apply(Observable<Response<T>> observable) {
                return observable
                        .compose(RxUtils.onHandleResult(new DefaultApiHandleResult()))
                        .compose(RxLifecycleUtil.bindUntilEvent(view))
                        .compose(RxUtils.onRxThread());
            }
        };
    }

    /**
     * 切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> onRxThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 订阅事件源触发方式
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Observable<T> formatData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(t);
            }
        });
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
            return httpResponseFlowable.flatMap(new Function<Response<T>, Observable<T>>() {
                @Override
                public Observable<T> apply(Response<T> response) {
                    if (response.getErrcode() == 200) {
                        return formatData(response.getData());
                    } else {
                        return Observable.error(new ApiException(StringUtil.isBlank(response.getMessage())
                                ? "服务器错误" : response.getMessage()));
                    }
                }
            });
        }
    }

}

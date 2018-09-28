package me.csxiong.library.utils;

import org.jsoup.helper.StringUtil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
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
    public static <T> FlowableTransformer<Response<T>, T> doDefaultHttpTransformer(final IView view,Class<T> dataClass) {
        return new FlowableTransformer<Response<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<Response<T>> observable) {
                return observable
                        .compose(RxUtils.onRxThread())
                        .compose(RxLifecycleUtil.bindUntilEvent(view))
                        .compose(RxUtils.onHandleResult(new DefaultApiHandleResult()));
            }
        };
    }

    /**
     * 切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> onRxThread() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
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
    private static <T> Flowable<T> formatData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                } catch (Exception e) {
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 获取结果转换
     *
     * @param transformer
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<Response<T>, T> onHandleResult(FlowableTransformer<Response<T>, T> transformer) {
        return transformer;
    }

    /**
     * 获取结果转换
     *
     * @param flatMap
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> FlowableTransformer<R, T> getHandleResultDefault(final Function<R, Flowable<T>> flatMap) {
        return new FlowableTransformer<R, T>() {
            @Override
            public Flowable<T> apply(Flowable<R> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(flatMap);
            }
        };
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
    public static class DefaultApiHandleResult<T> implements FlowableTransformer<Response<T>, T> {
        @Override
        public Flowable<T> apply(Flowable<Response<T>> httpResponseFlowable) {
            return httpResponseFlowable.flatMap(new Function<Response<T>, Flowable<T>>() {
                @Override
                public Flowable<T> apply(Response<T> response) {
                    if (response.getErrcode() == 200) {
                        return formatData(response.getData());
                    } else {
                        return Flowable.error(new ApiException(StringUtil.isBlank(response.getMessage())
                                ? "服务器错误" : response.getMessage()));
                    }
                }
            });
        }
    }

}

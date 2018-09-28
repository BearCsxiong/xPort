package me.csxiong.library.utils;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
     * 切换线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
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
    public static <T> Flowable<T> formatData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
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
    public static <T> FlowableTransformer<Response<T>, T> handleResult(FlowableTransformer transformer) {
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
}

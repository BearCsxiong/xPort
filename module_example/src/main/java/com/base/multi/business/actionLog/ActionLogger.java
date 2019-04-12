package com.base.multi.business.actionLog;

import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by csxiong on 2018/10/27.
 */

public class ActionLogger {

    final static ConcurrentLinkedQueue<String> actionQueue = new ConcurrentLinkedQueue<>();

    private static boolean isWork = false;

    public static void write(final String action) {
        synchronized (actionQueue) {
            actionQueue.add(action);
            if (isWork) {
                return;
            }
        }
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                if (emitter == null) {
                    Log.e("tag", "emitter is null");
                }
                isWork = true;
                while (actionQueue.size() != 0) {
                    emitter.onNext(actionQueue.poll());
                }
                emitter.onComplete();
                isWork = false;
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        try {
                            Thread.sleep(2000);
                            Log.e("tag", s);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("tag", t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("tag", "onComplete");
                    }
                });
//                .compose(new FlowableTransformer<Object, File>() {
//                    @Override
//                    public Publisher<File> apply(Flowable<Object> upstream) {
//                        return upstream.flatMap(new Function<Object, Publisher<File>>() {
//                            @Override
//                            public Publisher<File> apply(Object o) throws Exception {
//                                //文件o写入文件
//                                final File file = new File("");
//                                return Flowable.create(new FlowableOnSubscribe<File>() {
//                                    @Override
//                                    public void subscribe(FlowableEmitter<File> emitter) throws Exception {
//                                        emitter.onNext(file);
//                                    }
//                                }, BackpressureStrategy.BUFFER);
//                            }
//                        });
//                    }
//                })
//                .compose(new FlowableTransformer<File, File>() {
//                    @Override
//                    public Publisher<File> apply(Flowable<File> upstream) {
//                        return upstream.flatMap(new Function<File, Publisher<File>>() {
//                            @Override
//                            public Publisher<File> apply(File file) throws Exception {
//                                return Flowable.create(new FlowableOnSubscribe<File>() {
//
//                                    @Override
//                                    public void subscribe(FlowableEmitter<File> emitter) throws Exception {
//
//                                    }
//                                }, BackpressureStrategy.BUFFER);
//                            }
//                        });
//                    }
//
//                })
//                .subscribeWith(new ResourceSubscriber<File>() {
//
//                    @Override
//                    public void onNext(File o) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

}

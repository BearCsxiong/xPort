package me.csxiong.library.integration.http;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by csxiong on 2019/2/11.
 */

public class HttpLogger implements HttpLoggingInterceptor.Logger {

    private StringBuilder mMessage = new StringBuilder();

    private String tag;

    public HttpLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void log(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        // 请求或者响应开始
        if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
            mMessage.setLength(0);
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {

            message = GsonUtils.toJson(message);
        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            Logger.t(tag).d(mMessage.toString());
        }
    }

    /**
     * 获取一个打印拦截器
     *
     * @return
     */
    public static HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger(Preconditions.TAG));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
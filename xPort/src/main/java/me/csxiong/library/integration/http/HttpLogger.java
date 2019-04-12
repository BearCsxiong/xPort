package me.csxiong.library.integration.http;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import me.csxiong.library.utils.GsonUtils;
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
            message = GsonUtils.formatJson(message);
        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            Logger.t(tag).d(mMessage.toString());
        }
    }
}

package me.csxiong.library.integration.http;

import android.net.Uri;

import java.util.Map;

import okhttp3.Request;

/**
 * Desc : GET请求发起模式模式
 * Author : csxiong - 2019/6/24
 */
public class GetRequest extends AbsRequest {

    public GetRequest(String requestMapping) {
        super(requestMapping);
    }

    @Override
    protected String getMethodType() {
        return "get";
    }

    @Override
    protected Request onCreateRequest(Request.Builder requestBuilder, String fullRequestMapping,
                                      Map<String, Object> parameters) {
        Uri.Builder builder = Uri.parse(fullRequestMapping).buildUpon();
        for (String key : parameters.keySet()) {
            Object object = parameters.get(key);
            builder.appendQueryParameter(key, String.valueOf(object));
        }
        return requestBuilder.get().url(builder.build().toString()).build();
    }
}

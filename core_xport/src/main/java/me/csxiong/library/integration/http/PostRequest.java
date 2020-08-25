package me.csxiong.library.integration.http;

import java.util.Map;

import me.csxiong.library.utils.GsonUtils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Desc : Post请求发起模式
 * Author : csxiong - 2019/6/24
 */
public class PostRequest extends AbsRequest {

    public PostRequest(String requestMapping) {
        super(requestMapping);
    }

    @Override
    protected String getMethodType() {
        return "post";
    }

    @Override
    protected Request onCreateRequest(Request.Builder requestBuilder, String fullRequestMapping,
                                      Map<String, Object> parameters) {
        requestBuilder.url(fullRequestMapping);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, GsonUtils.toJson(parameters));
        return requestBuilder.url(fullRequestMapping).post(body).build();
    }
}

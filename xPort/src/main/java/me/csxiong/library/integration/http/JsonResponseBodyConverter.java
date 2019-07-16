package me.csxiong.library.integration.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @Desc : 响应体装换
 * @Author : csxiong create on 2019/7/16
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson mGson;
    private final TypeAdapter<T> adapter;

    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        return adapter.fromJson(json);
    }

}
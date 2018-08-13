package me.csxiong.library.integration.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        return adapter.fromJson(json);
    }

}
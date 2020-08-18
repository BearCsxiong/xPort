package me.csxiong.library.integration.http;

import com.google.gson.annotations.SerializedName;

/**
 * @Desc : 网络请求结果
 * @Author : Bear - 2020/8/13
 */
public class HttpResult<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("msg")
    private T data;

    @SerializedName("type")
    private String type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnable() {
       return status == 200;
    }
}

package me.csxiong.library.integration.http;

/**
 * @Desc : 数据响应体,主要提供数据转换,可根据后台结构独立配置,使用配置{@link me.csxiong.library.utils.RxUtils}
 * @Author : csxiong create on 2019/7/16
 */
public class Response<T> implements java.io.Serializable {

    /**
     * 响应码
     */
    private int errcode;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应实体
     */
    private T data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return 200 == errcode;
    }
}

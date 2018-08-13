package me.csxiong.library.integration.http;

/**
 * 请求实体的基类
 */
public class Response<T> implements java.io.Serializable{
    private int errcode;
    private String message;
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

    public boolean isSuccess(){
        return 200==errcode;
    }
}

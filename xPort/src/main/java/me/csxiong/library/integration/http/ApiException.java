package me.csxiong.library.integration.http;

/**
 * @Desc : 自定义Api异常
 * @Author : csxiong create on 2019/7/16
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

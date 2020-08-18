package me.csxiong.library.integration.http;

/**
 * Desc : 网络异常
 * Author : csxiong - 2019/6/21
 */
public class HttpException extends Exception {

    private int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

package me.csxiong.library.integration.http;

/**
 * Desc : 基础code 最好平台统一,需要服务端支持统一(不同与Http stateCode,这个是服务端定义code)
 * <p>
 * Author : csxiong - 2019/6/24
 */
public @interface ApiCode {

    /**
     * 响应码200-成功响应s
     */
    int OK = 200;
    int FAIL = -1;
}

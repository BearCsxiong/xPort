package me.csxiong.library.integration.http;

import java.lang.reflect.Type;

/**
 * @Desc : 解析数据适配器
 * @Author : Bear - 2020/9/14
 */
public interface ParseDataAdapter {

    /**
     * 解析数据
     *
     * @param jsonStr
     * @return
     */
    Object onParseData(String jsonStr, Type typeT);

}

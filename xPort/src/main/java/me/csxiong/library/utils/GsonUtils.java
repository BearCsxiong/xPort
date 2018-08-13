package me.csxiong.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 */

public class GsonUtils {
    private static Gson gson;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }


    /**
     * 对象转Json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        checkGson();
        return gson.toJson(object);
    }

    /**
     * 字符串转Json对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        checkGson();
        return gson.fromJson(json, clazz);
    }


    /**
     * @des 检测转化工具对象
     * @author Yancy Lin
     * @date 2017/8/14 上午11:22
     */
    private static void checkGson() {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * @des 去除Bom头与格式多余
     * @author Yancy Lin
     * @date 2017/8/21 下午3:14
     */
    public static String JSONTokener(String str_json) {
        String istr_json;
        // consume an optional byte order mark (BOM) if it exists
        if (str_json != null && str_json.startsWith("\ufeff")) {
            istr_json = str_json.substring(1);
        }
//        如果多出"与/，去除
        str_json=str_json.replace("\"{","{");
        str_json=str_json.replace("}\"","}");
        str_json=str_json.replace("\\","");

        return str_json;
    }

    /**
     * @des 去除格式多余"和/
     * @author Yancy Lin
     * @date 2017/8/21 下午3:14
     */
    public static String JSONTokener2(String str_json) {
//        如果多出"与/，去除
        str_json=str_json.replace("\\","");
        str_json=str_json.replace("&",",");
        str_json=str_json.replace("\"\"","");
//        str_json=str_json.replace("}\"","}");
//        str_json=str_json.replace("\\","");

        return str_json;
    }

    /**
     * @des 转化成JsonObject
     * @author Yancy Lin
     * @date 2017/8/30 下午4:20
     */
    public static JsonObject toJsonObject(Object obj){
        return gson.toJsonTree(obj).getAsJsonObject();
    }

}

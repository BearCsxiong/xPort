package me.csxiong.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : gson 对json格式数据转换工具
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/9/28 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class GsonUtils {

    /**
     * 单例content
     */
    private static Gson gson;

    /**
     * 默认创建Gson提供使用
     */
    static {
        gson = new Gson();
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
     * @date 2017/8/14 上午11:22
     */
    private static void checkGson() {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * @des 转化成JsonObject
     * @date 2017/8/30 下午4:20
     */
    public static JsonObject toJsonObject(Object obj) {
        return gson.toJsonTree(obj).getAsJsonObject();
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

}

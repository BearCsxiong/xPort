package me.csxiong.library.integration.http;

/**
 * Desc : 简单service工厂
 * Author : csxiong - 2019/6/24
 */
public class ServiceFactory {

    /**
     * 创建service方法
     */
    public static <T> T create(Class<T> classes) {
        Preconditions.checkNotNull(classes);
        T t = null;
        try {
            t = classes.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }
}

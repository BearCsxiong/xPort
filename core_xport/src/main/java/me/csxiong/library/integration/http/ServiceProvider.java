package me.csxiong.library.integration.http;

import android.util.LruCache;

/**
 * Desc : 服务提供者 其实在日后管理中就是DBModel ApiModel FileModel中的ApiModel的角色 看量级扩展
 * Author : csxiong - 2019/6/24
 */
public class ServiceProvider {

    /**
     * 最近最少使用缓存
     */
    private LruCache<String, Object> serviceCache = new LruCache<>(10);

    private static ServiceProvider serviceProvider;

    public static ServiceProvider getInstance() {
        synchronized (ServiceProvider.class) {
            if (serviceProvider == null) {
                serviceProvider = new ServiceProvider();
            }
            return serviceProvider;
        }
    }

    private ServiceProvider() {
    }

    /**
     * 默认拉取service的方法
     *
     * @param classes
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> classes) {
        Object o = serviceCache.get(classes.getSimpleName());
        if (o != null) {
            return (T) o;
        }
        T t = ServiceFactory.create(classes);
        serviceCache.put(classes.getSimpleName(), t);
        return t;
    }

}

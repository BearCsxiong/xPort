package me.csxiong.library.base;

import java.util.HashMap;
import java.util.Map;

import dagger.Lazy;

/**
 * @Desc : XViewModel仓库,使用于Dagger2创建的ViewModel
 * @Author : csxiong create on 2019/8/22
 */
public class DaggerViewModelStore {

    /**
     * 简单的ViewModel对应的map
     */
    private Map<Class, XViewModel> xViewModelMap = new HashMap<>();

    /**
     * 生成ViewModel规则
     *
     * @param tClass
     * @param tLazy
     * @param <T>
     * @return
     */
    public <T extends XViewModel> T get(Class<T> tClass, Lazy<T> tLazy) {
        XViewModel xViewModel = xViewModelMap.get(tClass);
        if (tClass.isInstance(xViewModel)) {
            return (T) xViewModelMap.get(tClass);
        }
        T t = tLazy.get();
        xViewModelMap.put(tClass, t);
        return t;
    }

    /**
     * 清理ViewModelMap
     */
    public void clear() {
        if (xViewModelMap != null) {
            xViewModelMap.clear();
        }
    }
}

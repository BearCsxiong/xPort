package me.csxiong.camera;

import java.util.HashMap;

/**
 * @Desc : 数据提供者
 * @Author : csxiong - 2019-11-22
 */
public class DataProviders {

    private HashMap<Integer, Object> mDatas = new HashMap<>(8);

    public DataProviders() {
    }

    public boolean getBoolean(int dataCode, boolean defaultt) {
        Object data = mDatas.get(dataCode);
        if (data instanceof Boolean) {
            return (boolean) data;
        }
        return defaultt;
    }

    public void putBoolean(int dataCode, boolean value) {
        mDatas.put(dataCode, value);
    }

}

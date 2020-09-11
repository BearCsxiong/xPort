package me.csxiong.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc : SP配置类
 * @Author : Bear - 2020/9/11
 */
public class XSPConfig {

    private static final int INT_COMMIT = 1;
    private static final int FLOAT_COMMIT = 2;
    private static final int LONG_COMMIT = 3;
    private static final int STRING_COMMIT = 4;
    private static final int BOOLEAN_COMMIT = 5;

    @IntDef({INT_COMMIT, FLOAT_COMMIT, LONG_COMMIT, STRING_COMMIT, BOOLEAN_COMMIT})
    @interface SPType {

    }

    private SharedPreferences mSharedPreferences;
    private static HandlerThread sHandlerThread;
    private Handler mHandler;
    private ConcurrentSPHelper<Integer> mIntSPHelper = new ConcurrentSPHelper<>(INT_COMMIT);
    private ConcurrentSPHelper<Float> mFloatSPHelper = new ConcurrentSPHelper<>(FLOAT_COMMIT);
    private ConcurrentSPHelper<Boolean> mBooleanSPHelper = new ConcurrentSPHelper<>(BOOLEAN_COMMIT);
    private ConcurrentSPHelper<String> mStringSPHelper = new ConcurrentSPHelper<>(STRING_COMMIT);
    private ConcurrentSPHelper<Long> mLongSPHelper = new ConcurrentSPHelper<>(LONG_COMMIT);

    class ConcurrentSPHelper<V> {
        /**
         * SP操作时的单位类。
         * @param <V>
         */
        class SPNode<V> {
            private final String key;
            private volatile V value;

            SPNode(String k, V v) {
                key = k;
                value = v;
            }
        }

        /**缓存未操作的Map*/
        private volatile ConcurrentHashMap<String, SPNode<V>> cacheMap = new ConcurrentHashMap<>();
        /**SP操作的基础类型*/
        private @SPType int spType;

        public ConcurrentSPHelper(@SPType int type) {
            spType = type;
        }

        public V get(String key) {
            SPNode<V> spNode = cacheMap.get(key);
            return spNode != null ? spNode.value : null;
        }

        public void put(String key, V value) {
            if (TextUtils.isEmpty(key)) {
                return;
            }
            SPNode<V> spNode = cacheMap.get(key);
            if (spNode == null || spNode.key == null || spNode.value == null) {
                cacheMap.put(key, new SPNode<>(key, value));
                mHandler.sendMessage(Message.obtain(mHandler, spType, key));
            } else {
                spNode.value = value;
            }
        }

        public void commit(String key) {
            SPNode<V> spNode = cacheMap.get(key);
            if (spNode != null && spNode.value != null && spNode.key != null) {
                spNode = cacheMap.remove(key);
                Editor editor = mSharedPreferences.edit();
                if (spNode.value instanceof Integer) {
                    editor.putInt(key, (Integer) spNode.value);
                    editor.commit();
                } else if (spNode.value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) spNode.value);
                    editor.commit();
                } else if (spNode.value instanceof Long) {
                    editor.putLong(key, (Long) spNode.value);
                    editor.commit();
                } else if (spNode.value instanceof Float) {
                    editor.putFloat(key, (Float) spNode.value);
                    editor.commit();
                } else if (spNode.value instanceof String) {
                    editor.putString(key, (String) spNode.value);
                    editor.commit();
                }
            }
        }

    }

    static {
        sHandlerThread = new HandlerThread("SpCommitThread");
        sHandlerThread.start();
    }

    /**
     * 
     * @param context
     * @param name
     *            配置文件名
     */
    public XSPConfig(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mHandler = new Handler(sHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg == null || !(msg.obj instanceof String)) {
                    return;
                }
                String key = (String) msg.obj;
                switch (msg.what) {
                    case INT_COMMIT: {
                        mIntSPHelper.commit(key);
                        break;
                    }
                    case FLOAT_COMMIT: {
                        mFloatSPHelper.commit(key);
                        break;
                    }
                    case LONG_COMMIT: {
                        mLongSPHelper.commit(key);
                        break;
                    }
                    case BOOLEAN_COMMIT: {
                        mBooleanSPHelper.commit(key);
                        break;
                    }
                    case STRING_COMMIT: {
                        mStringSPHelper.commit(key);
                        break;
                    }
                }
            }
        };
    }

    /**
     * 获取long数据
     * 
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue) {
        if (TextUtils.isEmpty(key)){
            return defValue;
        }

        try {
            Long cache = mLongSPHelper.get(key);
            if (cache != null) {
                return cache;
            }
            return mSharedPreferences.getLong(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            remove(key);
            return defValue;
        }
    }

    /**
     * 存储long数据
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, long value) {
        if (TextUtils.isEmpty(key)){
            return false;
        }

        mLongSPHelper.put(key, value);
        return true;
    }

    /**
     * 获取int数据
     * 
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        try {
            Integer cache = mIntSPHelper.get(key);
            if (cache != null) {
                return cache;
            }
            return mSharedPreferences.getInt(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            remove(key);
            return defValue;
        }
    }

    /**
     * 存储int数据
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        mIntSPHelper.put(key, value);
        return true;
    }

    /**
     * 获取String数据
     * 
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        try {
            String cache = mStringSPHelper.get(key);
            if (cache != null) {
                return cache;
            }
            return mSharedPreferences.getString(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            remove(key);
            return defValue;
        }
    }

    /**
     * 存储String数据
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        String noNullValue = value == null ? "" : value;
        mStringSPHelper.put(key, noNullValue);
        return true;
    }

    /**
     * 获取boolean数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        try {
            Boolean cache = mBooleanSPHelper.get(key);
            if (cache != null) {
                return cache;
            }
            return mSharedPreferences.getBoolean(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            remove(key);
            return defValue;
        }
    }

    /**
     * 存储boolean数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        mBooleanSPHelper.put(key, value);
        return true;
    }

    /**
     * 获取boolean数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public float getFloat(String key, float defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        try {
            Float cache = mFloatSPHelper.get(key);
            if (cache != null) {
                return cache;
            }
            return mSharedPreferences.getFloat(key, defValue);
        } catch (Exception e) {
            // TODO: handle exception
            remove(key);
            return defValue;
        }
    }

    /**
     * Float
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, float value) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        mFloatSPHelper.put(key, value);
        return true;
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    /**
     * 移除某一项数据
     * 
     * @param key
     * @return
     */
    public boolean remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    public void clear() {
        Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 是否包含特定Key的数据。
     * 
     * @param key 键值
     * @return 包含
     */
    public boolean contains(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return mSharedPreferences.contains(key);
    }

    /***************************************************静态方法,建议在子线程中使用******************************************************************************/

    public static long getLong(Context context, final String tableName, final String key) {
        return getValue(context, tableName, key, 0);
    }

    public static String getString(Context context, final String tableName, final String key) {
        return getValue(context, tableName, key, null);
    }

    public static int getInt(Context context, final String tableName, final String key) {
        return getValue(context, tableName, key, 0);
    }

    public static boolean getBoolean(Context context, final String tableName, final String key) {
        return getValue(context, tableName, key, false);
    }

    public static float getFloat(Context context, final String tableName, final String key) {
        return getValue(context, tableName, key, 0);
    }

    public static long getValue(Context context, final String tableName, final String key, final long defValue) {
        if (context == null) {
            return defValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }

        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static String getValue(Context context, final String tableName, final String key, final String defValue) {
        if (context == null) {
            return defValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static int getValue(Context context, final String tableName, final String key, final int defValue) {
        if (context == null) {
            return defValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static boolean getValue(Context context, final String tableName, final String key, final boolean defValue) {
        if (context == null) {
            return defValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static float getValue(Context context, final String tableName, final String key, final float defValue) {
        if (context == null) {
            return defValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).getFloat(key, defValue);
    }

    public static boolean putValue(Context context, final String tableName, final String key, final long defValue) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).edit().putLong(key, defValue).commit();
    }

    public static boolean putValue(Context context, final String tableName, final String key, final String defValue) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).edit().putString(key, defValue).commit();
    }

    public static boolean putValue(Context context, final String tableName, final String key, final int defValue) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).edit().putInt(key, defValue).commit();
    }

    public static boolean putValue(Context context, final String tableName, final String key, final boolean defValue) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).edit().putBoolean(key, defValue).commit();
    }

    public static boolean putValue(Context context, final String tableName, final String key, final float defValue) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return context.getSharedPreferences(tableName, Context.MODE_PRIVATE).edit().putFloat(key, defValue).commit();
    }
}

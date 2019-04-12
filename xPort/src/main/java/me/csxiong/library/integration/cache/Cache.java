package me.csxiong.library.integration.cache;

/**-------------------------------------------------------------------------------
*|
*| desc : 缓存基本方法,待实现
*|
*|--------------------------------------------------------------------------------
*| on 2018/8/20 created by csxiong
*|--------------------------------------------------------------------------------
*/
public interface Cache<K, V> {

    /**
     *
     * @param key key
     * @return the value or {@code null}.
     */
    V get(K key);

    /**
     *
     * @param key   key
     * @param value image
     * @return the previous value.
     */
    V put(K key, V value);

    /**
     *
     * @return the previous value or @{code null}.
     */
    V remove(K key);

    /**
     */
    void clear();

    /**
     *
     * @return max memory size.
     */
    int getMaxMemorySize();

    /**
     *
     * @return current memory size.
     */
    int getMemorySize();

}
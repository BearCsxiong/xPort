package me.csxiong.library.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Desc : 集合相关工具
 * @Author : csxiong - 2020-01-29
 */
public class CollectionUtils {
    /**
     * Map中的Value转List。
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<V> transMapValueToList(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<V> valueCollection = map.values();
        return new ArrayList<>(valueCollection);
    }

    /**
     * 判断列表是否为空。
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断Map是否为空。
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 比较两个列表是否相同。
     *
     * @param list1
     * @param list2
     * @return
     */
    public static boolean compare(List list1, List list2) {
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!Objects.equals(list1.get(i), list2.get(2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断Position是否在列表的范围里。
     *
     * @param list
     * @param position
     * @return
     */
    public static boolean inRange(List list, int position) {
        return list != null && position > -1 && position < list.size();
    }

    /**
     * 判断position是否在数组中。
     *
     * @param arrays
     * @param position
     * @return
     */
    public static boolean inRange(int[] arrays, int position) {
        return arrays != null && position > -1 && position < arrays.length;
    }

    /**
     * 判断position是否在数组中。
     *
     * @param arrays
     * @param position
     * @return
     */
    public static boolean inRange(String[] arrays, int position) {
        return arrays != null && position > -1 && position < arrays.length;
    }

    /**
     * 是否包含。
     *
     * @param modes
     * @param mode
     * @return
     */
    public static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }
}

package net.rcsoft.service.proxy.data.util;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Utility class for generic objects.
 */
public class ObjectUtil {

    /**
     * test if given object is a list.
     *
     * @param o object to test
     * @return true if o is list
     */
    public static boolean isList(final Object o) {
        return Collection.class.isAssignableFrom(o.getClass());
    }

    /**
     * test if given object is a map.
     *
     * @param o object to test
     * @return true if o is list
     */
    public static boolean isMap(final Object o) {
        return Map.class.isAssignableFrom(o.getClass());
    }

    /**
     * test if given object is an array.
     *
     * @param o object to test
     * @return true if o is list
     */
    public static boolean isArray(final Object o) {
        return o.getClass().isArray();
    }

    /**
     * Get list of type from array;
     *
     * @param type list type
     * @param objs objects to ass
     * @param <T> list type
     * @return list of type
     */
    public static <T> List<T> toList(Class<T> type, Object[] objs) {
        List<T> list = new ArrayList<>();

        Arrays.stream(objs).forEach((obj) -> {
            list.add((T) obj);
        });

        return list;
    }

    /**
     * Get list of type from array;
     *
     * @param keyType list type
     * @param valueType key type
     * @param objMap to convert
     * @param <K> keys type
     * @param <V> values type
     * @return list of type
     */
    public static <K,V> Map<K,V> toMap(Class<K> keyType, Class<V> valueType,  Map objMap) {
        final Gson gson = new Gson();
        final Map<K,V> result = new HashMap<>();

        if (objMap != null && !objMap.isEmpty()) {
            for (Object key : objMap.keySet()) {
                final K resultKey = (K) key;
                final String valueJson = gson.toJson(objMap.get(key));
                final V resultValue = gson.fromJson(valueJson, valueType);

                result.put(resultKey, resultValue);
            }
        }

        return result;
    }

    /**
     * Get list of type from array;
     *
     * @param type list type
     * @param objs objects to ass
     * @param <T> list type
     * @return list of type
     */
    public static <T> T[] toArray(Class<T> type, Object[] objs) {
        T[] result = (T[]) Array.newInstance(type, objs.length);

        for (int i = 0; i < objs.length; i++) {
            result[i] = (T) objs[i];
        }

        return result;
    }

    /**
     * Get class from objects in list.
     *
     * @param list list
     * @return the class of the objects in list
     */
    public static Class<?> getClassFromCollection(final Collection<?> list) {
        if (list == null || list.isEmpty()) {
            return Object.class;
        }

        return list.stream().findFirst().get().getClass();
    }

    /**
     * Get class name from objects in list.
     *
     * @param list list
     * @return the class name of the objects in list
     */
    public static String getClassNameFromCollection(final Collection<?> list) {
        if (list == null || list.isEmpty()) {
            return Object.class.getName();
        }

        return list.stream().findFirst().get().getClass().getName();
    }

    /**
     * Get object class name.
     *
     * @param obj object
     * @return the class name of the given object
     */
    public static String getClassNameForObject(final Object obj) {
        if (ObjectUtil.isList(obj)) {
            return ObjectUtil.getClassNameFromCollection((List) obj);
        } else {
            return obj.getClass().getName();
        }
    }
}

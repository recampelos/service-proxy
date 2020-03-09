package net.rcsoft.service.proxy.data.util;

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
        return (Collection.class.isAssignableFrom(o.getClass()) || Map.class.isAssignableFrom(o.getClass()) || o.getClass().isArray());
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
     * Get class from objects in list.
     *
     * @param list list
     * @return the class of the objects in list
     */
    public static Class<?> getClassFromList(final List<?> list) {
        if (list == null || list.isEmpty()) {
            return Object.class;
        }

        return list.get(0).getClass();
    }

    /**
     * Get class name from objects in list.
     *
     * @param list list
     * @return the class name of the objects in list
     */
    public static String getClassNameFromList(final List<?> list) {
        if (list == null || list.isEmpty()) {
            return Object.class.getName();
        }

        return list.get(0).getClass().getName();
    }

    /**
     * Get object class name.
     *
     * @param obj object
     * @return the class name of the given object
     */
    public static String getClassNameForObject(final Object obj) {
        if (ObjectUtil.isList(obj)) {
            return ObjectUtil.getClassNameFromList((List) obj);
        } else {
            return obj.getClass().getName();
        }
    }
}

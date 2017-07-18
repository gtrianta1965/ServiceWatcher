package com.cons.utils;

public class GenericUtils {
    public GenericUtils() {
        super();
    }
    /*
     * Implement as the NVL function of PL/SQL for Java.
     * gtrianta 18/7/2017
     */

    public static <T> T nvl(T... objs) {
        for (T obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

}

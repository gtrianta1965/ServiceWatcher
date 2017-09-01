package com.cons.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

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

    public static void printCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(dateFormat.format(new Date()));
    }

    public static boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    public static boolean validateRegex(String regex) {
        try {
            java.util
                .regex
                .Pattern
                .compile(regex);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}

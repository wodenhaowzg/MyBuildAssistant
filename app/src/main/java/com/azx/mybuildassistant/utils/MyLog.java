package com.azx.mybuildassistant.utils;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLog {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    public static void log(String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(format + " " + msg);
    }

    public static void log(String tag, String msg) {
        Logger loger = Logger.getGlobal();
        String format = dateFormat.format(System.currentTimeMillis());
        loger.log(Level.WARNING, tag + " -> " + format + " " + msg);
    }

    public static void d(String tag, String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(tag + " -> " + format + " " + msg);
    }

    public static void e(String tag, String msg) {
        try {
            throw new Exception(tag + " -> " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

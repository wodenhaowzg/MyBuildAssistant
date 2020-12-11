package com.azx.mybuildassistant.utils;

import java.text.SimpleDateFormat;

public class MyLog {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    public static void d(String tag, String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(format + " D/" + tag + " : " + msg);
    }

    public static void w(String tag, String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println("\033[34;43;4m" + format + " W/" + tag + " : " + msg + "\033[0m");
    }

    public static void e(String tag, String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println("\033[31;4m" + format + " E/" + tag + " : " + msg + "\033[0m");
    }

    public static void error(String tag, String msg) {
        try {
            throw new Exception(tag + " -> " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

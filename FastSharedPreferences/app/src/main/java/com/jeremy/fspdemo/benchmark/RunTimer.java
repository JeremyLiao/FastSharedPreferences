package com.jeremy.fspdemo.benchmark;

/**
 * Created by liaohailiang on 2018/10/23.
 */
public class RunTimer {

    private static long startTime = -1;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static long end() {
        return System.currentTimeMillis() - startTime;
    }
}

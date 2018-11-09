package com.jeremy.fspdemo;

import android.app.Application;

import com.jeremy.fastsharedpreferences.FastSharedPreferences;
import com.jeremy.fspdemo.benchmark.BenchmarkManager;

/**
 * Created by liaohailiang on 2018/10/24.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FastSharedPreferences.init(this);
        BenchmarkManager.getManager().init(this);
    }
}

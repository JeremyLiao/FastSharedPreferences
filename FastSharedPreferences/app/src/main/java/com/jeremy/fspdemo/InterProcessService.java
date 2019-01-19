package com.jeremy.fspdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jeremy.fastsharedpreferences.FastSharedPreferences;
import com.jeremy.fspdemo.bean.IPSData;

/**
 * Created by liaohailiang on 2019/1/18.
 */
public class InterProcessService extends Service {

    public static final String EXTRA_KEY = "extra_key";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IPSData data = (IPSData) intent.getSerializableExtra(EXTRA_KEY);
        if (data == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(data.name);
        if (data.value instanceof Integer) {
            sharedPreferences.edit().putInt(data.key, (int) data.value).apply();
        } else if (data.value instanceof String) {
            sharedPreferences.edit().putString(data.key, (String) data.value).apply();
        } else if (data.value instanceof Long) {
            sharedPreferences.edit().putLong(data.key, (long) data.value).apply();
        } else if (data.value instanceof Float) {
            sharedPreferences.edit().putFloat(data.key, (float) data.value).apply();
        } else if (data.value instanceof Boolean) {
            sharedPreferences.edit().putBoolean(data.key, (boolean) data.value).apply();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

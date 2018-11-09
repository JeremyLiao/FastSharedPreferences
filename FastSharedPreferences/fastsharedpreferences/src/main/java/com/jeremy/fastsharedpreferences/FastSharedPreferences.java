package com.jeremy.fastsharedpreferences;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jeremy.fastsharedpreferences.io.ReadWriteManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by liaohailiang on 2018/10/23.
 */
public class FastSharedPreferences implements EnhancedSharedPreferences {

    private static final Map<String, FastSharedPreferences> FSP_MAP = new HashMap<>();
    private static final ExecutorService WRITE_EXECUTOR = Executors.newSingleThreadExecutor();
    private static Context sContext = null;

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        sContext = context.getApplicationContext();
    }

    public static FastSharedPreferences get(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        if (FSP_MAP.containsKey(name)) {
            return FSP_MAP.get(name);
        }
        synchronized (FastSharedPreferences.class) {
            if (!FSP_MAP.containsKey(name)) {
                HashMap<String, Object> map = (HashMap<String, Object>) new ReadWriteManager(
                        sContext, name).read();
                FSP_MAP.put(name, new FastSharedPreferences(name, map));
            }
            return FSP_MAP.get(name);
        }
    }

    public static void clearCache() {
        FSP_MAP.clear();
    }

    private final String name;
    private final Map<String, Object> keyValueMap;
    private final FspEditor editor = new FspEditor();
    private final AtomicBoolean needSync = new AtomicBoolean(false);
    private final AtomicBoolean syncing = new AtomicBoolean(false);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private FastSharedPreferences(String name, Map<String, Object> map) {
        this.name = name;
        this.keyValueMap = new ConcurrentHashMap<>();
        if (map != null) {
            this.keyValueMap.putAll(map);
        }
    }

    @Override
    public Map<String, ?> getAll() {
        return keyValueMap;
    }

    @Nullable
    @Override
    public String getString(String s, @Nullable String s1) {
        if (keyValueMap.containsKey(s)) {
            return (String) keyValueMap.get(s);
        }
        return s1;
    }

    @Override
    public Serializable getSerializable(String key, @Nullable Serializable defValue) {
        if (keyValueMap.containsKey(key)) {
            return (Serializable) keyValueMap.get(key);
        }
        return defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, @Nullable Set<String> set) {
        if (keyValueMap.containsKey(s)) {
            return (Set<String>) keyValueMap.get(s);
        }
        return set;
    }

    @Override
    public int getInt(String s, int i) {
        if (keyValueMap.containsKey(s)) {
            return (int) keyValueMap.get(s);
        }
        return i;
    }

    @Override
    public long getLong(String s, long l) {
        if (keyValueMap.containsKey(s)) {
            return (long) keyValueMap.get(s);
        }
        return l;
    }

    @Override
    public float getFloat(String s, float v) {
        if (keyValueMap.containsKey(s)) {
            return (float) keyValueMap.get(s);
        }
        return v;
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        if (keyValueMap.containsKey(s)) {
            return (boolean) keyValueMap.get(s);
        }
        return b;
    }

    @Override
    public boolean contains(String s) {
        return keyValueMap.containsKey(s);
    }

    @Override
    public EnhancedEditor edit() {
        return editor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

    }

    private class FspEditor implements EnhancedEditor {
        @Override
        public EnhancedEditor putSerializable(String key, @Nullable Serializable value) {
            put(key, value);
            return this;
        }

        @Override
        public EnhancedEditor putString(String s, @Nullable String s1) {
            put(s, s1);
            return this;
        }

        @Override
        public EnhancedEditor putStringSet(String s, @Nullable Set<String> set) {
            put(s, set);
            return this;
        }

        @Override
        public EnhancedEditor putInt(String s, int i) {
            put(s, i);
            return this;
        }

        @Override
        public EnhancedEditor putLong(String s, long l) {
            put(s, l);
            return this;
        }

        @Override
        public EnhancedEditor putFloat(String s, float v) {
            put(s, v);
            return this;
        }

        @Override
        public EnhancedEditor putBoolean(String s, boolean b) {
            put(s, b);
            return this;
        }

        private void put(String s, Object obj) {
            readWriteLock.readLock().lock();
            keyValueMap.put(s, obj);
            readWriteLock.readLock().unlock();
        }

        @Override
        public Editor remove(String s) {
            readWriteLock.readLock().lock();
            keyValueMap.remove(s);
            readWriteLock.readLock().unlock();
            return this;
        }

        @Override
        public EnhancedEditor clear() {
            readWriteLock.readLock().lock();
            keyValueMap.clear();
            readWriteLock.readLock().unlock();
            return this;
        }

        @Override
        public boolean commit() {
            sync();
            return true;
        }

        @Override
        public void apply() {
            sync();
        }

        private void sync() {
            needSync.compareAndSet(false, true);
            postSyncTask();
        }

        private synchronized void postSyncTask() {
            if (syncing.get()) {
                return;
            }
            WRITE_EXECUTOR.execute(new SyncTask());
        }

        private class SyncTask implements Runnable {

            @Override
            public void run() {
                if (!needSync.get()) {
                    return;
                }
                //先把syncing标记置为true
                syncing.compareAndSet(false, true);
                //copy map，copy的过程中不允许写入
                readWriteLock.writeLock().lock();
                Map<String, Object> storeMap = new HashMap<>(keyValueMap);
                readWriteLock.writeLock().unlock();
//                Log.d("FastSharedPreferences", "start sync with item size: " + storeMap.size());
                //把needSync置为false，如果在此之后有数据写入，则需要重新同步
                needSync.compareAndSet(true, false);
                ReadWriteManager manager = new ReadWriteManager(sContext, name);
                manager.write(storeMap);
                //解除同步过程
                syncing.compareAndSet(true, false);
//                Log.d("FastSharedPreferences", "sync really finished!!!");
                //如果数据被更改，则需要重新同步
                if (needSync.get()) {
                    postSyncTask();
                }
            }
        }
    }
}

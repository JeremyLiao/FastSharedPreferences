package com.jeremy.fspdemo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.jeremy.fastsharedpreferences.FastSharedPreferences;
import com.jeremy.fspdemo.bean.IPSData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by liaohailiang on 2018/10/24.
 */
@RunWith(AndroidJUnit4.class)
public class FastSharedPreferencesAndroidTest {

    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        FastSharedPreferences.init(context);
    }

    @Test
    public void testLoad() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_load");
        assertNotNull(sharedPreferences);
    }

    @Test
    public void testWriteString() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_string");
        sharedPreferences.edit().putString("test_key", "test_value").apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_string");
        assertEquals(sharedPreferences.getString("test_key", ""), "test_value");
    }

    @Test
    public void testWriteInteger() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_integer");
        sharedPreferences.edit().putInt("test_key", 100).apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_integer");
        assertEquals(sharedPreferences.getInt("test_key", -1), 100);
    }

    @Test
    public void testWriteLong() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_long");
        sharedPreferences.edit().putLong("test_key", 100).apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_long");
        assertEquals(sharedPreferences.getLong("test_key", -1), 100);
    }

    @Test
    public void testWriteFloat() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_float");
        sharedPreferences.edit().putFloat("test_key", 100.0f).apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_float");
        assertTrue(sharedPreferences.getFloat("test_key", -1) == 100.0f);
    }

    @Test
    public void testWriteBoolean() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_bool");
        sharedPreferences.edit().putBoolean("test_key", true).apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_bool");
        assertEquals(sharedPreferences.getBoolean("test_key", false), true);
    }

    @Test
    public void testWriteIntegers() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_int");
        for (int i = 0; i < 1000; i++) {
            sharedPreferences.edit().putInt("test_key_" + i, i).apply();
        }
        sleep(200);
        sharedPreferences = FastSharedPreferences.get("test_write_int");
        for (int i = 0; i < 1000; i++) {
            assertEquals(sharedPreferences.getInt("test_key_" + i, -1), i);
        }
    }

    @Test
    public void testWriteCount() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_count");
        sharedPreferences.edit().clear().commit();
        sleep(200);
        for (int i = 0; i < 10000; i++) {
            sharedPreferences.edit().putInt("test_key_" + i, i).apply();
        }
        sleep(1500);
        sharedPreferences = FastSharedPreferences.get("test_write_count");
        int size = sharedPreferences.getAll().size();
        assertEquals(size, 10000);
    }

    @Test
    public void testRemove() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_re");
        sharedPreferences.edit().putString("test_key", "test_value").apply();
        sleep(100);
        sharedPreferences.edit().remove("test_key").apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_re");
        assertEquals(sharedPreferences.contains("test_key"), false);
    }

    @Test
    public void testClear() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_cl");
        for (int i = 0; i < 1000; i++) {
            sharedPreferences.edit().putInt("test_key_" + i, i).apply();
        }
        sleep(200);
        sharedPreferences.edit().clear().apply();
        sleep(100);
        sharedPreferences = FastSharedPreferences.get("test_write_cl");
        assertEquals(sharedPreferences.getAll().size(), 0);
    }

    @Test
    public void testInterProcessWriteInteger() {
        IPSData data = new IPSData("ip_test_write_integer", "test_key", 100);
        Intent intent = new Intent(context, InterProcessService.class);
        intent.putExtra(InterProcessService.EXTRA_KEY, data);
        context.startService(intent);
        sleep(500);
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("ip_test_write_integer");
        assertEquals(sharedPreferences.getInt("test_key", -1), 100);
    }

    @Test
    public void testInterProcessWriteInteger1() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("ip_test_write_integer_1");
        sharedPreferences.edit().putInt("test_key", 100).apply();
        assertEquals(sharedPreferences.getInt("test_key", -1), 100);
        IPSData data = new IPSData("ip_test_write_integer_1", "test_key", 200);
        Intent intent = new Intent(context, InterProcessService.class);
        intent.putExtra(InterProcessService.EXTRA_KEY, data);
        context.startService(intent);
        sleep(500);
        assertEquals(sharedPreferences.getInt("test_key", -1), 200);
    }

    @Test
    public void testInterProcessWriteInteger2() {
        final String name = "ip_test_write_integer_2";
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(name);
        Log.d("testInterProcessWriteInteger2", "size: " + sharedPreferences.getAll().size());
        sharedPreferences.edit().clear().apply();
        sleep(200);
        final int loop = 10;
        for (int i = 0; i < loop; i++) {
            sharedPreferences.edit().putInt("test_key_" + i, i).apply();
        }
        sleep(1000);
        for (int i = 0; i < loop; i++) {
            int remoteIndex = loop + i;
            IPSData data = new IPSData(name, "test_key_" + remoteIndex, remoteIndex);
            Intent intent = new Intent(context, InterProcessService.class);
            intent.putExtra(InterProcessService.EXTRA_KEY, data);
            context.startService(intent);
        }
        sleep(1500);
        for (int i = 0; i < loop * 2; i++) {
            assertEquals(sharedPreferences.getInt("test_key_" + i, -1), i);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}

package com.jeremy.fastsharedpreferences;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by liaohailiang on 2018/10/25.
 */
public class FastSharedPreferencesTest {

    @Mock
    Context context;

    @Mock
    Context appContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(context.getApplicationContext()).thenReturn(appContext);
        File file = new File("./build/");
        when(appContext.getFilesDir()).thenReturn(file);
        FastSharedPreferences.init(context);
    }

    @Test
    public void testContext() {
        assertNotNull(context);
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
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_string");
        assertEquals(sharedPreferences.getString("test_key", ""), "test_value");
    }

    @Test
    public void testWriteInteger() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_integer");
        sharedPreferences.edit().putInt("test_key", 100).apply();
        sleep(100);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_integer");
        assertEquals(sharedPreferences.getInt("test_key", -1), 100);
    }

    @Test
    public void testWriteLong() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_long");
        sharedPreferences.edit().putLong("test_key", 100).apply();
        sleep(100);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_long");
        assertEquals(sharedPreferences.getLong("test_key", -1), 100);
    }

    @Test
    public void testWriteFloat() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_float");
        sharedPreferences.edit().putFloat("test_key", 100.0f).apply();
        sleep(100);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_float");
        assertTrue(sharedPreferences.getFloat("test_key", -1) == 100.0f);
    }

    @Test
    public void testWriteBoolean() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_bool");
        sharedPreferences.edit().putBoolean("test_key", true).apply();
        sleep(100);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_bool");
        assertEquals(sharedPreferences.getBoolean("test_key", false), true);
    }

    @Test
    public void testWriteSerializable() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_ser");
        sharedPreferences.edit().putSerializable("test_key",
                new TestBean("test_str", 100)).apply();
        sleep(100);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_ser");
        TestBean testBean = (TestBean) sharedPreferences.getSerializable("test_key", null);
        assertNotNull(testBean);
        assertEquals(testBean.getStrValue(), "test_str");
        assertEquals(testBean.getIntValue(), 100);
    }

    @Test
    public void testWriteIntegers() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_int");
        for (int i = 0; i < 1000; i++) {
            sharedPreferences.edit().putInt("test_key_" + i, i).apply();
        }
        sleep(200);
        FastSharedPreferences.clearCache();
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
        sleep(1000);
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_count");
        assertEquals(sharedPreferences.getAll().size(), 10000);
    }

    @Test
    public void testRemove() {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get("test_write_re");
        sharedPreferences.edit().putString("test_key", "test_value").apply();
        sleep(100);
        sharedPreferences.edit().remove("test_key").apply();
        sleep(100);
        FastSharedPreferences.clearCache();
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
        FastSharedPreferences.clearCache();
        sharedPreferences = FastSharedPreferences.get("test_write_cl");
        assertEquals(sharedPreferences.getAll().size(), 0);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}

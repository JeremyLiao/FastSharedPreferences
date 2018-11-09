package com.jeremy.fastsharedpreferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by liaohailiang on 2018/10/25.
 */
public interface EnhancedEditor extends SharedPreferences.Editor {

    EnhancedEditor putSerializable(String key, @Nullable Serializable value);
}

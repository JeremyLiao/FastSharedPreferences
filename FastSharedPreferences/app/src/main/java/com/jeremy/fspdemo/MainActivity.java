package com.jeremy.fspdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jeremy.fastsharedpreferences.FastSharedPreferences;
import com.jeremy.fspdemo.benchmark.BenchmarkActivity;

public class MainActivity extends AppCompatActivity {

    private static final String FSP_ID = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toBenchmark(View v) {
        startActivity(new Intent(this, BenchmarkActivity.class));
    }

    public void writeInt(View v) {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(FSP_ID);
        sharedPreferences.edit().putInt("test_key", 100).apply();
        Toast.makeText(this, "Write Int", Toast.LENGTH_SHORT).show();
    }

    public void readInt(View v) {
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(FSP_ID);
        int ret = sharedPreferences.getInt("test_key", -1);
        Toast.makeText(this, "Read Int: " + ret, Toast.LENGTH_SHORT).show();
    }
}

package com.jeremy.fspdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jeremy.fspdemo.benchmark.BenchmarkManager;
import com.jeremy.fspdemo.benchmark.BenchmarkResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void batchWriteInt(View v) {
        Toast.makeText(this, "Start benchmark", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BenchmarkResult> results = BenchmarkManager.getManager().benchmarkWriteInt();
                final String result = results.toString();
                Log.d("BenchmarkWriteInt", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void batchReadInt(View v) {
        Toast.makeText(this, "Start benchmark", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BenchmarkResult> results = BenchmarkManager.getManager().benchmarReadInt();
                final String result = results.toString();
                Log.d("BenchmarkReadInt", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}

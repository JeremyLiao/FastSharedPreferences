package com.jeremy.fspdemo.benchmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jeremy.fspdemo.R;

import java.util.List;

public class BenchmarkActivity extends AppCompatActivity {

    private static final String TAG = "BENCHMARK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);
    }

    public void batchWriteInt(View v) {
        Toast.makeText(this, "Start benchmark", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BenchmarkResult> results = BenchmarkManager.getManager().benchmarkWriteInt();
                final String result = results.toString();
                Log.d(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BenchmarkActivity.this, result, Toast.LENGTH_LONG).show();
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
                List<BenchmarkResult> results = BenchmarkManager.getManager().benchmarkReadInt();
                final String result = results.toString();
                Log.d(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BenchmarkActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}

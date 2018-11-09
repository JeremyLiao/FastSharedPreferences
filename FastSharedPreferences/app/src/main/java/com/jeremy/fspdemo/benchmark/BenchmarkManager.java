package com.jeremy.fspdemo.benchmark;

import android.content.Context;
import android.content.SharedPreferences;

import com.jeremy.fastsharedpreferences.FastSharedPreferences;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liaohailiang on 2018/11/8.
 */
public final class BenchmarkManager {

    private static final int LOOPS = 1000;
    private static final String SP_ID = "test";
    private static final String FSP_ID = "test";
    private static final String MMKV_ID = "test";

    private static final String BATCH_WRITE_INT = "BatchWriteInt";
    private static final String BATCH_READ_INT = "BatchReadInt";

    private static class SingletonHolder {
        private static final BenchmarkManager SINGLETON = new BenchmarkManager();
    }

    public static BenchmarkManager getManager() {
        return SingletonHolder.SINGLETON;
    }

    private Context context;

    private BenchmarkManager() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        MMKV.initialize(this.context);
    }

    public List<BenchmarkResult> benchmarkWriteInt() {
        BenchmarkController controller = new BenchmarkController();
        List<BenchmarkResult> results = new ArrayList<>();
        double benchmark = 0;
        benchmark = controller.runBenchmark(10, new IBenchmark() {
            @Override
            public long benchmark() {
                return spBatchWriteInt();
            }
        });
        results.add(new BenchmarkResult("SharedPreferences", BATCH_WRITE_INT, benchmark));
        benchmark = controller.runBenchmark(10, new IBenchmark() {
            @Override
            public long benchmark() {
                return sqliteWriteInt();
            }
        });
        results.add(new BenchmarkResult("SQLite", BATCH_WRITE_INT, benchmark));
        benchmark = controller.runBenchmark(1000, new IBenchmark() {
            @Override
            public long benchmark() {
                return mmkvBatchWriteInt();
            }
        });
        results.add(new BenchmarkResult("MMKV", BATCH_WRITE_INT, benchmark));
        benchmark = controller.runBenchmark(1000, new IBenchmark() {
            @Override
            public long benchmark() {
                return fspBatchWriteInt();
            }
        });
        results.add(new BenchmarkResult("FastSharedPreferences", BATCH_WRITE_INT, benchmark));
        return results;
    }

    public List<BenchmarkResult> benchmarReadInt() {
        BenchmarkController controller = new BenchmarkController();
        List<BenchmarkResult> results = new ArrayList<>();
        double benchmark = 0;
        benchmark = controller.runBenchmark(10, new IBenchmark() {
            @Override
            public long benchmark() {
                return spBatchReadInt();
            }
        });
        results.add(new BenchmarkResult("SharedPreferences", BATCH_READ_INT, benchmark));
        benchmark = controller.runBenchmark(10, new IBenchmark() {
            @Override
            public long benchmark() {
                return sqliteBatchReadInt();
            }
        });
        results.add(new BenchmarkResult("SQLite", BATCH_READ_INT, benchmark));
        benchmark = controller.runBenchmark(1000, new IBenchmark() {
            @Override
            public long benchmark() {
                return mmkvBatchReadInt();
            }
        });
        results.add(new BenchmarkResult("MMKV", BATCH_READ_INT, benchmark));
        benchmark = controller.runBenchmark(1000, new IBenchmark() {
            @Override
            public long benchmark() {
                return fspBatchReadInt();
            }
        });
        results.add(new BenchmarkResult("FastSharedPreferences", BATCH_READ_INT, benchmark));
        return results;
    }

    private long spBatchWriteInt() {
        RunTimer.start();
        Random rand = new Random();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ID, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int value = rand.nextInt();
            editor.putInt(key, value);
            editor.apply();
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long fspBatchWriteInt() {
        RunTimer.start();
        Random rand = new Random();
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(FSP_ID);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int value = rand.nextInt();
            editor.putInt(key, value);
            editor.apply();
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long sqliteWriteInt() {
        RunTimer.start();
        Random rand = new Random();
        SQLIteKV sqlIteKV = new SQLIteKV(context);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int value = rand.nextInt();
            sqlIteKV.putInt(key, value);
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long mmkvBatchWriteInt() {
        RunTimer.start();
        Random rand = new Random();
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, null);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int value = rand.nextInt();
            mmkv.encode(key, value);
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long spBatchReadInt() {
        RunTimer.start();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ID, 0);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int tmp = sharedPreferences.getInt(key, -1);
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long fspBatchReadInt() {
        RunTimer.start();
        FastSharedPreferences sharedPreferences = FastSharedPreferences.get(FSP_ID);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int tmp = sharedPreferences.getInt(key, -1);
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long sqliteBatchReadInt() {
        RunTimer.start();
        SQLIteKV sqlIteKV = new SQLIteKV(context);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int tmp = sqlIteKV.getInt(key);
        }
        long cost = RunTimer.end();
        return cost;
    }

    private long mmkvBatchReadInt() {
        RunTimer.start();
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, null);
        for (int i = 0; i < LOOPS; i++) {
            String key = i + "";
            int tmp = mmkv.getInt(key, -1);
        }
        long cost = RunTimer.end();
        return cost;
    }
}

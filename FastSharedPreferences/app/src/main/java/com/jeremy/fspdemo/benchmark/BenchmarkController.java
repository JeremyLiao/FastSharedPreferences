package com.jeremy.fspdemo.benchmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohailiang on 2018/11/8.
 */
public class BenchmarkController {

    private List<Long> results = new ArrayList<>();

    public double runBenchmark(int count, IBenchmark benchmark) {
        results.clear();
        for (int i = 0; i < count; i++) {
            results.add(benchmark.benchmark());
        }
        return calculateAverage(results);
    }

    private double calculateAverage(List<Long> marks) {
        Long sum = 0L;
        if (!marks.isEmpty()) {
            for (Long mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}

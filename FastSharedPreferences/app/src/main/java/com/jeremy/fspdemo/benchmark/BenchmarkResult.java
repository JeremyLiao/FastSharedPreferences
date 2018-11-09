package com.jeremy.fspdemo.benchmark;

/**
 * Created by liaohailiang on 2018/11/8.
 */
public class BenchmarkResult {

    private String method;
    private String category;
    private double result;

    public BenchmarkResult(String method, String category, double result) {
        this.method = method;
        this.category = category;
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("method: ").append(method).append(" | ")
                .append("category: ").append(category).append(" | ")
                .append("result: ").append(result);
        return sb.toString();
    }
}

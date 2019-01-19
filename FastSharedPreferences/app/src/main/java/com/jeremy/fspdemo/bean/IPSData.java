package com.jeremy.fspdemo.bean;

import java.io.Serializable;

/**
 * Created by liaohailiang on 2019/1/18.
 */
public class IPSData implements Serializable {
    public String name;
    public String key;
    public Object value;

    public IPSData(String name, String key, Object value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }
}

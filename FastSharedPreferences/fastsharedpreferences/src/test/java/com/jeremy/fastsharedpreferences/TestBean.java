package com.jeremy.fastsharedpreferences;

import java.io.Serializable;

/**
 * Created by liaohailiang on 2018/10/25.
 */
public class TestBean implements Serializable {

    private String strValue;
    private int intValue;

    public TestBean() {
    }

    public TestBean(String strValue, int intValue) {
        this.strValue = strValue;
        this.intValue = intValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}

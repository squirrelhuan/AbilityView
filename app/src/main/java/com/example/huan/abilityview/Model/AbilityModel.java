package com.example.huan.abilityview.Model;

/**
 * Created by squirrelhuan on 2017/7/25.
 */

public class AbilityModel {
    private String title;
    private float value;

    public AbilityModel(String title, float value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}

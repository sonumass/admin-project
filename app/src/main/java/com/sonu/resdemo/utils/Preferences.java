package com.sonu.resdemo.utils;

/**
 * Created by Sonu Saini 5/26/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.content.Context.MODE_PRIVATE;

public class Preferences {

    private String prefName = "com.zaax.myapplication";
    private SharedPreferences sharedPreferences;
    private Editor edit;

    public Preferences(Context context) {

        sharedPreferences = context.getSharedPreferences(prefName, MODE_PRIVATE);
        edit = sharedPreferences.edit();

    }

    public void storeString(String key, String val) {

        edit.putString(key, val);
        edit.commit();

    }

    public void storeInt(String key, int val) {

        edit.putInt(key, val);
        edit.commit();

    }

    public float storeFloat(String key, float val) {

        edit.putFloat(key, val);
        edit.commit();

        return val;
    }

    public void storeDouble(String key, long val) {

        edit.putLong(key, val);
        edit.commit();

    }

    public void storeBoolean(String key, Boolean val) {

        edit.putBoolean(key, val);
        edit.commit();
    }

    public void storeLong(String key, long val) {

        edit.putLong(key, val);
        edit.commit();
    }

    public String getString(String key) {

        return sharedPreferences.getString(key, "");
    }

    public String getString(String key, String defaultVal) {

        return sharedPreferences.getString(key, defaultVal);
    }

    public int getInt(String key) {

        return sharedPreferences.getInt(key, -1);
    }

    public float getFloat(String key) {

        return sharedPreferences.getFloat(key, -1);
    }

    public int getInt(String key, int defaultValue) {

        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key) {

        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultB) {

        return sharedPreferences.getBoolean(key, defaultB);
    }

    public Long getLong(String key) {

        return sharedPreferences.getLong(key, -1);
    }

    public Boolean has(String key) {

        return sharedPreferences.contains(key);
    }

    public void remove(String key) {
        edit.remove(key);
        edit.commit();
    }

    public void clearAll() {
        edit.clear().commit();
    }
}

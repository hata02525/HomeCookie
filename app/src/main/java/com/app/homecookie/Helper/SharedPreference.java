package com.app.homecookie.Helper;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by fluper on 5/4/17.
 */
public class SharedPreference {
    public static final String MY_PREFERENCES = "MY_PREFERENCES";
    public static final int MODE = Context.MODE_PRIVATE;
    private static SharedPreference pref;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        sharedPreference = context.getSharedPreferences(MY_PREFERENCES, MODE);
        editor = sharedPreference.edit();
    }

    public static SharedPreference getInstance(Context context) {
        if (pref == null) {
            pref = new SharedPreference(context);
        }
        return pref;
    }

    public String getString(String key, String defValue) {
        return sharedPreference.getString(key, defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }


    public int getInt(String key, int defValue) {
        return sharedPreference.getInt(key, defValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }


    public long getLong(String key, long defValue) {
        return sharedPreference.getLong(key, defValue);
    }


    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }


    public float getFloat(String key, float defValue) {
        return sharedPreference.getFloat(key, defValue);
    }


    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreference.getBoolean(key, defValue);
    }


    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }


    public boolean contains(String key) {
        return sharedPreference.contains(key);
    }

    public void remove(String key) {
        editor.remove(key).commit();
    }

    public void clear() {
        editor.clear().commit();
    }
}

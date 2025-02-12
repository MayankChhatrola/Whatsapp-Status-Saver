package com.example.whtsappstatussaver;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "my-intro-slider";


    public PrefManager(Context context1) {

        this.context = context1;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }
    

    public String setString(String Key, String data) {
        editor.putString(Key, data);
        editor.commit();
        return data;
    }

    public String getString(String Key) {
        return sharedPreferences.getString(Key,null);
    }


    public boolean setBoolen(String Key, boolean data) {
        editor.putBoolean(Key, data);
        editor.commit();
        return data;
    }

    public boolean getBoolen(String Key) {
        return sharedPreferences.getBoolean(Key,false);
    }

}

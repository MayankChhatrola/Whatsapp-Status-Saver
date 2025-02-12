package com.example.whtsappstatussaver.util;


import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

public class Application extends android.app.Application {


    private static Application instance;
    private static WeakReference<Activity> runningActivity;
    public static String WHATSAPP_FOLDER = "Whatsapp Saver";


    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static String getNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);

    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static void setRunningActivity(Activity activity) {
        runningActivity = new WeakReference<>(activity);
    }

    public static Activity getRunningActivity() {
        WeakReference<Activity> weakReference = runningActivity;
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        return runningActivity.get();
    }
}

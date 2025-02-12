package com.example.whtsappstatussaver.util;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class Utils {


    public static boolean checkP(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context,
                    WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    @SuppressLint("NewApi")
    public static void Frag_Req(Activity activity, int rq_code) {
        activity.requestPermissions(new String[]{
                WRITE_EXTERNAL_STORAGE}, rq_code);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean ratoinalCheckFragment(Activity activity) {
        return activity.shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE);
    }


}

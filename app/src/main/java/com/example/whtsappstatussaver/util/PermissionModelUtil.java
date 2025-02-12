package com.example.whtsappstatussaver.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import com.example.whtsappstatussaver.R;




@SuppressLint("NewApi")
public class PermissionModelUtil {

    public static final String[] NECESSARY_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.CAMERA};

    @SuppressWarnings("unused")
    private static final String PERMISSION_CHECK_PREFRENCE = "marshmallow_permission_check";
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static Context context;
    @SuppressWarnings("unused")
    private SharedPreferences sharedPreferences;

    public static void requestPermissions() {
        ((Activity) context).requestPermissions(NECESSARY_PERMISSIONS, 1);
        //ActivityCompat.requestPermissions((Activity) context,
        //		NECESSARY_PERMISSIONS, 1);
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (((Activity) context)
                    .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (((Activity) context).shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.permission_necessary);
                    alertBuilder
                            .setMessage(R.string.external_storage_and_camera_permission_are_necessary);
                    alertBuilder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ((Activity) context)
                                            .requestPermissions(
                                                    NECESSARY_PERMISSIONS,
                                                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ((Activity) context).requestPermissions(
                            NECESSARY_PERMISSIONS,
                            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
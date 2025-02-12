package com.example.whtsappstatussaver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;


@SuppressLint("NewApi")
public class Permission {

    public static final String[] NECESSARY_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SET_WALLPAPER,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.SET_WALLPAPER_HINTS, Manifest.permission.SET_WALLPAPER,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    @SuppressWarnings("unused")
    private static final String PERMISSION_CHECK_PREF = "marshmallow_permission_check";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static Context context;
    @SuppressWarnings("unused")
    private SharedPreferences sharedPrefs;

    public static void requestPermissions() {
        ((Activity) context).requestPermissions(NECESSARY_PERMISSIONS, 1);
    }

    public static void checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.R) {
            if (((Activity) context)
                    .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (((Activity) context).shouldShowRequestPermissionRationale(

                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                            context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.permission_necessary);
                    alertBuilder
                            .setMessage(R.string.external_storage_and_camera_permission_are_necessary);
                    alertBuilder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ((Activity) context)
                                            .requestPermissions(
                                                    NECESSARY_PERMISSIONS,
                                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            });

                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ((Activity) context).requestPermissions(
                            NECESSARY_PERMISSIONS,
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            } else {
            }
        } else {
        }
    }
}

package com.example.whtsappstatussaver.model;

import android.net.Uri;
import android.os.Environment;


import com.example.whtsappstatussaver.util.Application;

import java.io.File;


public class ImageModel {
    private String sPath;
    private String sOfflinePath;
    private boolean isAvailOffline = false;
    private String fileName;
    private Uri contentUri;


    public Uri getContentUri() {
        return contentUri;
    }

    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        String offlinePathStore = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER+ File.separator+fileName;
        setsOfflinePath(offlinePathStore);
    }

    public boolean isAvailOffline() {
        return isAvailOffline;
    }

    public void setAvailOffline(boolean availOffline) {
        isAvailOffline = availOffline;
    }

    public String getsOfflinePath() {
        return sOfflinePath;
    }

    public void setsOfflinePath(String sOfflinePath) {
        this.sOfflinePath = sOfflinePath;
    }

    private boolean thumbVideo;

    public boolean isThumbVideo() {
        return thumbVideo;
    }

    public void setThumbVideo(boolean thumbVideo) {
        this.thumbVideo = thumbVideo;
    }

    private boolean isSelected = false;

    public ImageModel(String path) {
        this.sPath = path;
        String offlinePathStore = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER+ File.separator+path.substring(path.lastIndexOf("/")+1);
        setsOfflinePath(offlinePathStore);
    }

    public String getsPath() {
        return sPath;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

}

package com.example.whtsappstatussaver.util;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.List;


public class HelperMethods {

    private static Context ctx;

    public HelperMethods(Context context) {
        ctx = context;
    }

    public Uri transfer(File file, boolean isVideo, Uri fUri, String filename) {
        try {
            String stringBuffer = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/WhatsAppSaver_2022/" + Application.WHATSAPP_FOLDER + "/";
            Uri uri = copyFile(file, isVideo, fUri, filename);
            MediaScannerConnection.scanFile(ctx, new String[]{stringBuffer + file.getName()}, null,
                    new OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            android.util.Log.e("ExternalStorage", "Scanned " + path + ":");
                            android.util.Log.e("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(new File(stringBuffer + file.getName())));
            ctx.sendBroadcast(intent);
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Uri copyFile(File input, boolean isVideo, Uri fUri, String filename) throws IOException {
        Log.e("moveFile", "Image Folder ");
        String mimeType = "";
        if (isVideo) {
            mimeType = "video/*";
        } else {
            mimeType = "image/*";
        }
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER);     //end "/" is not mandatory

                //important!
                if (isVideo) {
                    uri = ctx.getContentResolver().insert(MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values);
                } else {
                    uri = ctx.getContentResolver().insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values);
                }
                Log.e("moveFile", "Uri " + uri + "File created successfully");
                ParcelFileDescriptor pfd;

                try {
                    InputStream in = ctx.getContentResolver().openInputStream(fUri);
                    pfd = ctx.getContentResolver().openFileDescriptor(uri, "rwt");
                    FileOutputStream outputStream = new FileOutputStream(pfd.getFileDescriptor());


                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    in.close();
                    in = null;


                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                values.clear();
                if (isVideo) {
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                } else {
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                }
                ctx.getContentResolver().update(uri, values, null, null);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("moveFile", "Fail to create file");
            }

        } else {

            File movie = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM);
            if (!movie.exists()) {
                movie.mkdir();
            }
            Log.e("moveFile", movie.getAbsolutePath() + "  " + movie.exists());
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER);
            if (!file.exists()) {
                file.mkdir();
            }
            ContentValues values = new ContentValues(7);

            String path = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER + File.separator + input.getName();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, input.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "video/*");
            values.put(MediaStore.MediaColumns.DATA, path);

            Log.e("URIIIIIIIII_v", "> " + path);
            ContentResolver cr = ctx.getContentResolver();
            uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

            Log.e("URIIIIIIIII_v", "> " + uri);


            FileChannel outputChannel = null;
            FileChannel inputChannel = null;
            try {
                outputChannel = new FileOutputStream(path).getChannel();
                inputChannel = new FileInputStream(input).getChannel();
                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                inputChannel.close();
            } finally {
                if (inputChannel != null) inputChannel.close();
                if (outputChannel != null) outputChannel.close();
            }
        }
        return uri;
    }


    public static File getLatestFilefromDir(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return (File) null;
        }
        File file = listFiles[0];
        for (int i = 1; i < listFiles.length; i++) {
            if (file.lastModified() < listFiles[i].lastModified()) {
                file = listFiles[i];
            }
        }
        return file;
    }

    @Nullable
    public static String getFullPathFromTreeUri(@Nullable final Uri treeUri, Context con) {
        if (treeUri == null) return null;
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri), con);
        if (volumePath == null) return File.separator;
        if (volumePath.endsWith(File.separator))
            volumePath = volumePath.substring(0, volumePath.length() - 1);

        String documentPath = getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator))
            documentPath = documentPath.substring(0, documentPath.length() - 1);

        if (documentPath.length() > 0) {
            if (documentPath.startsWith(File.separator))
                return volumePath + documentPath;
            else
                return volumePath + File.separator + documentPath;
        } else return volumePath;
    }

    private static String getVolumePath(final String volumeId, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return getVolumePathForAndroid11AndAbove(volumeId, context);
        else
            return getVolumePathBeforeAndroid11(volumeId, context);
    }

    private static String getVolumePathBeforeAndroid11(final String volumeId, Context context) {
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Object result = getVolumeList.invoke(mStorageManager);

            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                Boolean primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                if (primary && volumeId.equals("primary"))    // primary volume?
                    return (String) getPath.invoke(storageVolumeElement);

                if (uuid != null && uuid.equals(volumeId))    // other volumes?
                    return (String) getPath.invoke(storageVolumeElement);
            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private static String getVolumePathForAndroid11AndAbove(final String volumeId, Context context) {
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
            for (StorageVolume storageVolume : storageVolumes) {
                // primary volume?
                if (storageVolume.isPrimary() && volumeId.equals("primary"))
                    return storageVolume.getDirectory().getPath();

                // other volumes?
                String uuid = storageVolume.getUuid();
                if (uuid != null && uuid.equals(volumeId))
                    return storageVolume.getDirectory().getPath();

            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getVolumeIdFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if (split.length > 0) return split[0];
        else return null;
    }


    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getDocumentPathFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if ((split.length >= 2) && (split[1] != null)) return split[1];
        else return File.separator;
    }

}
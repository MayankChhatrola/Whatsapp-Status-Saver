package com.example.whtsappstatussaver.fragments;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.ViewDataActivity;
import com.example.whtsappstatussaver.adapter.AdapterImages;
import com.example.whtsappstatussaver.model.ImageModel;
import com.example.whtsappstatussaver.recycler.RecyclerViewClickListener;
import com.example.whtsappstatussaver.recycler.RecyclerViewTouchListener;
import com.example.whtsappstatussaver.util.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {
    private ImageFragment waImgInstance;
    private RecyclerView rvView;
    private FragmentActivity factivity;
    AdapterImages adapterImages;
    private final ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private SwipeRefreshLayout waSwipeRefreshLayout;
    private View view;
    public static ActionMode mActionMode;
    private LinearLayout lltNoVideo, lltLoader;
    private Button buttonOpenWhatsApp;
    public boolean isAllSelected = false;
    long currentVisiblePosition = 0;


    private boolean isLoadFromCreat = false;

    public ImageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        bindView(view);
        init();
        addListener();

        return view;
    }

    private void bindView(View v) {
        waSwipeRefreshLayout = v.findViewById(R.id.waSwipeRefreshLayout);
        rvView = v.findViewById(R.id.rvView);
        lltNoVideo = v.findViewById(R.id.lltNoVideo);
        lltLoader = v.findViewById(R.id.lltLoader);
        buttonOpenWhatsApp = v.findViewById(R.id.buttonOpenWhatsApp);


    }

    private void init() {
        factivity = getActivity();
        setHasOptionsMenu(true);

        waImgInstance = this;

        lltNoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FFF", ">>>" + "click");
                try {
                    Intent launchIntentForPackage = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    if (launchIntentForPackage == null) {
                        Toast.makeText(getActivity(), "Whatsapp not install", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(launchIntentForPackage);
                    }

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Whatsapp not install", Toast.LENGTH_SHORT).show();

                }
            }
        });


        rvView.setItemAnimator(new DefaultItemAnimator());
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(factivity, 3));
        try {
            waSwipeRefreshLayout.setEnabled(true);
            isLoadFromCreat = true;
            populateRecyclerView();


            waSwipeRefreshLayout.setColorSchemeResources(new int[]{R.color.wa_colorPrimary, R.color.wa_colorPrimary, R.color.wa_colorPrimaryDark});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addListener() {
        rvView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentVisiblePosition = ((GridLayoutManager) rvView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            }
        });
        waSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        buttonOpenWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (appInstalledOrNot("com.whatsapp")) {
                        Intent i = factivity.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                        factivity.startActivity(i);
                    } else {
                        Toast.makeText(factivity, R.string.whatsapp_na, Toast.LENGTH_SHORT).show();
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void checkAvailableOffline() {
        ArrayList<String> downloaded = getDownloadedStatus();
        for (int i = 0; i < downloaded.size(); i++) {
            Log.e("TagOfflineManage", "List : " + downloaded.get(i));
        }
        for (int i = 0; i < imageModelArrayList.size(); i++) {
            Log.e("TagOfflineManage", imageModelArrayList.get(i).getFileName());

            if (downloaded.contains(imageModelArrayList.get(i).getFileName())) {
                imageModelArrayList.get(i).setAvailOffline(true);
            }
        }
    }

    public ArrayList<String> getDownloadedStatus() {

        ArrayList<String> temp = new ArrayList<>();

        Uri collection;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DURATION,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE
        };

        String selection = null;
        String pathss = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " like ? ";
            pathss = Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER;
        } else {
            selection = MediaStore.Images.Media.DATA + " like ? ";
            pathss = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + Application.WHATSAPP_FOLDER;
        }

        String[] selectionArgs = new String[]{"%" + pathss + "%"};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = factivity.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {

            // Cache column indices.
            Log.e("contentUri_F", cursor.getCount() + "");

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));

                String folderPATH = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                Log.e("contentUri_PA", folderPATH + "");

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                try {
                    if (new File(folderPATH).exists()) {
                        temp.add(name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;

    }

    public void populateRecyclerView() {

        new loadStatusInBg().execute();

    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        rvView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rvView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                Log.e("view Id", view.getId() + "");
                if (mActionMode != null) {
                    onListItemSelect(position);
                } else {
                    String str = adapterImages.getItem(position).getsPath();
                    try {
                        ViewDataActivity.imageModelArrayList = imageModelArrayList;
                        Intent intent = new Intent(getActivity(), ViewDataActivity.class);
                        intent.putExtra("pos", str);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, 1);
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                mActionMode = null;
                onListItemSelect(position);
            }
        }));
    }



    //List item select method
    public void onListItemSelect(int position) {
        adapterImages.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapterImages.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
        } else if (!hasCheckedItems && mActionMode != null){
        // there no selected items, finish the actionMode
            mActionMode.finish();
            mActionMode = null;
        }

        if (mActionMode != null) {
            mActionMode.setTitle("" + adapterImages.getSelectedCount() + " " + getString(R.string.selected));
            if ((adapterImages.getSelectedCount()) == imageModelArrayList.size()) {

            } else {
                mActionMode.setTitle("" + adapterImages.getSelectedCount() + " " + getString(R.string.selected));

            }

        }
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (VideoFragment.mActionMode != null) {
                VideoFragment.mActionMode.finish();
                VideoFragment.mActionMode = null;
            }
            if (SavedFragment.mActionMode != null) {
                SavedFragment.mActionMode.finish();
                SavedFragment.mActionMode = null;
            }
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
            try {
                Log.e("TagScrollPos", "pos : " + currentVisiblePosition);
                rvView.scrollToPosition((int) currentVisiblePosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getStatus() {
        ArrayList<ImageModel> temp = new ArrayList<>();
        File[] listFiles;
        File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Android/media/com.whatsapp/WhatsApp/Media");
        if (files.exists()) {
            listFiles = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/Android/media/com.whatsapp/WhatsApp/Media/.Statuses/").toString()).listFiles();

        } else {
            listFiles = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();

        }

        File[] listFiles1 = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp Business/Media/.Statuses/").toString()).listFiles();
        if (listFiles1 != null)
            listFiles = arrCopy(listFiles, listFiles1);


        if (listFiles == null || listFiles.length <= 0) {

        } else {
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                        ImageModel model = new ImageModel(file.getAbsolutePath());
                        model.setFileName(file.getName());
                        model.setContentUri(Uri.fromFile(file));
                        temp.add(model);
                    }
                }

                imageModelArrayList.clear();
                imageModelArrayList.addAll(temp);
                temp.clear();
            }

        }
    }

    private File[] arrCopy(File[] array1, File[] array2) {
        if (array1 == null) return array2;

        int length = array1.length + array2.length;

        File[] result = new File[length];
        int pos = 0;
        for (File element : array1) {
            result[pos] = element;
            pos++;
        }

        for (File element : array2) {
            result[pos] = element;
            pos++;
        }

        return result;
    }


    public void getDocumentFileIfAllowedToWrite(Context con) {
        if (Build.VERSION.SDK_INT >= 29) {

            ArrayList<ImageModel> temp = new ArrayList<>();

            Log.e("getStatus29", "start");
            List<UriPermission> permissionUris = con.getContentResolver().getPersistedUriPermissions();
            Log.e("getStatus29", "start for");
            for (UriPermission permissionUri : permissionUris) {
                Log.e("getStatus29", "start treeUri");
                Uri treeUri = permissionUri.getUri();
                DocumentFile rootDocFile = DocumentFile.fromTreeUri(con, treeUri);
                Log.e("getStatus29", "end treeUri");

                DocumentFile[] documentFileArr = new DocumentFile[0];
                if (rootDocFile != null) {
                    documentFileArr = rootDocFile.listFiles();
                }

                Log.e("getStatus29", "start inFor");
                for (DocumentFile documentFile : documentFileArr) {
                    Log.e("getStatus29", documentFile.getType());

                    if (documentFile.getType() != null && documentFile.getType().contains("image")) {
                        ImageModel model = new ImageModel(documentFile.getUri().toString());
                        model.setFileName(documentFile.getName());
                        model.setContentUri(documentFile.getUri());
                        temp.add(model);
                    }
                }
                Log.e("getStatus29", "end inFor");


            }

            imageModelArrayList.clear();
            imageModelArrayList.addAll(temp);
            temp.clear();
            Log.e("getStatus29", "end");
        }

    }

    private class loadStatusInBg extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (lltNoVideo != null) {
                lltNoVideo.setVisibility(View.GONE);
                buttonOpenWhatsApp.setVisibility(View.GONE);
                waSwipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (Build.VERSION.SDK_INT >= 29) {
                getDocumentFileIfAllowedToWrite(factivity);
            } else {
                ImageFragment.this.getStatus();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (imageModelArrayList == null || imageModelArrayList.size() <= 0) {
                lltNoVideo.setVisibility(View.VISIBLE);
                buttonOpenWhatsApp.setVisibility(View.GONE);
            } else {
                lltNoVideo.setVisibility(View.GONE);
                buttonOpenWhatsApp.setVisibility(View.GONE);
            }

            checkAvailableOffline();
            adapterImages = new AdapterImages(factivity, imageModelArrayList, ImageFragment.this);
            lltLoader.setVisibility(View.GONE);
            rvView.setAdapter(adapterImages);
            adapterImages.notifyDataSetChanged();
            waSwipeRefreshLayout.setRefreshing(false);
        }
    }


    public void refresh() {

        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        populateRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null) {
            if (resultCode == -1) {
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refresh();
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = factivity.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


}

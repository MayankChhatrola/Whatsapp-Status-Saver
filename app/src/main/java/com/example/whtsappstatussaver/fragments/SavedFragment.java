package com.example.whtsappstatussaver.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.adapter.AdapterHelper;
import com.example.whtsappstatussaver.adapter.AdapterSaved;
import com.example.whtsappstatussaver.adapter.GenericAppAdapter;
import com.example.whtsappstatussaver.model.ImageModel;
import com.example.whtsappstatussaver.model.ModelHelp;
import com.example.whtsappstatussaver.recycler.ActionModeCallback;
import com.example.whtsappstatussaver.util.Application;
import com.example.whtsappstatussaver.util.InstanceHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private SavedFragment mInstance;
    private RecyclerView rvView;
    public static AdapterSaved adapterSaved;
    public static ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private SwipeRefreshLayout waSwipeRefreshLayout;
    private View v;
    public static ActionMode mActionMode;
    public boolean isAllSelected = false;

    RecyclerView rv_helps;

    AdapterHelper adapterHelper;
    ArrayList<ModelHelp> arrayList;

    private LinearLayout lltNovideo;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public SavedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_video, container, false);

        bindView(v);
        init();
        addListener();

        return v;
    }

    private void bindView(View v) {
        waSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.waSwipeRefreshLayout);
        rvView = v.findViewById(R.id.rvView);
        lltNovideo = v.findViewById(R.id.lltNoVideo);
        rv_helps = v.findViewById(R.id.rv_helps);

    }

    private void init() {
        setHasOptionsMenu(true);
        mInstance = this;

        arrayList = new ArrayList<>();
        rv_helps.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_helps.setLayoutManager(linearLayoutManager);


        adapterHelper = new AdapterHelper(arrayList);
        rv_helps.setAdapter(adapterHelper);

        rvView.setHasFixedSize(true);
        rvView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rvView.setItemAnimator(new DefaultItemAnimator());

        try {
            populateRecyclerView();

            waSwipeRefreshLayout.setEnabled(false);
            waSwipeRefreshLayout.setColorSchemeResources(new int[]{R.color.wa_colorPrimary, R.color.wa_colorPrimary, R.color.wa_colorPrimaryDark});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addListener() {
        waSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }


    public void populateRecyclerView() {
        getStatus();
        Log.e("populateReSAVED", imageModelArrayList.size() + "");
        adapterSaved = new AdapterSaved(getActivity(), imageModelArrayList, SavedFragment.this);
        rvView.setAdapter(adapterSaved);
        adapterSaved.notifyDataSetChanged();
    }

    //List item select method
    public void onListItemSelect(int position) {
        adapterSaved.toggleSelection(position);//Toggle the selection
        List<Fragment> fragments;

        boolean hasCheckedItems = adapterSaved.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionModeCallback(getActivity(), new GenericAppAdapter<AdapterSaved>(adapterSaved), imageModelArrayList, new InstanceHandler<>(mInstance)));
        } else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            mActionMode = null;
        }


        if (mActionMode != null) {
            mActionMode.setTitle("" + adapterSaved.getSelectedCount() + " " + getString(R.string.selected));

        }
    }

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (adapterSaved != null) {
                refresh();
            }
            if (VideoFragment.mActionMode != null) {
                VideoFragment.mActionMode.finish();
                VideoFragment.mActionMode = null;
            }
            if (ImageFragment.mActionMode != null) {
                ImageFragment.mActionMode.finish();
                ImageFragment.mActionMode = null;
            }
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
        }
    }


    public void getStatus() {
        imageModelArrayList.clear();
        String path;

        path = File.separator + Application.WHATSAPP_FOLDER;


        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DISPLAY_NAME
        };

        Uri collection;

        collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);

        String selection = null;
        String pathss = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " like ? ";
            pathss = Environment.DIRECTORY_DCIM + path;
        } else {
            selection = MediaStore.Files.FileColumns.DATA + " like ? ";
            pathss = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + path;
        }

        String[] selectionArgs = new String[]{"%" + pathss + "%"};
        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

        try (Cursor cursor = mContext.getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            Log.e("SavedWa", cursor.getCount() + "");


            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));

                String folderPATH = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                Log.e("contentUri_PA", folderPATH + "");
                String MIME_TYPE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                Log.e("MIME_TYPE : ", MIME_TYPE + "");

                Uri contentUri;
                if (MIME_TYPE.contains("video")) {
                    contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                } else {
                    contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                }

                if (!folderPATH.contains("preview")) {

                    try {
                        ImageModel model = new ImageModel(folderPATH);
                        model.setFileName(Application.getNameFromPath(folderPATH));
                        model.setThumbVideo(folderPATH.endsWith(".mp4"));
                        model.setContentUri(contentUri);
                        imageModelArrayList.add(model);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imageModelArrayList.size() <= 0) {
            lltNovideo.setVisibility(View.VISIBLE);
        } else {
            lltNovideo.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            refresh();
        }
        if (requestCode == 1 && data != null) {
            if (resultCode == -1) {
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refresh();
            }
        }
    }

    public void refresh() {
        Log.e("TagRefresh", "call");
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        adapterSaved.updateData(new ArrayList<ImageModel>());
        adapterSaved.notifyDataSetChanged();
        populateRecyclerView();

        waSwipeRefreshLayout.setRefreshing(false);
    }
}


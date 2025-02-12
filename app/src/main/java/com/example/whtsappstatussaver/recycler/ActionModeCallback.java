package com.example.whtsappstatussaver.recycler;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.adapter.AdapterImages;
import com.example.whtsappstatussaver.adapter.AdapterSaved;
import com.example.whtsappstatussaver.adapter.AdapterVideos;
import com.example.whtsappstatussaver.adapter.GenericAppAdapter;
import com.example.whtsappstatussaver.fragments.ImageFragment;
import com.example.whtsappstatussaver.fragments.SavedFragment;
import com.example.whtsappstatussaver.fragments.VideoFragment;
import com.example.whtsappstatussaver.model.ImageModel;
import com.example.whtsappstatussaver.util.InstanceHandler;

import java.util.ArrayList;


public class ActionModeCallback implements ActionMode.Callback {

    private String str = "";
    private Context context;
    private AdapterSaved savedAdapter;
    private AdapterImages imageAdapter;
    private AdapterVideos videoAdapter;
    private ArrayList<ImageModel> message_models;

    ImageFragment imageFragment;
    VideoFragment videoFragment;
    SavedFragment savedFragment;
    String s = "";

    public ActionModeCallback(Context context, GenericAppAdapter<?> adapter, ArrayList<ImageModel> message_models, InstanceHandler<?> instance) {
        this.context = context;
        this.savedAdapter = savedAdapter;
        this.message_models = message_models;
        if (instance.getValue() != null) {
            s = instance.getValue().getClass().getSimpleName();
        } else {
            s = "NA";
        }
        switch (s) {
            case "WhtsappImageFragment":
                imageFragment = (ImageFragment) instance.getValue();
                imageAdapter = (AdapterImages) adapter.getValue();
                break;
            case "FragmentWhtsappVideo":
                videoFragment = (VideoFragment) instance.getValue();
                videoAdapter = (AdapterVideos) adapter.getValue();
                break;
            case "FragmentWhtsappSaved":
                str = "FragmentWhtsappSaved";
                savedFragment = (SavedFragment) instance.getValue();
                savedAdapter = (AdapterSaved) adapter.getValue();
                break;
        }

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        View customNav = LayoutInflater.from(context).inflate(R.layout.toolbar_action_mode, null);
//        ((AppCompatActivity) context).getSupportActionBar().setCustomView(customNav);
//        mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);//Inflate the menu_main over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


//        menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        if (str.equalsIgnoreCase("FragmentWhtsappSaved")) {
//            menu.findItem(R.id.action_save).setIcon(R.drawable.ic_delete_action);
//            menu.findItem(R.id.action_save).setTitle("Delete");
//        } else {
//            menu.findItem(R.id.action_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
//        menu.findItem(R.id.action_select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
        return false;
    }

    SparseBooleanArray selectedIds;

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int size;
        switch (item.getItemId()) {
        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.e("TAG", "onDestroyActionMode");
        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        Fragment recyclerFragment;
//        ((AppCompatActivity) context).getSupportActionBar().show();
        switch (s) {
            case "WhtsappImageFragment":
                Log.e("TAG", "WAonDestroyActionMode");
                imageAdapter.removeSelection();  // remove selection
                recyclerFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_wa_image);//Get recycler fragment
                if (recyclerFragment != null)
                    ((ImageFragment) recyclerFragment).setNullToActionMode();//Set action mode null
                ImageFragment.mActionMode = null;
                if (imageAdapter.getSelectedCount() <= 0) {
                    imageFragment.isAllSelected = false;
                } else {
                    imageFragment.isAllSelected = true;
                }
                break;
            case "FragmentWhtsappVideo":
                Log.e("TAG", "WAonDestroyActionMode");
                videoAdapter.removeSelection();  // remove selection
                recyclerFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_wa_image);//Get recycler fragment
                if (recyclerFragment != null)
                    ((VideoFragment) recyclerFragment).setNullToActionMode();//Set action mode null
                VideoFragment.mActionMode = null;
                if (videoAdapter.getSelectedCount() <= 0) {
                    videoFragment.isAllSelected = false;
                } else {
                    videoFragment.isAllSelected = true;
                }
                break;
            case "FragmentWhtsappSaved":
                Log.e("TAG", "WAonDestroyActionMode");
                savedAdapter.removeSelection();  // remove selection
                recyclerFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_wa_image);//Get recycler fragment
                if (recyclerFragment != null)
                    ((SavedFragment) recyclerFragment).setNullToActionMode();//Set action mode null
                savedFragment.mActionMode = null;
                if (savedAdapter.getSelectedCount() <= 0) {
                    savedFragment.isAllSelected = false;
                } else {
                    savedFragment.isAllSelected = true;
                }
                break;
        }
    }


}

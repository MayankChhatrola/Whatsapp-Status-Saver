package com.example.whtsappstatussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.ViewDataActivity;
import com.example.whtsappstatussaver.fragments.SavedFragment;
import com.example.whtsappstatussaver.holders.WhtsappVideoViewHolder;
import com.example.whtsappstatussaver.model.ImageModel;

import java.util.ArrayList;

public class AdapterSaved extends
        RecyclerView.Adapter<WhtsappVideoViewHolder> {
    private ArrayList<ImageModel> imageModelArrayList;
    private Context ctx;
    private SparseBooleanArray mSelectedItemsIds;
    private SavedFragment waSavedFragment;
    protected int mLastPosition = -1;
    private String path;
    private int position;


    public AdapterSaved(Context context, ArrayList<ImageModel> arrayList, SavedFragment fragment) {
        this.ctx = context;
        this.imageModelArrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();
        waSavedFragment = fragment;

    }

    private void loadVideoPlayer() {

        ViewDataActivity.imageModelArrayList = imageModelArrayList;
        Intent intent = new Intent(ctx, ViewDataActivity.class);
        intent.putExtra("pos", path);
        intent.putExtra("uri", imageModelArrayList.get(position).getContentUri().toString());
        intent.putExtra("position", position);
        waSavedFragment.startActivityForResult(intent, 101);


    }


    @Override
    public int getItemCount() {
        return (null != imageModelArrayList ? imageModelArrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(WhtsappVideoViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(ctx)
                .load(imageModelArrayList.get(position).getContentUri())
                .into(holder.roundedImgThumb);



        if (imageModelArrayList.get(position).isThumbVideo()) {
            holder.imgPlay.setVisibility(View.VISIBLE);
        } else {
            holder.imgPlay.setVisibility(View.GONE);
        }


        if (mSelectedItemsIds.get(position)) {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.wa_all_select));
        } else {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_select_all));

        }



        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SavedFragment.mActionMode != null)
                    waSavedFragment.onListItemSelect(position);
                else {
                    path = getItem(position).getsPath();
                    AdapterSaved.this.position = position;
                    try {
                        loadVideoPlayer();
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());
                    }
                }
            }
        });

        holder.roundedImgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SavedFragment.mActionMode != null) {
                    waSavedFragment.onListItemSelect(position);
                } else {
                    path = getItem(position).getsPath();
                    AdapterSaved.this.position = position;
                    try {

                        loadVideoPlayer();
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());
                    }
                }
            }
        });
        holder.roundedImgThumb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SavedFragment.mActionMode = null;
                waSavedFragment.onListItemSelect(position);
                return false;
            }
        });
        holder.roundedImgOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SavedFragment.mActionMode != null) {
                    waSavedFragment.onListItemSelect(position);
                }
            }
        });

    }

    @Override
    public WhtsappVideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.video_list_item, viewGroup, false);
        return new WhtsappVideoViewHolder(mainGroup);


    }
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or ic_delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public ImageModel getItem(int i) {
        return (ImageModel) imageModelArrayList.get(i);
    }

    public void updateData(ArrayList<ImageModel> viewModels) {
        imageModelArrayList.clear();
        imageModelArrayList.addAll(viewModels);
        notifyDataSetChanged();
    }

}
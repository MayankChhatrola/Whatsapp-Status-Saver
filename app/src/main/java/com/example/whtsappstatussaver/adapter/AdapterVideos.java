package com.example.whtsappstatussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.ViewDataActivity;
import com.example.whtsappstatussaver.fragments.VideoFragment;
import com.example.whtsappstatussaver.holders.WhtsappImageViewHolder;
import com.example.whtsappstatussaver.model.ImageModel;

import java.util.ArrayList;

public class AdapterVideos extends RecyclerView.Adapter<WhtsappImageViewHolder> {

    private ArrayList<ImageModel> imageModelArrayList;
    private Context ctx;
    private SparseBooleanArray mSelectedItemsIds;
    VideoFragment waVideoFragment;
    private String path;
    private int position;
    public AdapterVideos(Context context,
                         ArrayList<ImageModel> arrayList, VideoFragment fragment) {
        this.ctx = context;
        this.imageModelArrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();
        waVideoFragment = fragment;


    }

    @Override
    public WhtsappImageViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.image_list_item, viewGroup, false);
        return new WhtsappImageViewHolder(mainGroup);

    }

    @Override
    public void onBindViewHolder(final WhtsappImageViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        Glide.with(ctx)
                .load(imageModelArrayList.get(position).getsPath())
                .into(holder.roundedImgThumb);



        if (mSelectedItemsIds.get(position)) {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.wa_all_select));
        } else {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_select_all));
        }



        holder.roundedImgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VideoFragment.mActionMode != null) {
                    waVideoFragment.onListItemSelect(position);
                    Log.e("TAGCLICK", "ivThumb ivThumb if");
                } else {

                    AdapterVideos.this.path = getItem(position).getsPath();
                    Log.e("TAGCLICK", AdapterVideos.this.path);
                    AdapterVideos.this.position = position;
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
                VideoFragment.mActionMode = null;
                Log.e("TAGCLICK", "ivThumb setOnLongClickListener");
                waVideoFragment.onListItemSelect(position);
                return false;
            }
        });

        holder.roundedImgOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VideoFragment.mActionMode != null) {
                    waVideoFragment.onListItemSelect(position);
                    Log.e("TAGCLICK", "ivThumb ivOverlay if");
                } else {
                    Log.e("TAGCLICK", "ivThumb ivOverlay");
                }
            }
        });
    }



    private void loadVideoPlayer() {

        ViewDataActivity.imageModelArrayList = imageModelArrayList;
        Intent intent = new Intent(ctx, ViewDataActivity.class);
        intent.putExtra("pos", path);
        intent.putExtra("uri", imageModelArrayList.get(position).getsPath());
        intent.putExtra("position", position);
        waVideoFragment.startActivityForResult(intent, 101);
    }

    @Override
    public int getItemCount() {
        return (null != imageModelArrayList ? imageModelArrayList.size() : 0);

    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public ImageModel getItem(int i) {
        return (ImageModel) imageModelArrayList.get(i);
    }
}
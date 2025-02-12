package com.example.whtsappstatussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.ViewDataActivity;
import com.example.whtsappstatussaver.fragments.ImageFragment;
import com.example.whtsappstatussaver.holders.WhtsappImageViewHolder;
import com.example.whtsappstatussaver.model.ImageModel;

import java.util.ArrayList;

public class AdapterImages extends RecyclerView.Adapter<WhtsappImageViewHolder> {
    private ArrayList<ImageModel> imageModelArrayList;
    private Context ctx;
    private SparseBooleanArray mSelectedItemsIds;
    private ImageFragment imgFragment;
    protected int mLastPosition = -1;
    private String path;
    public static int select_whatsup_image = 0;

    public AdapterImages(Context context,
                         ArrayList<ImageModel> arrayList, ImageFragment fragment) {
        this.ctx = context;
        this.imageModelArrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();
        imgFragment = fragment;


    }


    @Override
    public int getItemCount() {
        return (null != imageModelArrayList ? imageModelArrayList.size() : 0);

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final WhtsappImageViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(ctx)
                .load(imageModelArrayList.get(position).getsPath())
                .apply(options)
                .into(holder.roundedImgThumb);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, imageModelArrayList.get(position).getsPath(), Toast.LENGTH_SHORT).show();
            }
        });


        if (mSelectedItemsIds.get(position)) {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.wa_all_select));

        } else {
            holder.imgcheck.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_select_all));

        }



        holder.roundedImgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_whatsup_image = position;
                if (ImageFragment.mActionMode != null) {
                    imgFragment.onListItemSelect(position);
                } else {

                    Log.e("TAGCLICK", "ivThumb ivThumb");
                    AdapterImages.this.path = getItem(position).getsPath();
                    try {
                        ViewDataActivity.imageModelArrayList = imageModelArrayList;
                        Intent intent = new Intent(ctx, ViewDataActivity.class);
                        intent.putExtra("pos", path);
                        intent.putExtra("uri", imageModelArrayList.get(position).getContentUri().toString());
                        intent.putExtra("position", position);
                        imgFragment.startActivityForResult(intent, 101);
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());

                    }
                }
            }
        });
        holder.roundedImgThumb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageFragment.mActionMode = null;
                imgFragment.onListItemSelect(position);
                return false;
            }
        });
        holder.roundedImgOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageFragment.mActionMode != null) {
                    imgFragment.onListItemSelect(position);
                }
            }
        });


    }

    @Override
    public WhtsappImageViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate( R.layout.image_list_item, viewGroup, false);
        return new WhtsappImageViewHolder(mainGroup);

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

    public void selectAll() {

    }

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > mLastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            anim.setDuration(400);// to make duration
            viewToAnimate.startAnimation(anim);
            mLastPosition = position;
        }
    }

}
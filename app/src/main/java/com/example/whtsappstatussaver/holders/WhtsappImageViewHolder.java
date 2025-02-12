package com.example.whtsappstatussaver.holders;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.view.RoundedImageView;


public class WhtsappImageViewHolder extends RecyclerView.ViewHolder {

    public RoundedImageView roundedImgThumb, roundedImgSelector, roundedImgOverlay;
    public ImageView imgPlay;
    public ImageView imgcheck;


    public WhtsappImageViewHolder(View view) {
        super(view);

        this.roundedImgThumb = view.findViewById(R.id.roundedImgThumb);
        this.roundedImgSelector = view.findViewById(R.id.roundedImgSelector);
        this.imgPlay = view.findViewById(R.id.imgPlay);
        this.roundedImgOverlay = view.findViewById(R.id.roundedImgOverlay);
        this.imgcheck = view.findViewById(R.id.imgcheck);

    }
}
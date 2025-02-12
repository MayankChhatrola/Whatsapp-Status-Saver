package com.example.whtsappstatussaver.holders;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.view.RoundedImageView;


public class WhtsappVideoViewHolder extends RecyclerView.ViewHolder {


    public RoundedImageView roundedImgThumb, roundedImgSelector, roundedImgOverlay;
    public ImageView imgPlay, imgcheck;

    public WhtsappVideoViewHolder(View view) {
        super(view);


        this.roundedImgThumb = view.findViewById(R.id.roundedImgThumb);
        this.roundedImgSelector = view.findViewById(R.id.roundedImgSelector);
        this.imgPlay = view.findViewById(R.id.imgPlay);
        roundedImgOverlay = view.findViewById(R.id.roundedImgOverlay);
        imgcheck = view.findViewById(R.id.imgcheck);


    }
}
package com.example.whtsappstatussaver.view;

import android.content.Context;
import android.util.AttributeSet;

public class RoundedImageView extends ShaderImageView {
    private com.example.whtsappstatussaver.view.RoundedShader RoundedShader;
    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        RoundedShader = new RoundedShader();
        return RoundedShader;
    }

    public final int getRadius() {
        if(RoundedShader != null) {
            return RoundedShader.getRadius();
        }
        return 0;
    }

    public final void setRadius(final int radius) {
        if(RoundedShader != null) {
            RoundedShader.setRadius(radius);
            invalidate();
        }
    }

}

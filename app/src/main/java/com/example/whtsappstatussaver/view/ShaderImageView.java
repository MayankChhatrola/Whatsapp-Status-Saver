package com.example.whtsappstatussaver.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

@SuppressWarnings("WeakerAccess")
public abstract class ShaderImageView extends androidx.appcompat.widget.AppCompatImageView {

    private final static boolean DEBUG = false;
    private ShaderHelper shaderHelper;

    public ShaderImageView(Context context) {
        super(context);
        setup(context, null, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs, defStyle);
    }

    private void setup(Context context, AttributeSet attrs, int defStyle) {
        getShaderHelper().init(context, attrs, defStyle);
    }

    protected ShaderHelper getShaderHelper() {
        if(shaderHelper == null) {
            shaderHelper = createImageViewHelper();
        }
        return shaderHelper;
    }

    protected abstract ShaderHelper createImageViewHelper();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getShaderHelper().isSquare()) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    //Required by path helper
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        getShaderHelper().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getShaderHelper().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        getShaderHelper().onImageDrawableReset(getDrawable());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getShaderHelper().onSizeChanged(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(DEBUG) {
            canvas.drawRGB(10, 200, 200);
        }

        if(!getShaderHelper().onDraw(canvas)) {
            super.onDraw(canvas);
        }
    }

    public void setBorderColor(final int borderColor) {
        getShaderHelper().setBordersColor(borderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return getShaderHelper().getBordersWidth();
    }

    public void setBorderWidth(final int borderWidth) {
        getShaderHelper().setBordersWidth(borderWidth);
        invalidate();
    }

    public float getBorderAlpha() {
        return getShaderHelper().getBorderAlpha();
    }

    public void setBorderAlpha(final float borderAlpha) {
        getShaderHelper().setBorderAlpha(borderAlpha);
        invalidate();
    }

    public void setSquare(final boolean square) {
        getShaderHelper().setSquare(square);
        invalidate();
    }
}
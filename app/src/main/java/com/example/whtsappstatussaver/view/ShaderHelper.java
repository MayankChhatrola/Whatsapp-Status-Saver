package com.example.whtsappstatussaver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.example.whtsappstatussaver.R;


@SuppressWarnings("WeakerAccess")
public abstract class ShaderHelper {
    private final static int ALPHA_MAXIMUM = 255;

    protected int vwWidth;
    protected int vwHeight;

    protected int bordersColor = Color.BLACK;
    protected int bordersWidth = 0;
    protected float borderAlpha = 1f;
    protected boolean square = false;

    protected final Paint borderPaint;
    protected final Paint imagePaint;
    protected BitmapShader shader;
    protected Drawable drawable;
    protected final Matrix matrix = new Matrix();

    public ShaderHelper() {
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
    }

    public abstract void draw(Canvas canvas, Paint imagePaint, Paint borderPaint);

    public abstract void reset();

    @SuppressWarnings("UnusedParameters")
    public abstract void calculate(int bitmapWidth, int bitmapHeight, float width, float height, float scale, float translateX, float translateY);


    @SuppressWarnings("SameParameterValue")
    protected final int dpToPx(DisplayMetrics displayMetrics, int dp) {
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaShaderImageView, defStyle, 0);
            bordersColor = typedArray.getColor(R.styleable.WaShaderImageView_siBorderColor, bordersColor);
            bordersWidth = typedArray.getDimensionPixelSize(R.styleable.WaShaderImageView_siBorderWidth, bordersWidth);
            borderAlpha = typedArray.getFloat(R.styleable.WaShaderImageView_siBorderAlpha, borderAlpha);
            square = typedArray.getBoolean(R.styleable.WaShaderImageView_siSquare, square);
            typedArray.recycle();
        }

        borderPaint.setColor(bordersColor);
        borderPaint.setAlpha(Float.valueOf(borderAlpha * ALPHA_MAXIMUM).intValue());
        borderPaint.setStrokeWidth(bordersWidth);
    }

    public boolean onDraw(Canvas canvas) {
        if (shader == null) {
            createShader();
        }
        if (shader != null && vwWidth > 0 && vwHeight > 0) {
            draw(canvas, imagePaint, borderPaint);
            return true;
        }

        return false;
    }

    public void onSizeChanged(int width, int height) {
        if (vwWidth == width && vwHeight == height) return;
        vwWidth = width;
        vwHeight = height;
        if (isSquare()) {
            vwWidth = vwHeight = Math.min(width, height);
        }
        if (shader != null) {
            calculateDrawableSizes();
        }
    }

    public Bitmap calculateDrawableSizes() {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            if (bitmapWidth > 0 && bitmapHeight > 0) {
                float width = Math.round(vwWidth - 2f * bordersWidth);
                float height = Math.round(vwHeight - 2f * bordersWidth);

                float scale;
                float translateX = 0;
                float translateY = 0;

                if (bitmapWidth * height > width * bitmapHeight) {
                    scale = height / bitmapHeight;
                    translateX = Math.round((width / scale - bitmapWidth) / 2f);
                } else {
                    scale = width / (float) bitmapWidth;
                    translateY = Math.round((height / scale - bitmapHeight) / 2f);
                }

                matrix.setScale(scale, scale);
                matrix.preTranslate(translateX, translateY);
                matrix.postTranslate(bordersWidth, bordersWidth);

                calculate(bitmapWidth, bitmapHeight, width, height, scale, translateX, translateY);

                return bitmap;
            }
        }

        reset();
        return null;
    }

    public final void onImageDrawableReset(Drawable drawable) {
        this.drawable = drawable;
        shader = null;
        imagePaint.setShader(null);
    }

    protected void createShader() {
        Bitmap bitmap = calculateDrawableSizes();
        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            imagePaint.setShader(shader);
        }
    }

    protected Bitmap getBitmap() {
        Bitmap bitmap = null;
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }
        }

        return bitmap;
    }

    public final int getBordersColor() {
        return bordersColor;
    }

    public final void setBordersColor(final int bordersColor) {
        this.bordersColor = bordersColor;
        if (borderPaint != null) {
            borderPaint.setColor(bordersColor);
        }
    }

    public final int getBordersWidth() {
        return bordersWidth;
    }

    public final void setBordersWidth(final int bordersWidth) {
        this.bordersWidth = bordersWidth;
        if (borderPaint != null) {
            borderPaint.setStrokeWidth(bordersWidth);
        }
    }

    public final float getBorderAlpha() {
        return borderAlpha;
    }

    public final void setBorderAlpha(final float borderAlpha) {
        this.borderAlpha = borderAlpha;
        if (borderPaint != null) {
            borderPaint.setAlpha(Float.valueOf(borderAlpha * ALPHA_MAXIMUM).intValue());
        }
    }

    public final void setSquare(final boolean square) {
        this.square = square;
    }

    public final boolean isSquare() {
        return square;
    }
}
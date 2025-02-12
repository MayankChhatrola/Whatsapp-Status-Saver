package com.example.whtsappstatussaver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.whtsappstatussaver.R;


public class RoundedShader extends ShaderHelper {

    private final RectF reactBorder = new RectF();
    private final RectF reactImage = new RectF();

    private int radius = 0;
    private int bitmapRadius;

    public RoundedShader() {
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        borderPaint.setStrokeWidth(bordersWidth *2);
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaShaderImageView, defStyle, 0);
            radius = typedArray.getDimensionPixelSize(R.styleable.WaShaderImageView_siRadius, radius);
            typedArray.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.drawRoundRect(reactBorder, radius, radius, borderPaint);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawRoundRect(reactImage, bitmapRadius, bitmapRadius, imagePaint);
        canvas.restore();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        reactBorder.set(bordersWidth, bordersWidth, vwWidth - bordersWidth, vwHeight - bordersWidth);
    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight,
                          float width, float height,
                          float scale,
                          float translateX, float translateY) {
        reactImage.set(-translateX, -translateY, bitmapWidth + translateX, bitmapHeight + translateY);
        bitmapRadius = Math.round(radius / scale);
    }

    @Override
    public void reset() {
        reactImage.set(0,0,0,0);
        bitmapRadius = 0;
    }

    public final int getRadius() {
        return radius;
    }

    public final void setRadius(final int radius) {
        this.radius = radius;
    }
}
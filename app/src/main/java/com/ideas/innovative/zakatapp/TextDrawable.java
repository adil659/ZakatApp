package com.ideas.innovative.zakatapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by adil6 on 2019-07-22.
 */

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private float height;

    public TextDrawable(String text, float height) {
        this.text = text;
        this.paint = new Paint();
        this.height = height;
        paint.setColor(Color.BLACK);
        paint.setTextSize(90f);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(text, 0, 40, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

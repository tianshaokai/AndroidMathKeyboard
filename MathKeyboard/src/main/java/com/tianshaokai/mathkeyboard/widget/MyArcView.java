package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaopeng on 2018/5/11.
 */

public class MyArcView extends View {
    private Paint mPaint;

    public MyArcView(Context context) {
        super(context);
        init();
    }

    public MyArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(12);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, getHeight() / 3, getWidth(), getHeight());
        canvas.drawArc(rectF, -30, -120, false, mPaint);
    }
}

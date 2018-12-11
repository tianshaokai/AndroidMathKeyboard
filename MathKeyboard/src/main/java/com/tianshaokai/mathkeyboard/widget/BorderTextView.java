package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tianshaokai.mathkeyboard.R;

import java.util.ArrayList;
import java.util.List;

public class BorderTextView extends CustomTextView {
    private final static String TAG = BorderTextView.class.getSimpleName();

    private Paint mPaint;

    private int sroke_width = 1;
    String borderDir;//边框的方向
    List<String> borderDir_trans;//边框的方向
    Integer borderWidth;//边框的宽度

    public BorderTextView(Context context) {
        super(context);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TYTextView);
        borderDir = typedArray.getString(R.styleable.TYTextView_borderDir);
        borderDir_trans = new ArrayList<>();
        if (borderDir != null && !"".equals(borderDir.trim())) {
            String borderDirtmp = borderDir.toUpperCase();
            if(!borderDirtmp.contains(",")){
                if("ALL".equals(borderDirtmp)){
                    borderDir_trans.add("L");
                    borderDir_trans.add("T");
                    borderDir_trans.add("R");
                    borderDir_trans.add("B");
                }else if("LEFT".equals(borderDirtmp)){
                    borderDir_trans.add("L");
                }else if("TOP".equals(borderDirtmp)){
                    borderDir_trans.add("T");
                }else  if("RIGHT".equals(borderDirtmp)){
                    borderDir_trans.add("R");
                }else if("BOTTOM".equals(borderDirtmp)){
                    borderDir_trans.add("B");
                }else if("NULL".equals(borderDirtmp)){
                    borderDir_trans.add("N");
                }
            }else{
               String []  tmpboder = borderDirtmp.split(",");
                for (int i = 0; i < tmpboder.length; i++) {
                    if("ALL".equals(tmpboder[i])){
                        borderDir_trans.add("L");
                        borderDir_trans.add("T");
                        borderDir_trans.add("R");
                        borderDir_trans.add("B");
                        break;
                    }else if("LEFT".equals(tmpboder[i])){
                        borderDir_trans.add("L");
                    }else if("TOP".equals(tmpboder[i])){
                        borderDir_trans.add("T");
                    }else  if("RIGHT".equals(tmpboder[i])){
                        borderDir_trans.add("R");
                    }else if("BOTTOM".equals(tmpboder[i])){
                        borderDir_trans.add("B");
                    }
                }
            }
        }else{
            borderDir_trans.add("L");
            borderDir_trans.add("T");
            borderDir_trans.add("R");
            borderDir_trans.add("B");
        }

        borderWidth = typedArray.getInt(R.styleable.TYTextView_TYBorderWidth, 1);
        sroke_width = borderWidth;
        setClickable(true);
        setOnTouchListener(mOnTouchListener);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.colorBorder));
        /*背景色恢复到正常状态下*/
        Drawable drawable_normal = getContext().getResources().getDrawable(R.drawable.textview_bg_normal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable_normal);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int strkeTmp = 1;//DensityUtil.dip2px(mContext,sroke_width);
        if (borderDir_trans.size() > 0) {
            for (int i = 0; i < borderDir_trans.size(); i++) {
                if ("L".equals(borderDir_trans.get(i))) {
                    canvas.drawRect(0, 0, strkeTmp, this.getHeight(), mPaint);//左边线条
                } else if ("T".equals(borderDir_trans.get(i))) {
                    canvas.drawRect(0, 0, this.getWidth(), strkeTmp, mPaint);//头部线条
                } else if ("R".equals(borderDir_trans.get(i))) {
                    canvas.drawRect(this.getWidth() - strkeTmp, 0, this.getWidth(), this.getHeight(),
                            mPaint);//右边
                } else if ("B".equals(borderDir_trans.get(i))) {
                    canvas.drawRect(0, this.getHeight() - strkeTmp, this.getWidth(), this.getHeight(),
                            mPaint);//底边
                }
            }
        } else {
            canvas.drawRect(0, 0, this.getWidth(), strkeTmp, mPaint);//头部线条
            canvas.drawRect(0, 0, strkeTmp, this.getHeight(), mPaint);//左边线条
            canvas.drawRect(this.getWidth() - strkeTmp, 0, this.getWidth(), this.getHeight(),
                    mPaint);//右边
            canvas.drawRect(0, this.getHeight() - strkeTmp, this.getWidth(), this.getHeight(),
                    mPaint);//底边
        }
    }

    OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i(TAG, "onTouch: --------->down");
                Drawable drawable_normal = getContext().getResources().getDrawable(R.drawable.textview_bg_press);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackground(drawable_normal);
                }

            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i(TAG, "onTouch: --------->up");
                   /*背景色恢复到正常状态下*/
                Drawable drawable_normal = getContext().getResources().getDrawable(R.drawable.textview_bg_normal);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackground(drawable_normal);
                }

            }
            return false;//下发事件
        }
    };
}
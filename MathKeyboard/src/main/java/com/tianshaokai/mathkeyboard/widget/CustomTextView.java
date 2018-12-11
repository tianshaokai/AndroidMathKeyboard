package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextView extends TextView {
    private static final String TAG = CustomTextView.class.getSimpleName();
    private Typeface iconfont;

    public CustomTextView(Context context) {
        super(context);
        this.init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        try {
            this.iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        } catch (Exception var3) {
            Log.e(TAG, " 加载 iconfont 字体文件失败");
        }

        this.setTypeface(this.iconfont);
    }
}

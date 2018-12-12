package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 编辑框UI
 */

public class EditView extends FormulaView {
    private static final String TAG = "EditView";
    public EditView(Context context) {
        super(context, 1);
        initView();
    }

    public EditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        Log.d(TAG, "添加编辑框");
        //重新设置latexview名称，用于操作
        latexView = getClass().getSimpleName();

        this.setBackgroundColor(Color.parseColor("#dddddd"));
        //设置可点击控件，并且初始化选中控件
        setCanClickView(this, false, true);
    }
}

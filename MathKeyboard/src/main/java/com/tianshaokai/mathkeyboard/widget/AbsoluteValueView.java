package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;

import java.util.logging.Logger;

public class AbsoluteValueView extends FormulaView {
    private static final String TAG = "AbsoluteValueView";
    private LinearLayout llyVmatrixRoot, llyVmatrix;

    public AbsoluteValueView(Context context, int level) {
        super(context, level);
        initView();
    }

    public AbsoluteValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AbsoluteValueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        Log.d(TAG, "--->添加矩阵组公式, 级别：" + level);

        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_absolute_value_1, this);
        } else if (level == 2) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_absolute_value_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_absolute_value_3, this);
        }

        llyVmatrixRoot = (LinearLayout) findViewById(R.id.llyVmatrixRoot);          // 根布局
        llyVmatrix = (LinearLayout) findViewById(R.id.llyVmatrix);          // 底数布局

        //设置可点击控件，并且初始化选中控件
        setCanClickView(llyVmatrixRoot, false, true);
        setCanClickView(llyVmatrix, true, false);
    }

    @Override
    public boolean isSpecial(String clickName) {
        return true;
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        //添加数据
        convertResult.message += "\\left|";
        toLatexString(llyVmatrix, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += "\\right|";
    }

    @Override
    public void parseLatexString(String string) {

    }
}

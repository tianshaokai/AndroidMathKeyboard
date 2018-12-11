package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;

public class Power10View extends FormulaView {
    private static final String TAG = "Power10View";
    private LinearLayout llyPowerRoot, llyPower;

    public Power10View(Context context, int level) {
        super(context, level);
        initView();
    }

    public Power10View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Power10View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        Log.d(TAG, "--->添加10的幂次方公式, 级别：" + level);

        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_power10_1, this);
        } else if (level == 2){
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_power10_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_power10_3, this);
        }

        llyPowerRoot = (LinearLayout) findViewById(R.id.llyPowerRoot);          // 根布局
        llyPower = (LinearLayout) findViewById(R.id.llyPower);

        //设置可点击控件，并且初始化选中控件
        setCanClickView(llyPowerRoot, false, true);
        setCanClickView(llyPower, true, false);
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\times10^";

        toLatexString(llyPower, convertResult);
    }
}

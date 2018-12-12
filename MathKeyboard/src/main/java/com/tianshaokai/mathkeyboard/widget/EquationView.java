package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.LatexConstant;

public class EquationView extends FormulaView {
    private static final String TAG = "EquationView";
    private LinearLayout llyEquationRoot, llyEquation1, llyEquation2;

    public EquationView(Context context, int level) {
        super(context, level);
        initView();
    }

    public EquationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EquationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        Log.d(TAG, "--->添加方程式公式, 级别：" + level);

        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_equation_1, this);
        } else if (level == 2){
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_equation_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_equation_3, this);
        }

        llyEquationRoot = (LinearLayout) findViewById(R.id.llyEquationRoot);          // 根布局
        llyEquation1 = (LinearLayout) findViewById(R.id.llyEquation1);          // 底数布局
        llyEquation2 = (LinearLayout) findViewById(R.id.llyEquation2);

        //设置可点击控件，并且初始化选中控件
        setCanClickView(llyEquationRoot, false, true);
        setCanClickView(llyEquation1, true, false);
        setCanClickView(llyEquation2, false, false);
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\begin{cases}";

        //添加分子
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(llyEquation1, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;

        convertResult.message += "\\\\";

        //添加分母
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(llyEquation2, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;

        convertResult.message += "\\end{cases}";
    }

    @Override
    public void parseLatexString(String string) {

    }
}

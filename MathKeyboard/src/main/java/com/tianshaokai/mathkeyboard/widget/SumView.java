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

/**
 * 求和
 */

public class SumView extends FormulaView {
    private static final String TAG = "SumView";
    private LinearLayout ll_Root, ll_UpRoot, ll_DownRoot;

    public SumView(Context context, int level) {
        super(context, level);
        initView();
    }

    protected SumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    protected SumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        Log.d(TAG, "--->添加分数公式, 级别：" + level);
        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sum_1, this);
        } else if (level == 2) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sum_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sum_3, this);
        }

        ll_Root = (LinearLayout) findViewById(R.id.sum_root);
        ll_UpRoot = (LinearLayout) findViewById(R.id.sum_up_root);
        ll_DownRoot = (LinearLayout) findViewById(R.id.sum_down_root);
        //设置可点击控件，并且初始化选中控件
        setCanClickView(ll_Root, false, true);
        setCanClickView(ll_UpRoot, true, false);
        setCanClickView(ll_DownRoot, false, false);
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\sum_";

        //添加分母
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(ll_DownRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;

        convertResult.message += "^";

        //添加分子
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(ll_UpRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;
    }

    @Override
    public void parseLatexString(String string) {

    }
}

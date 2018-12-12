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
import com.tianshaokai.mathkeyboard.manager.View2Latex;

import java.util.logging.Logger;

/**
 * 分数视图
 */

public class FractionView extends FormulaView {
    private static final String TAG = "FractionView";
    private LinearLayout ll_Root, ll_NumeratorRoot, ll_DenominatorRoot;

    public FractionView(Context context, int level) {
        super(context, level);
        initView();
    }

    protected FractionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    protected FractionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        Log.d(TAG, "--->添加分数公式, 级别：" + level);
        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_fraction_1, this);
        } else if (level == 2) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_fraction_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_fraction_3, this);
        }

        ll_Root = (LinearLayout) findViewById(R.id.fraction_root);
        ll_NumeratorRoot = (LinearLayout) findViewById(R.id.numerator_root);
        ll_DenominatorRoot = (LinearLayout) findViewById(R.id.denominator_root);
        //设置可点击控件，并且初始化选中控件
        setCanClickView(ll_Root, false, true);
        setCanClickView(ll_NumeratorRoot, true, false);
        setCanClickView(ll_DenominatorRoot, false, false);
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\" + View2Latex.getLatexName(this);

        //添加分子
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(ll_NumeratorRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;

        //添加分母
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(ll_DenominatorRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;
    }

    @Override
    public void parseLatexString(String string) {
        Log.d(TAG, "frac latex：" + string);

        boolean hasLeft = false;
        int skipBraceLeftCount = 0;

        for (int i = 0; i < string.length(); ++i) {
            String subStr = string.substring(i, i + 1);
            if (subStr.equals(LatexConstant.Brace_Left)) {
                if (!hasLeft) {
                    hasLeft = true;
                } else {
                    ++skipBraceLeftCount;
                }
            } else if (subStr.equals(LatexConstant.Brace_Right)) {
                if (skipBraceLeftCount > 0) {
                    --skipBraceLeftCount;
                } else if (skipBraceLeftCount == 0) {
                    if (hasLeft) {
                        parseLatexString(ll_NumeratorRoot, string.substring(1, i));
                        parseLatexString(ll_DenominatorRoot, string.substring(i + 2, string.length() - 1));

                        break;
                    }
                } else {
                    Log.e(TAG, "计算出错：" + skipBraceLeftCount);
                }
            }
        }
    }
}

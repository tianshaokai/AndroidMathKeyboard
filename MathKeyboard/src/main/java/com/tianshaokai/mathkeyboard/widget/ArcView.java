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
 * 弧
 * Created by xiaopeng on 2018/5/10.
 */

public class ArcView extends FormulaView {
    private static final String TAG = "ArcView";
    private LinearLayout ll_Root, ll_Point_root;

    public ArcView(Context context, int level) {
        super(context, level);
        initView();
    }

    protected ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        Log.d(TAG, "--->添加分数公式, 级别：" + level);
        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_arc_1, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_arc_2, this);
        }

        ll_Root = (LinearLayout) findViewById(R.id.arc_root);
        ll_Point_root = (LinearLayout) findViewById(R.id.point_root);
        //设置可点击控件，并且初始化选中控件
        setCanClickView(ll_Root, false, true);
        setCanClickView(ll_Point_root, true, false);
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "_";

        //添加分子
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(ll_Point_root, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;
        convertResult.message += "^{\\frown}";
    }
}

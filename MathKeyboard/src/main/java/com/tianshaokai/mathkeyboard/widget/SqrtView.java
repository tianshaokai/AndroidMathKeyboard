package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.View2Latex;

/**
 * 根号视图 2√5
 */

public class SqrtView extends FormulaView {
    private static final String TAG = "SqrtView";
    private LinearLayout ll_SqrtRoot,ll_MultipleRoot,ll_RadicandRoot;

    public SqrtView(Context context, int level) {
        super(context, level);
        initView();
    }

    public SqrtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SqrtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        Log.d(TAG, "--->添加根号公式, 级别：" + level);

        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sqrt_1, this);
        } else if (level == 2){
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sqrt_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sqrt_3, this);
        }

        ll_SqrtRoot = (LinearLayout) findViewById(R.id.sqrt_root);          // 根布局
        ll_MultipleRoot = (LinearLayout) findViewById(R.id.multiple_root);  // 倍数布局
        ll_RadicandRoot = (LinearLayout) findViewById(R.id.radicand_root);  // 被开方数布局

        //设置可点击控件，并且初始化选中控件
        setCanClickView(ll_SqrtRoot, false, true);
        setCanClickView(ll_MultipleRoot, false, false);
        setCanClickView(ll_RadicandRoot, true, false);
    }

    @Override
    public boolean isSpecial(String clickName) {
        if (clickName.equals(getResources().getResourceEntryName(ll_RadicandRoot.getId()))) {
            return true;
        }
        return false;
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\" + View2Latex.getLatexName(this);

        //添加分子
        convertResult.message += "[";
        toLatexString(ll_MultipleRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += "]";

        //添加坟墓
        convertResult.message += "{";
        toLatexString(ll_RadicandRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += "}";
    }
}

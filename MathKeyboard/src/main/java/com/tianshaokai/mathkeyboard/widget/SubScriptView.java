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
import com.tianshaokai.mathkeyboard.manager.MathFormulaFactory;

public class SubScriptView extends FormulaView {
    private static final String TAG = "SubScriptView";
    private LinearLayout llySubScriptRoot, llyBase, llySub;

    public SubScriptView(Context context, int level, String base, String sub) {
        super(context, level);
        initView(base, sub);
    }

    public SubScriptView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(null, null);
    }

    public SubScriptView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(null, null);
    }

    private void initView(String base, String sub){
        Log.d(TAG, "--->添加下标公式, 级别：" + level);

        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_subscript_1, this);
        } else if (level == 2){
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_subscript_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_subscript_3, this);
        }

        llySubScriptRoot = (LinearLayout) findViewById(R.id.llySubScriptRoot);          // 根布局
        llyBase = (LinearLayout) findViewById(R.id.llyBase);          // 底数布局
        llySub = (LinearLayout) findViewById(R.id.llySub);          // 底数布局

        //设置可点击控件，并且初始化选中控件
        setCanClickView(llySubScriptRoot, false, true);
        setCanClickView(llyBase, true, false);
        setCanClickView(llySub, false, false);

        if (base != null && !base.isEmpty()) {
            SimpleSymbolView symbolView = new MathFormulaFactory().newInstance(getContext(),
                    String.valueOf(base), level);

            addChildView(getResources().getResourceEntryName(R.id.llyBase), -1, symbolView);
        }

        if (sub != null && !sub.isEmpty()) {
            SimpleSymbolView symbolView = new MathFormulaFactory().newInstance(getContext(),
                    String.valueOf(sub), level + 1);

            addChildView(getResources().getResourceEntryName(R.id.llySub), -1, symbolView);
        }
    }

    @Override
    public boolean isSpecial(String clickName) {
        if (clickName.equals(getResources().getResourceEntryName(llyBase.getId()))) {
            return true;
        }
        return false;
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        toLatexString(llyBase, convertResult);

        convertResult.message += "_";

        //添加数据
        convertResult.message += LatexConstant.Brace_Left;
        toLatexString(llySub, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
        convertResult.message += LatexConstant.Brace_Right;
    }

    @Override
    public void parseLatexString(String string) {

    }
}

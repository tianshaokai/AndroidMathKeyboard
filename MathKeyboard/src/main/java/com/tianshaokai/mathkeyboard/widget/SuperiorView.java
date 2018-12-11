package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.LatexConstant;
import com.tianshaokai.mathkeyboard.manager.MathFormulaFactory;

/**
 * 分数视图
 */

public class SuperiorView extends FormulaView {
    private static final String TAG = "SuperiorView";
    private TextView tvSuperior;
    private LinearLayout superior1_root, llyParamRoot;

    public SuperiorView(Context context, int level, String superior) {
        super(context, level);
        initView(superior);
    }

    protected SuperiorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(null);
    }

    protected SuperiorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(null);
    }


    private void initView(String superior) {
        Log.d(TAG, "--->添加******公式, 级别：" + level);
        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_superior_1, this);
        } else if (level == 2) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_superior_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_superior_3, this);
        }

        superior1_root = (LinearLayout) findViewById(R.id.superior_root);
        tvSuperior = (TextView) findViewById(R.id.tvSuperior);
        tvSuperior.setText(superior);
        llyParamRoot = (LinearLayout) findViewById(R.id.llyParamRoot);
        //设置可点击控件，并且初始化选中控件
        setCanClickView(superior1_root, false, true);
        setCanClickView(llyParamRoot, true, false);

        if (superior != null && !superior.isEmpty()) {
            SimpleSymbolView symbolView = new MathFormulaFactory().newInstance(getContext(),
                    String.valueOf(superior), level + 1);

            addChildView(getResources().getResourceEntryName(R.id.llyPower), -1, symbolView);
        }
    }

    @Override
    public boolean isSpecial(String clickName) {
        return true;
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        toLatexString(llyParamRoot, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }

        String superior = tvSuperior.getText().toString();
        if (superior.equals("°")) {
            convertResult.message += "^";
            convertResult.message += LatexConstant.Brace_Left;
            convertResult.message += "\\circ";
            convertResult.message += LatexConstant.Brace_Right;
        } else {
            convertResult.message += superior;
        }
    }

    @Override
    public void parseLatexString(String string) {

    }
}

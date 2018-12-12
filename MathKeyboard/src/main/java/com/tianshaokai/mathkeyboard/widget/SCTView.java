package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianshaokai.mathkeyboard.R;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;

/**
 * 正弦函数
 */
public class SCTView extends FormulaView {
    private static final String TAG = "SCTView";
    private TextView tvSCT;
    private LinearLayout ll_Sine_Root, ll_Sine_None_Root;

    public SCTView(Context context, int level, String sct) {
        super(context, level);
        initView(sct);
    }

    public SCTView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(null);
    }

    public SCTView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(null);
    }

    private void initView(String sct) {
        if (level == 1) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sct_1, this);
        } else if (level == 2) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sct_2, this);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_latex_sct_3, this);
        }

        ll_Sine_Root = (LinearLayout) findViewById(R.id.sine_root);          // 根布局
        tvSCT = (TextView) findViewById(R.id.tvSCT);
        tvSCT.setText(sct);
        ll_Sine_None_Root = (LinearLayout) findViewById(R.id.sine_none_root);

        //设置可点击控件，并且初始化选中控件
        setCanClickView(ll_Sine_Root, false, true);
        setCanClickView(ll_Sine_None_Root, true, false);
    }

    @Override
    public boolean isSpecial(String clickName) {
        return true;
    }

    @Override
    public void toLatexString(ConvertResult convertResult) {
        convertResult.message += "\\" + tvSCT.getText().toString() + " ";

        //添加数据
        toLatexString(ll_Sine_None_Root, convertResult);
        if (!convertResult.isSuccess) {
            return;
        }
    }
}

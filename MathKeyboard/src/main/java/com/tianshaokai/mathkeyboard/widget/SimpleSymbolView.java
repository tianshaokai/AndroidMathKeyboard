package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.LatexConstant;
import com.tianshaokai.mathkeyboard.manager.ViewAssembleManager;
import com.tianshaokai.mathkeyboard.utils.UUIDUtil;

/**
 * 简单符号view
 */

public class SimpleSymbolView extends AppCompatTextView {
    private static final String TAG = "SimpleSymbolView";
    private String symbolName;
    private int level = 1;
    private String parentLatexView, parentClickView;
    private float touchX, touchY;

    public SimpleSymbolView(Context context, String text, int level) {
        super(context);
        symbolName = getClass().getSimpleName() + "&" + UUIDUtil.generator();
        initView(text, level);
        this.level = level;
    }

    public SimpleSymbolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        symbolName = getClass().getSimpleName() + "&" + UUIDUtil.generator();
    }

    public SimpleSymbolView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        symbolName = getClass().getSimpleName() + "&" + UUIDUtil.generator();
    }

    private void initView(String textContent, int level) {
        Log.d(TAG, "添加符号：" + textContent + "  level：" + level);
        setText(textContent);
        setGravity(Gravity.CENTER);

        if (level == 1) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        } else if (level == 2) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        } else {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        setOnClickListener(clickListener);
        setOnTouchListener(touchListener);
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public int getLevel() {
        return this.level;
    }

    public String getParentLatexView() {
        return parentLatexView;
    }

    public void setParentLatexView(String parentLatexView) {
        this.parentLatexView = parentLatexView;
    }

    public String getParentClickView() {
        return parentClickView;
    }

    public void setParentClickView(String parentClickView) {
        this.parentClickView = parentClickView;
    }

    private View.OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int[] location=new int[2];
            v.getLocationOnScreen(location);
            if (touchX < v.getWidth() / 2) {
                ViewAssembleManager.getInstance().addLine(parentLatexView, parentClickView, symbolName, level, true);
            } else {
                ViewAssembleManager.getInstance().addLine(parentLatexView, parentClickView, symbolName, level, false);
            }
        }
    };

    private View.OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchX = event.getX();
                touchY = event.getY();
            }
            return false;
        }
    };

    public void toLatexString(ConvertResult convertResult) {
        if (getText().toString().equals(LatexConstant.Parenthesis_Left)) {
            convertResult.message += "\\left(";
        } else if (getText().toString().equals(LatexConstant.Parenthesis_Right)) {
            convertResult.message += "\\right)";
        } else if (getText().toString().equals(LatexConstant.AddSubtract)) {
            convertResult.message += "\\pm ";
        } else if (getText().toString().equals(LatexConstant.MULTIPLICATION)) {
            convertResult.message += "\\times ";
        } else if (getText().toString().equals(LatexConstant.DIVISION)) {
            convertResult.message += "\\div ";
        } else if (getText().toString().equals(LatexConstant.Less_Than_And_Equal)) {
            convertResult.message += "\\le ";
        } else if (getText().toString().equals(LatexConstant.Greater_Than_And_Equal)) {
            convertResult.message += "\\ge ";
        } else if (getText().toString().equals(LatexConstant.Not_Equal)) {
            convertResult.message += "\\ne ";
        } else if (getText().toString().equals(LatexConstant.PERCENT)) {
            convertResult.message += "\\% ";
        } else if (getText().toString().equals(LatexConstant.Approx)) {
            convertResult.message += "\\approx ";
        } else if (getText().toString().equals(LatexConstant.DoubleLine)) {
            convertResult.message += "\\parallel ";
        } else if (getText().toString().equals(LatexConstant.or)) {
            convertResult.message += "\\cup ";
        } else if (getText().toString().equals(LatexConstant.and)) {
            convertResult.message += "\\cap ";
        } else if (getText().toString().equals(LatexConstant.Triangle)) {
            convertResult.message += "\\triangle ";
        } else if (getText().toString().equals(LatexConstant.Angle)) {
            convertResult.message += "\\angle ";
        } else if (getText().toString().equals(LatexConstant.Cong)) {
            convertResult.message += "\\cong ";
        } else if (getText().toString().equals(LatexConstant.PI)) {
            convertResult.message += "\\pi ";
        } else if (getText().toString().equals(LatexConstant.Perp)) {
            convertResult.message += "\\perp ";
        } else if (getText().toString().equals(LatexConstant.ALPHA)) {
            convertResult.message += "\\alpha ";
        } else if (getText().toString().equals(LatexConstant.BETA)) {
            convertResult.message += "\\beta ";
        } else if (getText().toString().equals(LatexConstant.THETA)) {
            convertResult.message += "\\theta ";
        } else if (getText().toString().equals(LatexConstant.DELTA)) {
            convertResult.message += "\\Delta ";
        } else if (getText().toString().equals(LatexConstant.Sim)) {
            convertResult.message += "\\sim ";
        } else if (getText().toString().equals(LatexConstant.Because)) {
            convertResult.message += "\\because ";
        } else if (getText().toString().equals(LatexConstant.Therefore)) {
            convertResult.message += "\\therefore ";
        } else if (getText().toString().equals(LatexConstant.Product)) {
            convertResult.message += "\\cdot ";
        } else if (getText().toString().equals(LatexConstant.Circle)) {
            convertResult.message += "\\odot ";
        } else {
            convertResult.message += getText().toString();
        }
    }

    public void parseLatexString(String string) {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ">>>" + getText().toString();
    }
}


package com.tianshaokai.mathkeyboard.manager;

import android.content.Context;
import android.widget.Toast;

import com.tianshaokai.mathkeyboard.widget.FormulaView;
import com.tianshaokai.mathkeyboard.widget.SimpleSymbolView;


/**
 * latex组装器
 */

public class Assembles {
    private static Assembles instants;

    public Assembles() {

    }

    public static Assembles getInstants() {
        synchronized (Assembles.class) {
            if (instants == null) {
                instants = new Assembles();
            }
        }

        return instants;
    }

    public FormulaView addMathFormula(Context context, MathFormula mathFormula) {
        if (ViewAssembleManager.getInstance().getSelectedViewLevel() > 3) {
            Toast.makeText(context, "不能在添加子节点，已经达到最深层次", Toast.LENGTH_SHORT);
            return null;
        }
        SelectedStruct selectedStruct = ViewAssembleManager.getInstance().getSelectedStruct();
        FormulaView viewGroup = new MathFormulaFactory().newInstance(context, mathFormula,
                ViewAssembleManager.getInstance().getSelectedViewLevel());
        ViewAssembleManager.getInstance().addMathFormula(viewGroup, selectedStruct);
        return viewGroup;
    }

    public SimpleSymbolView addSimpleSymbol(Context context, String content) {
        SimpleSymbolView symbolView = new MathFormulaFactory().newInstance(context, content,
                ViewAssembleManager.getInstance().getSelectedViewLevelForSymbol());
        ViewAssembleManager.getInstance().addSimpleSymbol(symbolView);
        return symbolView;
    }

    public void deleteMathFormula(Context context) {
        ViewAssembleManager.getInstance().deleteMathFormula();
    }
}

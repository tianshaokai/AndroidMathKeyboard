package com.tianshaokai.mathkeyboard.manager;

import com.tianshaokai.mathkeyboard.widget.FormulaView;
import com.tianshaokai.mathkeyboard.widget.FractionView;
import com.tianshaokai.mathkeyboard.widget.Sqrt2View;
import com.tianshaokai.mathkeyboard.widget.SqrtView;

/**
 * view 转 latex
 */

public class View2Latex {

    public static String getLatexName(FormulaView formulaView) {
        String className = formulaView.getClass().getSimpleName();
        if (className.equals(FractionView.class.getSimpleName())) {
            return LatexConstant.Fraction;
        } else if (className.equals(SqrtView.class.getSimpleName())) {
            return LatexConstant.Sqrt;
        } else if (className.equals(Sqrt2View.class.getSimpleName())) {
            return LatexConstant.Sqrt;
        }

        return null;
    }
}

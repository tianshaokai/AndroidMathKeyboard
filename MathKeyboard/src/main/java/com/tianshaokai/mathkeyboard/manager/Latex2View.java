package com.tianshaokai.mathkeyboard.manager;

/**
 * latex转view
 */

public class Latex2View {

    public static MathFormula getFormulaType(String latex) {
        if (latex.equals(LatexConstant.Fraction)) {
            return MathFormula.Fraction;
        } else if (latex.equals(LatexConstant.Sqrt)) {
            return MathFormula.Sqrt;
        }

        return null;
    }
}

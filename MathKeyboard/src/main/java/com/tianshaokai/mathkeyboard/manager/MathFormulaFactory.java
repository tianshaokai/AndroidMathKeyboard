package com.tianshaokai.mathkeyboard.manager;

import android.content.Context;

import com.tianshaokai.mathkeyboard.widget.AbsoluteValueView;
import com.tianshaokai.mathkeyboard.widget.AngleView;
import com.tianshaokai.mathkeyboard.widget.ArcView;
import com.tianshaokai.mathkeyboard.widget.Equation3View;
import com.tianshaokai.mathkeyboard.widget.EquationView;
import com.tianshaokai.mathkeyboard.widget.FormulaView;
import com.tianshaokai.mathkeyboard.widget.FractionView;
import com.tianshaokai.mathkeyboard.widget.Power10View;
import com.tianshaokai.mathkeyboard.widget.PowerView;
import com.tianshaokai.mathkeyboard.widget.SCTView;
import com.tianshaokai.mathkeyboard.widget.SimpleSymbolView;
import com.tianshaokai.mathkeyboard.widget.Sqrt2View;
import com.tianshaokai.mathkeyboard.widget.SqrtView;
import com.tianshaokai.mathkeyboard.widget.SubScriptView;
import com.tianshaokai.mathkeyboard.widget.SumView;
import com.tianshaokai.mathkeyboard.widget.SuperiorView;


/**
 * 创建数学公式功能
 */

public class MathFormulaFactory {

    public MathFormulaFactory() {

    }

    public FormulaView newInstance(Context context, MathFormula formula, int level) {
        if (formula.equals(MathFormula.Fraction)) {
            return new FractionView(context, level);
        }
        //根号
        else if (formula.equals(MathFormula.Sqrt)) {
            return new SqrtView(context, level);
        } else if (formula.equals(MathFormula.Sqrt2)) {
            return new Sqrt2View(context, level);
        }
        //幂次方
        else if (formula.equals(MathFormula.Power)) {
            return new PowerView(context, level, null, 0);
        } else if (formula.equals(MathFormula.Power2)) {
            return new PowerView(context, level, null, 2);
        } else if (formula.equals(MathFormula.PowerX2)) {
            return new PowerView(context, level, "x", 2);
        } else if (formula.equals(MathFormula.Power10)) {
            return new Power10View(context, level);
        }
        //角度
        else if (formula.equals(MathFormula.Sine)) {
            return new SCTView(context, level, "sin");
        } else if (formula.equals(MathFormula.Cosine)) {
            return new SCTView(context, level, "cos");
        } else if (formula.equals(MathFormula.Tangent)) {
            return new SCTView(context, level, "tan");
        }
        //πr²
        else if (formula.equals(MathFormula.PIR2)) {
            return new PowerView(context, level, "πr", 2);
        }
        //方程式
        else if (formula.equals(MathFormula.Equation)) {
            return new EquationView(context, level);
        } else if (formula.equals(MathFormula.Equation_3)) {
            return new Equation3View(context, level);
        }
        //绝对值
        else if (formula.equals(MathFormula.AbsoluteValue)) {
            return new AbsoluteValueView(context, level);
        }
        //下标
        else if (formula.equals(MathFormula.SubScript)) {
            return new SubScriptView(context, level, null, null);
        }
        else if (formula.equals(MathFormula.SubScriptX1)) {
            return new SubScriptView(context, level, "x", "1");
        }
        else if (formula.equals(MathFormula.SubScriptX2)) {
            return new SubScriptView(context, level, "x", "2");
        }
        //三角
        else if (formula.equals(MathFormula.Angle)) {
            return new AngleView(context, level);
        }
        //度数，分，秒
        else if (formula.equals(MathFormula.Circ)) {
            return new SuperiorView(context, level, "°");
        }
        else if (formula.equals(MathFormula.Minute)) {
            return new SuperiorView(context, level, "'");
        }
        else if (formula.equals(MathFormula.Second)) {
            return new SuperiorView(context, level, "''");
        }
        //求和
        else if (formula.equals(MathFormula.Sum)) {
            return new SumView(context, level);
        }
        //弧
        else if (formula.equals(MathFormula.Arc)) {
            return new ArcView(context, level);
        }
        return null;
    }

    public SimpleSymbolView newInstance(Context context, String content, int level) {
        if (context == null || content == null) {
            return null;
        }
        return new SimpleSymbolView(context, content, level);
    }
}

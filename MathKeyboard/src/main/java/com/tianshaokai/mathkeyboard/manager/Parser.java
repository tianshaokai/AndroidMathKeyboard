package com.tianshaokai.mathkeyboard.manager;

import android.content.Context;
import android.util.Log;

import com.tianshaokai.mathkeyboard.widget.FormulaView;

/**
 * latex解析器
 */

public class Parser {

    private static final String TAG = "Parser";

    public static void latex2View(Context context, String str) {
        Log.d(TAG, "需要解析的latex公式为：" + str);

        for (int i = 0; i < str.length(); ) {
            String subStr = str.substring(i, i + 1);

            if (subStr.equals("\\")) {
                subStr = str.substring(i + 1, str.indexOf("{", i + 1));
                i += subStr.length() + 1;

                MathFormula mathFormula = Latex2View.getFormulaType(subStr);
                FormulaView formulaView = Assembles.getInstants().addMathFormula(context, mathFormula);

                if (mathFormula == MathFormula.Fraction) {
                    String content = "";
                    int mathCount = 0, skipBraceLeftCount = 0;
                    boolean hasLeft = false;

                    for (int j = i; j < str.length(); ++j, ++i) {
                        subStr = str.substring(j, j + 1);
                        content += subStr;

                        if (subStr.equals(LatexConstant.Brace_Left)) {
                            if (!hasLeft) {
                                hasLeft = true;
                            } else {
                                ++skipBraceLeftCount;
                            }
                        } else if (subStr.equals(LatexConstant.Brace_Right)) {
                            if (skipBraceLeftCount > 0) {
                                --skipBraceLeftCount;
                            } else if (skipBraceLeftCount == 0) {

                                if (hasLeft) {
                                    // TODO: 2017/5/24
                                    ++mathCount;
                                    hasLeft = false;
                                    if (mathCount == 1) {
                                        subStr = str.substring(j + 1, j + 2);
                                        if (!subStr.equals(LatexConstant.Brace_Left)) {
                                            //需要判断两个花括号是否挨在一起，即下一个字符是否是 {
                                            Log.e(TAG,"解析出错了：" + subStr);
                                            break;
                                        }
                                    } else if (mathCount == 2) {
                                        break;
                                    }
                                }
                            } else {
                                Log.e(TAG,"计算出错：" + skipBraceLeftCount);
                            }
                        }
                    }
                    formulaView.parseLatexString(content);
                    //重置光标位置
                    ViewAssembleManager.getInstance().getRootView().performClick();
                    ++i;
                }

            } else {
                Assembles.getInstants().addSimpleSymbol(context, subStr);
                Log.d(TAG,"add symbol：" + subStr);
                ++i;
            }
        }
    }

    private boolean isSeparator(String subStr) {
        if (subStr.equals("")) {
            return true;
        }
        return true;
    }
}

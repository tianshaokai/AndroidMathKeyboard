package com.tianshaokai.mathkeyboard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshaokai.mathkeyboard.manager.Assembles;
import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.LateXConfig;
import com.tianshaokai.mathkeyboard.manager.LatexConstant;
import com.tianshaokai.mathkeyboard.manager.MathFormula;
import com.tianshaokai.mathkeyboard.manager.Parser;
import com.tianshaokai.mathkeyboard.manager.ViewAssembleManager;
import com.tianshaokai.mathkeyboard.utils.LatexUtil;
import com.tianshaokai.mathkeyboard.utils.RichText;
import com.tianshaokai.mathkeyboard.widget.BorderTextView;
import com.tianshaokai.mathkeyboard.widget.EditView;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFragment extends DialogFragment {
    public static final String TAG = KeyboardFragment.class.getSimpleName();

    //外部的textView，用于展示输入结果
    private TextView tvOutSide;
    private String originContent;

    private TextView tvConfirm;
    private EditView evLatexView;
    private HorizontalScrollView hsvLatex;
    /**
     * 默认 代数 几何 字母
     */
    private TextView tvDefault, tvAlgebra, tvGeometry, tvLetter;

    private LinearLayout llMathKeyBoardLayout, llLatterKeyBoardLayout;
    private LinearLayout llDefaultLayout, llGeometryLayout;
    private LinearLayout tlAlgebraLayout;

    /******  第一页  ********/
    //公式类型：分号, 平方根, 二次平方, 平方
    private TextView ivFraction, ivSqrt2, ivPower2;
    //符号类型：左右括号, 逗号, 句号
    private TextView tvBracketsLeft, tvBracketsRight, tvComma, tvDecimalPoint;
    //运算符号：加, 减, 加减, 乘, 除, 等
    private TextView tvPlus, tvMinus, tvAddSubtract, tvMultiplication, tvDivision, tvEqual, tvNotEqual;
    //数字
    private TextView tvNum0, tvNum1, tvNum2, tvNum3, tvNum4, tvNum5, tvNum6, tvNum7, tvNum8, tvNum9;
    //删除按钮
    private TextView ivDelete1;


    /******  第二页  *********/
    //运算符：小于，大于，小于等于，大于等于，不等于，
    private TextView tvLessThan, tvGreaterThan, tvLessThanAndEqual, tvGreaterAndEqual;
    //符号类型：冒号，百分号，左中括号，右中括号
    private TextView tvColon, tvPercent, tvMiddleBracketsLeft, tvMiddleBracketsRight;
    //公式类型：平方，方程式组，三项方程组，二次平方，开根，矩阵，下标，2的幂次方，x的二次方，10的幂次方，分号
    private TextView ivPower, ivEquation, ivEquation_3, ivSqrt2_algebra, ivSqrt, ivAbsoluteValue, ivSubScript,
            ivPower2_algebra, ivPowerX2, ivPower10, ivFraction_aglebra, tvSum;
    //特殊符号：x_1, x_2,
    private TextView ivSubScriptX1, ivSubScriptX2;
    //因为，所以，乘积
    private TextView tvBecause, tvTherefore, tvProduct;
    //删除按钮
    private TextView ivDelete2;


    /******  第三页  *********/
    //公式：sin, cos, tan
    private TextView tvSine, tvCosine, tvTangent;
    //特殊字符：角度, 三角朝上, 全等于，相似于，约等于，垂直，双竖线
    private TextView tvAngle, tvTriangle, tvCong, tvSim, tvApprox, tvPerp, tvDoubleLine;
    //特殊字符：度数, 分, 秒
    private TextView ivCirc, ivMinute, ivSecond;
    //常用符号：π， πr²
    private TextView tvPi, tvPiR2;
    //希腊字母
    private TextView tvAlpha, tvBeta, tvTheta, tvDelta;
    //圆, 弧
    private TextView tvCircle, tvArc;

    //删除按钮
    private TextView ivDelete3;


    /******  第四页  *********/
    private Button btnLowerUpperCase, btnBack, btnDelete, btnComma, btnDivision, btnPlus,
            btnMinus, btnMultiplication, btnEqual;
    private Button btnLatterA, btnLatterB, btnLatterC, btnLatterD, btnLatterE, btnLatterF, btnLatterG,
            btnLatterH, btnLatterI, btnLatterJ, btnLatterK, btnLatterL, btnLatterM, btnLatterN,
            btnLatterO, btnLatterP, btnLatterQ, btnLatterR, btnLatterS, btnLatterT,
            btnLatterU, btnLatterV, btnLatterW, btnLatterX, btnLatterY, btnLatterZ;

    private String[] upperLatter = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private String[] lowerLatter = {"a", "b", "c", "d", "e", "f", "g",
            "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z"};

    private boolean toUpperKeyboard = true;

    private List<Button> latterViewList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.MathBottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.layout_keybord_dialog_fragment);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        findView(dialog);

        initList();

        initLatex();

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        parseOriginCotent();

        return dialog;
    }

    private void findView(Dialog dialog) {
        menuKeyboard(dialog);
        defaultKeyboard(dialog);
        algebraKeyboard(dialog);
        geometryKeyboard(dialog);
        latterKeyboard(dialog);

        tvDefault.performClick();
    }

    private void parseOriginCotent() {
        if (originContent != null && !originContent.isEmpty()) {
            Parser.latex2View(KeyboardFragment.this.getContext(), originContent);
        }
    }

    /**
     * 键盘左侧菜单
     */
    private void menuKeyboard(Dialog dialog) {
        evLatexView = (EditView) dialog.findViewById(R.id.evLatexView);
        hsvLatex = (HorizontalScrollView) dialog.findViewById(R.id.hsvLatex);

        tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);

        llMathKeyBoardLayout = (LinearLayout) dialog.findViewById(R.id.llMathKeyBoardLayout);
        llLatterKeyBoardLayout = (LinearLayout) dialog.findViewById(R.id.llLatterKeyBoardLayout);

        llDefaultLayout = (LinearLayout) dialog.findViewById(R.id.llDefaultLayout);
        tlAlgebraLayout = (LinearLayout) dialog.findViewById(R.id.tlAlgebraLayout);
        llGeometryLayout = (LinearLayout) dialog.findViewById(R.id.llGeometryLayout);

        tvDefault = (TextView) dialog.findViewById(R.id.tvDefault);
        tvAlgebra = (TextView) dialog.findViewById(R.id.tvAlgebra);
        tvGeometry = (TextView) dialog.findViewById(R.id.tvGeometry);
        tvLetter = (TextView) dialog.findViewById(R.id.tvLetter);

        tvConfirm.setOnClickListener(onClickListener);
        tvDefault.setOnClickListener(onClickListener);
        tvAlgebra.setOnClickListener(onClickListener);
        tvGeometry.setOnClickListener(onClickListener);
        tvLetter.setOnClickListener(onClickListener);
    }

    /**
     * 默认键盘
     */
    private void defaultKeyboard(Dialog dialog) {
        //公式类型：分号, 平方根, 二次平方, 平方
        ivFraction = (TextView) dialog.findViewById(R.id.ivFraction);
        ivSqrt2 = (TextView) dialog.findViewById(R.id.ivSqrt2);
        ivPower2 = (TextView) dialog.findViewById(R.id.ivPower2);
        //符号类型：左右括号, 逗号, 句号
        tvBracketsLeft = (TextView) dialog.findViewById(R.id.tvBracketsLeft);
        tvBracketsRight = (TextView) dialog.findViewById(R.id.tvBracketsRight);
        tvComma = (TextView) dialog.findViewById(R.id.tvComma);
        tvDecimalPoint = (TextView) dialog.findViewById(R.id.tvDecimalPoint);
        //运算符号：加, 减, 加减, 乘, 除, 等
        tvPlus = (TextView) dialog.findViewById(R.id.tvPlus);
        tvMinus = (TextView) dialog.findViewById(R.id.tvMinus);
        tvAddSubtract = (TextView) dialog.findViewById(R.id.tvAddSubtract);
        tvMultiplication = (TextView) dialog.findViewById(R.id.tvMultiplication);
        tvDivision = (TextView) dialog.findViewById(R.id.tvDivision);
        tvEqual = (TextView) dialog.findViewById(R.id.tvEqual);
        tvNotEqual = (TextView) dialog.findViewById(R.id.tvNotEqual);
        //数字
        tvNum0 = (TextView) dialog.findViewById(R.id.tvNum0);
        tvNum1 = (TextView) dialog.findViewById(R.id.tvNum1);
        tvNum2 = (TextView) dialog.findViewById(R.id.tvNum2);
        tvNum3 = (TextView) dialog.findViewById(R.id.tvNum3);
        tvNum4 = (TextView) dialog.findViewById(R.id.tvNum4);
        tvNum5 = (TextView) dialog.findViewById(R.id.tvNum5);
        tvNum6 = (TextView) dialog.findViewById(R.id.tvNum6);
        tvNum7 = (TextView) dialog.findViewById(R.id.tvNum7);
        tvNum8 = (TextView) dialog.findViewById(R.id.tvNum8);
        tvNum9 = (TextView) dialog.findViewById(R.id.tvNum9);
        //删除按钮
        ivDelete1 = (TextView) dialog.findViewById(R.id.ivDelete1);

        //公式类型：分号, 平方根, 二次平方, 平方
        ivFraction.setOnClickListener(onClickListener);
        ivSqrt2.setOnClickListener(onClickListener);
        ivPower2.setOnClickListener(onClickListener);
        //符号类型：左右括号, 逗号, 句号
        tvBracketsLeft.setOnClickListener(onClickListener);
        tvBracketsRight.setOnClickListener(onClickListener);
        tvComma.setOnClickListener(onClickListener);
        tvDecimalPoint.setOnClickListener(onClickListener);
        //运算符号：加, 减, 加减, 乘, 除, 等
        tvPlus.setOnClickListener(onClickListener);
        tvMinus.setOnClickListener(onClickListener);
        tvAddSubtract.setOnClickListener(onClickListener);
        tvMultiplication.setOnClickListener(onClickListener);
        tvDivision.setOnClickListener(onClickListener);
        tvEqual.setOnClickListener(onClickListener);
        tvNotEqual.setOnClickListener(onClickListener);
        //数字
        tvNum0.setOnClickListener(onClickListener);
        tvNum1.setOnClickListener(onClickListener);
        tvNum2.setOnClickListener(onClickListener);
        tvNum3.setOnClickListener(onClickListener);
        tvNum4.setOnClickListener(onClickListener);
        tvNum5.setOnClickListener(onClickListener);
        tvNum6.setOnClickListener(onClickListener);
        tvNum7.setOnClickListener(onClickListener);
        tvNum8.setOnClickListener(onClickListener);
        tvNum9.setOnClickListener(onClickListener);
        //删除按钮
        ivDelete1.setOnClickListener(onClickListener);
    }

    /**
     * 代数键盘
     */
    private void algebraKeyboard(Dialog dialog) {
        //运算符：小于，大于，小于等于，大于等于，不等于，
        tvLessThan = (BorderTextView) dialog.findViewById(R.id.tvLessThan);
        tvGreaterThan = (BorderTextView) dialog.findViewById(R.id.tvGreaterThan);
        tvLessThanAndEqual = (BorderTextView) dialog.findViewById(R.id.tvLessThanAndEqual);
        tvGreaterAndEqual = (BorderTextView) dialog.findViewById(R.id.tvGreaterAndEqual);
        tvNotEqual = (BorderTextView) dialog.findViewById(R.id.tvNotEqual);

        //符号类型：冒号，百分号，左中括号，右中括号
        tvColon = (BorderTextView) dialog.findViewById(R.id.tvColon);
        tvPercent = (BorderTextView) dialog.findViewById(R.id.tvPercent);
        tvMiddleBracketsLeft = (BorderTextView) dialog.findViewById(R.id.tvMiddleBracketsLeft);
        tvMiddleBracketsRight = (BorderTextView) dialog.findViewById(R.id.tvMiddleBracketsRight);

        //公式类型：幂次方，方程式组，三项方程组，二次开方，开根，矩阵，下标，2的幂次方，x的二次方，10的幂次方，分号, 和
        ivPower = (TextView) dialog.findViewById(R.id.ivPower);
        ivEquation = (TextView) dialog.findViewById(R.id.ivEquation);
        ivEquation_3 = (TextView) dialog.findViewById(R.id.ivEquation_3);
        ivSqrt2_algebra = (TextView) dialog.findViewById(R.id.ivSqrt2_algebra);
        ivSqrt = (TextView) dialog.findViewById(R.id.ivSqrt);
        ivAbsoluteValue = (TextView) dialog.findViewById(R.id.ivAbsoluteValue);
        ivSubScript = (TextView) dialog.findViewById(R.id.ivSubScript);
//        ivPower2_algebra = (TextView) dialog.findViewById(R.id.ivPower2_algebra);
        ivPowerX2 = (TextView) dialog.findViewById(R.id.ivPowerX2);
        ivPower10 = (TextView) dialog.findViewById(R.id.ivPower10);
        ivFraction_aglebra = (TextView) dialog.findViewById(R.id.ivFraction_algebra);
        tvSum = (TextView) dialog.findViewById(R.id.tvSum);

        //特殊符号：x_1, x_2,
        ivSubScriptX1 = (TextView) dialog.findViewById(R.id.ivSubScriptX1);
        ivSubScriptX2 = (TextView) dialog.findViewById(R.id.ivSubScriptX2);

        //因为，所以，乘积
        tvBecause = (TextView) dialog.findViewById(R.id.tvBecause);
        tvTherefore = (TextView) dialog.findViewById(R.id.tvTherefore);
        tvProduct = (TextView) dialog.findViewById(R.id.tvProduct);


        //删除按钮
        ivDelete2 = (TextView) dialog.findViewById(R.id.ivDelete2);


        //运算符：小于，大于，小于等于，大于等于，不等于，
        tvLessThan.setOnClickListener(onClickListener);
        tvGreaterThan.setOnClickListener(onClickListener);
        tvLessThanAndEqual.setOnClickListener(onClickListener);
        tvGreaterAndEqual.setOnClickListener(onClickListener);
        tvNotEqual.setOnClickListener(onClickListener);

        //符号类型：冒号，百分号，左中括号，右中括号
        tvColon.setOnClickListener(onClickListener);
        tvPercent.setOnClickListener(onClickListener);
        tvMiddleBracketsLeft.setOnClickListener(onClickListener);
        tvMiddleBracketsRight.setOnClickListener(onClickListener);

        //公式类型：幂次方，方程式组，二次开方，开根，矩阵，下标，2的幂次方，x的二次方，10的幂次方，分号
        ivPower.setOnClickListener(onClickListener);
        ivEquation.setOnClickListener(onClickListener);
        ivEquation_3.setOnClickListener(onClickListener);
        ivSqrt2_algebra.setOnClickListener(onClickListener);
        ivSqrt.setOnClickListener(onClickListener);
        ivAbsoluteValue.setOnClickListener(onClickListener);
        ivSubScript.setOnClickListener(onClickListener);
//        ivPower2_algebra.setOnClickListener(onClickListener);
        ivPowerX2.setOnClickListener(onClickListener);
        ivPower10.setOnClickListener(onClickListener);
        ivFraction_aglebra.setOnClickListener(onClickListener);
        tvSum.setOnClickListener(onClickListener);

        //特殊符号：x_1, x_2,
        ivSubScriptX1.setOnClickListener(onClickListener);
        ivSubScriptX2.setOnClickListener(onClickListener);

        //因为，所以，乘积
        tvBecause.setOnClickListener(onClickListener);
        tvTherefore.setOnClickListener(onClickListener);
        tvProduct.setOnClickListener(onClickListener);

        //删除按钮
        ivDelete2.setOnClickListener(onClickListener);
    }

    /**
     * 几何键盘
     */
    private void geometryKeyboard(Dialog dialog) {
        //公式：sin, cos, tan
        tvSine = (TextView) dialog.findViewById(R.id.tvSine);
        tvCosine = (TextView) dialog.findViewById(R.id.tvCosine);
        tvTangent = (TextView) dialog.findViewById(R.id.tvTangent);
        //特殊字符：角度, 三角朝上, 全等于，相似于，约等于，垂直，--
        tvAngle = (TextView) dialog.findViewById(R.id.tvAngle);
        tvTriangle = (TextView) dialog.findViewById(R.id.tvTriangle);
        tvCong = (TextView) dialog.findViewById(R.id.tvCong);
        tvSim = (TextView) dialog.findViewById(R.id.tvSim);
        tvApprox = (TextView) dialog.findViewById(R.id.tvApprox);
        tvPerp = (TextView) dialog.findViewById(R.id.tvPerp);
        tvDoubleLine = (TextView) dialog.findViewById(R.id.tvDoubleLine);
        //特殊字符：度数, 分钟, 秒
        ivCirc = (TextView) dialog.findViewById(R.id.ivCirc);
        ivMinute = (TextView) dialog.findViewById(R.id.ivMinute);
        ivSecond = (TextView) dialog.findViewById(R.id.ivSecond);
        //常用符号：π， πr²
        tvPi = (TextView) dialog.findViewById(R.id.tvPi);
        tvPiR2 = (TextView) dialog.findViewById(R.id.tvPiR2);
        //希腊字母
        tvAlpha = (TextView) dialog.findViewById(R.id.tvAlpha);
        tvBeta = (TextView) dialog.findViewById(R.id.tvBeta);
        tvTheta = (TextView) dialog.findViewById(R.id.tvTheta);
        tvDelta = (TextView) dialog.findViewById(R.id.tvDelta);

        //圆
        tvCircle = (TextView) dialog.findViewById(R.id.tvCircle);
        //弧
        tvArc = (TextView) dialog.findViewById(R.id.tvArc);

        //删除按钮
        ivDelete3 = (TextView) dialog.findViewById(R.id.ivDelete3);


        //公式：sin, cos, tan
        tvSine.setOnClickListener(onClickListener);
        tvCosine.setOnClickListener(onClickListener);
        tvTangent.setOnClickListener(onClickListener);
        //特殊字符：角度, 三角朝上, 全等于，相似于，约等于，垂直，--
        tvAngle.setOnClickListener(onClickListener);
        tvTriangle.setOnClickListener(onClickListener);
        tvCong.setOnClickListener(onClickListener);
        tvSim.setOnClickListener(onClickListener);
        tvApprox.setOnClickListener(onClickListener);
        tvPerp.setOnClickListener(onClickListener);
        tvDoubleLine.setOnClickListener(onClickListener);
        //特殊字符：--, --, --
        ivCirc.setOnClickListener(onClickListener);
        ivMinute.setOnClickListener(onClickListener);
        ivSecond.setOnClickListener(onClickListener);
        //常用符号：π， πr²
        tvPi.setOnClickListener(onClickListener);
        tvPiR2.setOnClickListener(onClickListener);
        //希腊字母
        tvAlpha.setOnClickListener(onClickListener);
        tvBeta.setOnClickListener(onClickListener);
        tvTheta.setOnClickListener(onClickListener);
        tvDelta.setOnClickListener(onClickListener);
        //圆
        tvCircle.setOnClickListener(onClickListener);
        //弧
        tvArc.setOnClickListener(onClickListener);
        //删除按钮
        ivDelete3.setOnClickListener(onClickListener);
    }

    /**
     * 字母键盘
     */
    private void latterKeyboard(Dialog dialog) {
        btnBack = (Button) dialog.findViewById(R.id.tvBack);
        btnLowerUpperCase = (Button) dialog.findViewById(R.id.tvLowerUpperCase);
        btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
        btnComma = (Button) dialog.findViewById(R.id.btnComma);
        btnDivision = (Button) dialog.findViewById(R.id.btnDivision);
        btnPlus = (Button) dialog.findViewById(R.id.btnPlus);
        btnMinus = (Button) dialog.findViewById(R.id.btnMinus);
        btnMultiplication = (Button) dialog.findViewById(R.id.btnMultiplication);
        btnEqual = (Button) dialog.findViewById(R.id.btnEqual);

        btnLatterA = (Button) dialog.findViewById(R.id.btnLatterA);
        btnLatterB = (Button) dialog.findViewById(R.id.btnLatterB);
        btnLatterC = (Button) dialog.findViewById(R.id.btnLatterC);
        btnLatterD = (Button) dialog.findViewById(R.id.btnLatterD);
        btnLatterE = (Button) dialog.findViewById(R.id.btnLatterE);
        btnLatterF = (Button) dialog.findViewById(R.id.btnLatterF);
        btnLatterG = (Button) dialog.findViewById(R.id.btnLatterG);

        btnLatterH = (Button) dialog.findViewById(R.id.btnLatterH);
        btnLatterI = (Button) dialog.findViewById(R.id.btnLatterI);
        btnLatterJ = (Button) dialog.findViewById(R.id.btnLatterJ);
        btnLatterK = (Button) dialog.findViewById(R.id.btnLatterK);
        btnLatterL = (Button) dialog.findViewById(R.id.btnLatterL);
        btnLatterM = (Button) dialog.findViewById(R.id.btnLatterM);
        btnLatterN = (Button) dialog.findViewById(R.id.btnLatterN);

        btnLatterO = (Button) dialog.findViewById(R.id.btnLatterO);
        btnLatterP = (Button) dialog.findViewById(R.id.btnLatterP);
        btnLatterQ = (Button) dialog.findViewById(R.id.btnLatterQ);
        btnLatterR = (Button) dialog.findViewById(R.id.btnLatterR);
        btnLatterS = (Button) dialog.findViewById(R.id.btnLatterS);
        btnLatterT = (Button) dialog.findViewById(R.id.btnLatterT);

        btnLatterU = (Button) dialog.findViewById(R.id.btnLatterU);
        btnLatterV = (Button) dialog.findViewById(R.id.btnLatterV);
        btnLatterW = (Button) dialog.findViewById(R.id.btnLatterW);
        btnLatterX = (Button) dialog.findViewById(R.id.btnLatterX);
        btnLatterY = (Button) dialog.findViewById(R.id.btnLatterY);
        btnLatterZ = (Button) dialog.findViewById(R.id.btnLatterZ);

        btnBack.setOnClickListener(onClickListener);
        btnLowerUpperCase.setOnClickListener(onClickListener);
        btnDelete.setOnClickListener(onClickListener);
        btnComma.setOnClickListener(onClickListener);
        btnDivision.setOnClickListener(onClickListener);
        btnPlus.setOnClickListener(onClickListener);
        btnMinus.setOnClickListener(onClickListener);
        btnMultiplication.setOnClickListener(onClickListener);
        btnEqual.setOnClickListener(onClickListener);

        btnLatterA.setOnClickListener(onClickListener);
        btnLatterB.setOnClickListener(onClickListener);
        btnLatterC.setOnClickListener(onClickListener);
        btnLatterD.setOnClickListener(onClickListener);
        btnLatterE.setOnClickListener(onClickListener);
        btnLatterF.setOnClickListener(onClickListener);
        btnLatterG.setOnClickListener(onClickListener);

        btnLatterH.setOnClickListener(onClickListener);
        btnLatterI.setOnClickListener(onClickListener);
        btnLatterJ.setOnClickListener(onClickListener);
        btnLatterK.setOnClickListener(onClickListener);
        btnLatterL.setOnClickListener(onClickListener);
        btnLatterM.setOnClickListener(onClickListener);
        btnLatterN.setOnClickListener(onClickListener);

        btnLatterO.setOnClickListener(onClickListener);
        btnLatterP.setOnClickListener(onClickListener);
        btnLatterQ.setOnClickListener(onClickListener);
        btnLatterR.setOnClickListener(onClickListener);
        btnLatterS.setOnClickListener(onClickListener);
        btnLatterT.setOnClickListener(onClickListener);

        btnLatterU.setOnClickListener(onClickListener);
        btnLatterV.setOnClickListener(onClickListener);
        btnLatterW.setOnClickListener(onClickListener);
        btnLatterX.setOnClickListener(onClickListener);
        btnLatterY.setOnClickListener(onClickListener);
        btnLatterZ.setOnClickListener(onClickListener);

    }

    private void initList() {
        latterViewList.add(btnLatterA);
        latterViewList.add(btnLatterB);
        latterViewList.add(btnLatterC);
        latterViewList.add(btnLatterD);
        latterViewList.add(btnLatterE);
        latterViewList.add(btnLatterF);
        latterViewList.add(btnLatterG);

        latterViewList.add(btnLatterH);
        latterViewList.add(btnLatterI);
        latterViewList.add(btnLatterJ);
        latterViewList.add(btnLatterK);
        latterViewList.add(btnLatterL);
        latterViewList.add(btnLatterM);
        latterViewList.add(btnLatterN);

        latterViewList.add(btnLatterO);
        latterViewList.add(btnLatterP);
        latterViewList.add(btnLatterQ);
        latterViewList.add(btnLatterR);
        latterViewList.add(btnLatterS);
        latterViewList.add(btnLatterT);

        latterViewList.add(btnLatterU);
        latterViewList.add(btnLatterV);
        latterViewList.add(btnLatterW);
        latterViewList.add(btnLatterX);
        latterViewList.add(btnLatterY);
        latterViewList.add(btnLatterZ);
    }

    private void initLatex() {
        LateXConfig.getInstance().setEditRoot(evLatexView, hsvLatex);
        LateXConfig.getInstance().setConfirmView(tvConfirm);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tvConfirm) {
                if (tvConfirm.getText().toString().equals("关闭")) {
                    KeyboardFragment.this.dismiss();
                } else {
                    ConvertResult convertResult = ViewAssembleManager.getInstance().toLatexString();
                    if (convertResult.isSuccess) {
                        Log.d(TAG, "导出公式为：" + convertResult.message);
                        String content = "$$" + convertResult.message + "$$";
                        if (tvOutSide != null) {
                            tvOutSide.setTag(content);
                            content = LatexUtil.analysisLatex(KeyboardFragment.this.getContext(), content, 0);
                            RichText.fromHtml(content).
                                    with(KeyboardFragment.this.getContext()).
                                    into(tvOutSide, null);
                        }

                        KeyboardFragment.this.dismiss();

//                        TestQuestionManager.getInstance().skipNextItem();
                    } else {
                        Toast.makeText(KeyboardFragment.this.getContext(), "导出失败", Toast.LENGTH_SHORT);
                    }
                }
            } else if (id == R.id.tvDefault) {
                llMathKeyBoardLayout.setVisibility(View.VISIBLE);
                llDefaultLayout.setVisibility(View.VISIBLE);
                tlAlgebraLayout.setVisibility(View.GONE);
                llGeometryLayout.setVisibility(View.GONE);
                llLatterKeyBoardLayout.setVisibility(View.GONE);

                if (!v.isSelected()) {
                    tvDefault.setSelected(true);
                }
                tvAlgebra.setSelected(false);
                tvGeometry.setSelected(false);
                tvLetter.setSelected(false);
            } else if (id == R.id.tvAlgebra) {
                llDefaultLayout.setVisibility(View.GONE);
                tlAlgebraLayout.setVisibility(View.VISIBLE);
                llGeometryLayout.setVisibility(View.GONE);
                llLatterKeyBoardLayout.setVisibility(View.GONE);
                if (!v.isSelected()) {
                    tvAlgebra.setSelected(true);
                }
                tvDefault.setSelected(false);
                tvGeometry.setSelected(false);
                tvLetter.setSelected(false);
            } else if (id == R.id.tvGeometry) {
                llDefaultLayout.setVisibility(View.GONE);
                tlAlgebraLayout.setVisibility(View.GONE);
                llGeometryLayout.setVisibility(View.VISIBLE);
                llLatterKeyBoardLayout.setVisibility(View.GONE);

                if (!v.isSelected()) {
                    tvGeometry.setSelected(true);
                }
                tvDefault.setSelected(false);
                tvAlgebra.setSelected(false);
                tvLetter.setSelected(false);
            } else if (id == R.id.tvLetter) {
                llMathKeyBoardLayout.setVisibility(View.GONE);
                llLatterKeyBoardLayout.setVisibility(View.VISIBLE);

                if (!v.isSelected()) {
                    tvLetter.setSelected(true);
                }
            }

//分数
            else if (id == R.id.ivFraction || id == R.id.ivFraction_algebra) {
                addMathFormula(MathFormula.Fraction);
            }
//开根号
            else if (id == R.id.ivSqrt) {
                addMathFormula(MathFormula.Sqrt);
            } else if (id == R.id.ivSqrt2 || id == R.id.ivSqrt2_algebra) {
                addMathFormula(MathFormula.Sqrt2);
            }
//幂次方
            else if (id == R.id.ivPower) {
                addMathFormula(MathFormula.Power);
            } else if (id == R.id.ivPower2 /*|| id == R.id.ivPower2_algebra*/) {
                addMathFormula(MathFormula.Power2);
            } else if (id == R.id.ivPowerX2) {
                addMathFormula(MathFormula.PowerX2);
            }
//方程式组
            else if (id == R.id.ivEquation) {
                addMathFormula(MathFormula.Equation);
            } else if (id == R.id.ivEquation_3) {
                addMathFormula(MathFormula.Equation_3);
            }
//绝对值
            else if (id == R.id.ivAbsoluteValue) {
                addMathFormula(MathFormula.AbsoluteValue);
            }
//下标
            else if (id == R.id.ivSubScript) {
                addMathFormula(MathFormula.SubScript);
            }
//10的幂次方
            else if (id == R.id.ivPower10) {
                addMathFormula(MathFormula.Power10);
            }
//求和
            else if (id == R.id.tvSum) {
                addMathFormula(MathFormula.Sum);
            }

//因为
            else if (id == R.id.tvBecause) {
                addCharacter(LatexConstant.Because);
            }
//所以
            else if (id == R.id.tvTherefore) {
                addCharacter(LatexConstant.Therefore);
            }
//乘积
            else if (id == R.id.tvProduct) {
                addCharacter(LatexConstant.Product);
            }


//特殊公式符号：度数， 分 ， 秒
            else {
                if (id == R.id.ivCirc) {
                    addMathFormula(MathFormula.Circ);
                } else if (id == R.id.ivMinute) {
                    addMathFormula(MathFormula.Minute);
                } else if (id == R.id.ivSecond) {
                    addMathFormula(MathFormula.Second);
                }
//数字，字母
                else if (id == R.id.tvNum0) {
                    addCharacter(LatexConstant.Num_0);
                } else if (id == R.id.tvNum1) {
                    addCharacter(LatexConstant.Num_1);
                } else if (id == R.id.tvNum2) {
                    addCharacter(LatexConstant.Num_2);
                } else if (id == R.id.tvNum3) {
                    addCharacter(LatexConstant.Num_3);
                } else if (id == R.id.tvNum4) {
                    addCharacter(LatexConstant.Num_4);
                } else if (id == R.id.tvNum5) {
                    addCharacter(LatexConstant.Num_5);
                } else if (id == R.id.tvNum6) {
                    addCharacter(LatexConstant.Num_6);
                } else if (id == R.id.tvNum7) {
                    addCharacter(LatexConstant.Num_7);
                } else if (id == R.id.tvNum8) {
                    addCharacter(LatexConstant.Num_8);
                } else if (id == R.id.tvNum9) {
                    addCharacter(LatexConstant.Num_9);
                } else if (id == R.id.btnLatterA || id == R.id.btnLatterB || id == R.id.btnLatterC || id == R.id.btnLatterD ||
                        id == R.id.btnLatterE || id == R.id.btnLatterF || id == R.id.btnLatterG ||
                        id == R.id.btnLatterH || id == R.id.btnLatterI || id == R.id.btnLatterJ || id == R.id.btnLatterK ||
                        id == R.id.btnLatterL || id == R.id.btnLatterM || id == R.id.btnLatterN ||
                        id == R.id.btnLatterO || id == R.id.btnLatterP || id == R.id.btnLatterQ ||
                        id == R.id.btnLatterR || id == R.id.btnLatterS || id == R.id.btnLatterT ||
                        id == R.id.btnLatterU || id == R.id.btnLatterV || id == R.id.btnLatterW ||
                        id == R.id.btnLatterX || id == R.id.btnLatterY || id == R.id.btnLatterZ) {
                    if (v instanceof Button) {
                        addCharacter(((Button) v).getText().toString().trim());
                    } else {
                        Log.e(TAG, "类型转换错误");
                    }
                }
//特殊符号：sin, cos, tan, X，x_1, x_2, π， πr²
                else if (id == R.id.tvSine) {
                    addMathFormula(MathFormula.Sine);
                } else if (id == R.id.tvCosine) {
                    addMathFormula(MathFormula.Cosine);
                } else if (id == R.id.tvTangent) {
                    addMathFormula(MathFormula.Tangent);
                } else if (id == R.id.ivSubScriptX1) {
                    addMathFormula(MathFormula.SubScriptX1);
                } else if (id == R.id.ivSubScriptX2) {
                    addMathFormula(MathFormula.SubScriptX2);
                } else if (id == R.id.tvPi) {
                    addCharacter(LatexConstant.PI);
                } else if (id == R.id.tvPiR2) {
                    addMathFormula(MathFormula.PIR2);
                }
//特殊符号：圈1, 2, 3, 4, 5，或，且, 希腊字母
                else if (id == R.id.tvAlpha) {
                    addCharacter(LatexConstant.ALPHA);
                } else if (id == R.id.tvBeta) {
                    addCharacter(LatexConstant.BETA);
                } else if (id == R.id.tvTheta) {
                    addCharacter(LatexConstant.THETA);
                } else if (id == R.id.tvDelta) {
                    addCharacter(LatexConstant.DELTA);
                }
//圆
                else if (id == R.id.tvCircle) {
                    addCharacter(LatexConstant.Circle);
                }
                //弧
                else if (id == R.id.tvArc) {
                    addMathFormula(MathFormula.Arc);
                }


//加，减，加减，乘，除，等于
                else {
                    if (id == R.id.tvPlus || id == R.id.btnPlus) {
                        addCharacter(LatexConstant.PLUS);
                    } else if (id == R.id.tvMinus || id == R.id.btnMinus) {
                        addCharacter(LatexConstant.MINUS);
                    } else if (id == R.id.tvAddSubtract) {
                        addCharacter(LatexConstant.AddSubtract);
                    } else if (id == R.id.tvMultiplication || id == R.id.btnMultiplication) {
                        addCharacter(LatexConstant.MULTIPLICATION);
                    } else if (id == R.id.tvDivision || id == R.id.btnDivision) {
                        addCharacter(LatexConstant.DIVISION);
                    } else if (id == R.id.tvEqual || id == R.id.btnEqual) {
                        addCharacter(LatexConstant.EQUAL);
                    }
//小于，大于，小于等于，大于等于，不等于
                    else if (id == R.id.tvLessThan) {
                        addCharacter(LatexConstant.Less_Than);
                    } else if (id == R.id.tvGreaterThan) {
                        addCharacter(LatexConstant.Greater_Than);
                    } else if (id == R.id.tvLessThanAndEqual) {
                        addCharacter(LatexConstant.Less_Than_And_Equal);
                    } else if (id == R.id.tvGreaterAndEqual) {
                        addCharacter(LatexConstant.Greater_Than_And_Equal);
                    } else if (id == R.id.tvNotEqual) {
                        addCharacter(LatexConstant.Not_Equal);
                    }
//百分号 %, 角度, 三角朝上, 全等于，相似于，约等于，垂直，双竖线
                    else if (id == R.id.tvPercent) {
                        addCharacter(LatexConstant.PERCENT);
                    } else if (id == R.id.tvAngle) {
                        addMathFormula(MathFormula.Angle);
                    } else if (id == R.id.tvTriangle) {
                        addCharacter(LatexConstant.Triangle);
                    } else if (id == R.id.tvCong) {
                        addCharacter(LatexConstant.Cong);
                    } else if (id == R.id.tvSim) {
                        addCharacter(LatexConstant.Sim);
                    } else if (id == R.id.tvApprox) {
                        addCharacter(LatexConstant.Approx);
                    } else if (id == R.id.tvPerp) {
                        addCharacter(LatexConstant.Perp);
                    } else if (id == R.id.tvDoubleLine) {
                        addCharacter(LatexConstant.DoubleLine);
                    }
//括号：左右小括号，左右中括号
                    else if (id == R.id.tvBracketsLeft) {       //括号
                        addCharacter(LatexConstant.Parenthesis_Left);
                    } else if (id == R.id.tvBracketsRight) {
                        addCharacter(LatexConstant.Parenthesis_Right);
                    } else if (id == R.id.tvMiddleBracketsLeft) {
                        addCharacter(LatexConstant.Bracket_Left);
                    } else if (id == R.id.tvMiddleBracketsRight) {
                        addCharacter(LatexConstant.Bracket_Right);
                    }
//逗号，句号，冒号
                    else if (id == R.id.tvComma || id == R.id.btnComma) {
                        addCharacter(LatexConstant.COMMA);
                    } else if (id == R.id.tvDecimalPoint) {
                        addCharacter(LatexConstant.DECIMAL_POINT);
                    } else if (id == R.id.tvColon) {
                        addCharacter(LatexConstant.Colon);
                    }
//字母页面特殊按钮：返回，大小写
                    else if (id == R.id.tvBack) {
                        llLatterKeyBoardLayout.setVisibility(View.GONE);
                        tvDefault.performClick();
                    } else if (id == R.id.tvLowerUpperCase) {
                        changeLatterKeyboardUpperOrLower();
                    }
//删除按钮
                    else if (id == R.id.ivDelete1 ||
                            id == R.id.ivDelete2 ||
                            id == R.id.ivDelete3 ||
                            id == R.id.btnDelete) {
                        Assembles.getInstants().deleteMathFormula(getContext());
                    }
                }
            }
        }
    };

    private void changeLatterKeyboardUpperOrLower() {
        if (toUpperKeyboard) { //变为大写
            toUpperKeyboard = false;
            for (int i = 0; i < latterViewList.size(); i++) {
                Button button = latterViewList.get(i);
                button.setText(upperLatter[i]);
            }
            btnLowerUpperCase.setText("小写");
        } else {//变为小写
            toUpperKeyboard = true;
            for (int i = 0; i < latterViewList.size(); i++) {
                Button button = latterViewList.get(i);
                button.setText(lowerLatter[i]);
            }
            btnLowerUpperCase.setText("大写");
        }
    }

    public void setOutSide(TextView outSide) {
        this.tvOutSide = outSide;
    }

    public void setOriginContent(String content) {
        this.originContent = content;
    }

    private void addMathFormula(MathFormula mathFormula) {
        Assembles.getInstants().addMathFormula(getContext(), mathFormula);
    }

    private void addCharacter(String content) {
        Assembles.getInstants().addSimpleSymbol(getContext(), content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LateXConfig.getInstance().setHsvLatex(null);
        LateXConfig.getInstance().setRl_root(null);
        LateXConfig.getInstance().setConfirmView(null);
        ViewAssembleManager.getInstance().release();
    }
}

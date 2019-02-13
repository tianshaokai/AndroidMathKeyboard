package com.tianshaokai.mathkeyboard.manager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.tianshaokai.mathkeyboard.widget.EditView;
import com.tianshaokai.mathkeyboard.widget.FormulaView;
import com.tianshaokai.mathkeyboard.widget.LineView;
import com.tianshaokai.mathkeyboard.widget.SimpleSymbolView;

import java.util.HashMap;
import java.util.Map;


/**
 * 组装UI管理器
 * 特殊说明：
 * 1. 选中状态和光标互斥，只能存一
 */

public class ViewAssembleManager {
    private static final String TAG = "ViewAssembleManager";
    private static ViewAssembleManager instance;

    private ViewAssembleManager() {

    }

    public static ViewAssembleManager getInstance() {
        synchronized (ViewAssembleManager.class) {
            if (instance == null) {
                instance = new ViewAssembleManager();
            }
        }

        return instance;
    }

    public ViewGroup getRootView() {
        return LateXConfig.getInstance().getEditRoot();
    }

    public Context getContext() {
        return LateXConfig.getInstance().getContext();
    }


    private Map<String, FormulaView> formulaViews = new HashMap<>();
    private SelectedStruct selectedStruct = null;

    public SelectedStruct getSelectedStruct() {
        return this.selectedStruct;
    }

    //获取被选中元素所在等级，
    //特殊处理：所在等级-1，为了添加子元素的正确性
    public int getSelectedViewLevel() {
        if (selectedStruct == null) {
            Log.e(TAG ,"selected struct为null，为了保证程序不崩溃，则添加到根节点");
            return 1;
        }

        String pLatexViewName = selectedStruct.getSelectedLatexView();
        String pClickViewName = selectedStruct.getSelectedClickView();
        int index = selectedStruct.getIndex();

        if (pLatexViewName != null && !pLatexViewName.isEmpty()
                && pClickViewName != null && !pClickViewName.isEmpty()) {
            if (formulaViews.containsKey(selectedStruct.getSelectedLatexView())) {
                FormulaView formulaView = formulaViews.get(selectedStruct.getSelectedLatexView());
                //没有光标情况下，根据选中元素，返回相应等级
                if (index == -1) {
                    //在根布局被选中情况下，则查找其父布局，将新公式添加后该公式后面
                    if (formulaView.isRootSelected()) {
                        //如果根布局被选中，则添加为同级
                        return formulaView.getLevel();
                    } else {
                        //如果选中的不是根布局，则添加到下一级
                        return formulaView.getLevel() + 1;
                    }
                } else {
                    //子元素内包含光标，则添加到下一级
                    return formulaView.getLevel() + 1;
                }
            } else {
                Log.e(TAG ,"没找到对应的公式：" + pLatexViewName + "， 为保证程序不崩溃，添加到根节点");
                return 1;
            }
        } else {
            //在latexView, clickview为空情况下，只可能存在根节点
            return 1;
        }
    }

    //获取被选中元素所在等级，
    //特殊处理：所在等级-1，为了添加子元素的正确性
    public int getSelectedViewLevelForSymbol() {
        if (selectedStruct == null) {
            Log.e(TAG ,"selected struct为null，为了保证程序不崩溃，则添加到根节点");
            return 1;
        }

        String pLatexViewName = selectedStruct.getSelectedLatexView();
        String pClickViewName = selectedStruct.getSelectedClickView();
        int index = selectedStruct.getIndex();

        if (pLatexViewName != null && !pLatexViewName.isEmpty()
                && pClickViewName != null && !pClickViewName.isEmpty()) {
            if (formulaViews.containsKey(selectedStruct.getSelectedLatexView())) {
                FormulaView formulaView = formulaViews.get(selectedStruct.getSelectedLatexView());
                //没有光标情况下，根据选中元素，返回相应等级
                if (index == -1) {
                    //在根布局被选中情况下，则查找其父布局，将新公式添加后该公式后面
                    if (formulaView.isRootSelected()) {
                        //如果根布局被选中，则添加为同级
                        return formulaView.getLevel();
                    } else {
                        if (isSpecial(formulaView, pClickViewName)) {
                            return formulaView.getLevel();
                        } else {
                            //如果选中的不是根布局，则添加到下一级
                            return formulaView.getLevel() + 1;
                        }
                    }
                } else {
                    if (isSpecial(formulaView, pClickViewName)) {
                        return formulaView.getLevel();
                    } else {
                        //子元素内包含光标，则添加到下一级
                        return formulaView.getLevel() + 1;
                    }
                }
            } else {
                Log.e(TAG ,"没找到对应的公式：" + pLatexViewName + "， 为保证程序不崩溃，添加到根节点");
                return 1;
            }
        } else {
            //在latexView, clickview为空情况下，只可能存在根节点
            return 1;
        }
    }

    private boolean isSpecial(FormulaView formulaView, String clickView) {
        if (formulaView.isSpecial(clickView)) {
            return true;
        }
        return false;
    }

    //添加光标，添加为编辑框尾部
    public void addRootLine() {
        //首先删除存在的光标，然后添加根节点尾部光标
        deleteLine();

        LineView subViewGroup = new LineView(getContext(), 1);
        formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
        ViewGroup viewGroup = getRootView();
        if (viewGroup != null) {
            viewGroup.addView(subViewGroup);
        }

        selectedStruct = new SelectedStruct();
        selectedStruct.setSelectedLatexView(null);
        selectedStruct.setSelectedClickView(null);
        selectedStruct.setRootSelected(false);
        if(null!=getRootView()){
            selectedStruct.setIndex(getRootView().getChildCount() - 1);
        }
    }

    //在指定index位置插入光标
    public void addLine(String latexView, String clickView, int index, int level) {
        //删除已有光标
        deleteLine();

        //设置所有公式为非选中状态
        for (Map.Entry<String, FormulaView> entry : formulaViews.entrySet()) {
            entry.getValue().clearBackground();
        }

        selectedStruct = new SelectedStruct();
        selectedStruct.setSelectedLatexView(latexView);
        selectedStruct.setSelectedClickView(clickView);
        selectedStruct.setRootSelected(false);

        //如果没有父节点，则插入在根节点中
        if (latexView == null) {
            //在对应位置上添加竖线，
            LineView subViewGroup = new LineView(getContext(), 1);
            formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
            if (index == -1) {
                getRootView().addView(subViewGroup);
                if(null!=getRootView())
                {
                    selectedStruct.setIndex(getRootView().getChildCount() - 1);
                }

            } else {
                getRootView().addView(subViewGroup, index);
                selectedStruct.setIndex(index);
            }
        } else {
            FormulaView formulaView = formulaViews.get(latexView);
            if (formulaView != null) {
                LineView subViewGroup = new LineView(getContext(), level);
                formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
                formulaView.addChildView(clickView, index, subViewGroup);
                selectedStruct.setIndex(formulaView.getChildViewIndex(clickView, subViewGroup.getLatexView()));
            } else {
                Log.e(TAG ,"不能找到对应的公式：" + latexView);
            }
        }
    }

    //点击symbol时，添加竖线
    public void addLine(String pLatexView, String pClickView, String clickView, int level, boolean isFront) {
        //删除已有光标
        deleteLine();

        //设置所有公式为非选中状态
        for (Map.Entry<String, FormulaView> entry : formulaViews.entrySet()) {
            entry.getValue().clearBackground();
        }

        //如果没有父节点，则插入在根节点中
        if (pLatexView == null) {
            if(getRootView()!=null){
                for (int i = 0; i < getRootView().getChildCount(); ++i) {
                    View view = getRootView().getChildAt(i);
                    if (view instanceof SimpleSymbolView) {
                        SimpleSymbolView symbolView = (SimpleSymbolView) view;
                        if (symbolView.getSymbolName().equals(clickView)) {
                            //在对应位置上添加竖线，
                            LineView subViewGroup = new LineView(getContext(), 1);
                            formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
                            if (isFront) {
                                getRootView().addView(subViewGroup, i);
                            } else {
                                getRootView().addView(subViewGroup, i + 1);
                            }

                            selectedStruct = new SelectedStruct();
                            selectedStruct.setSelectedLatexView(null);
                            selectedStruct.setSelectedClickView(null);
                            selectedStruct.setRootSelected(false);
                            if (isFront) {
                                selectedStruct.setIndex(i);
                            } else {
                                selectedStruct.setIndex(i + 1);
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            FormulaView pFormulaView = formulaViews.get(pLatexView);
            if (pFormulaView != null) {
                LineView subViewGroup = new LineView(getContext(), level);
                formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
                //获取symbol所在位置
                int index = pFormulaView.getChildViewIndex(pClickView, clickView);
                if (isFront) {
                    pFormulaView.addChildView(pClickView, index, subViewGroup);
                } else {
                    pFormulaView.addChildView(pClickView, index + 1, subViewGroup);
                }

                selectedStruct = new SelectedStruct();
                selectedStruct.setSelectedLatexView(pLatexView);
                selectedStruct.setSelectedClickView(pClickView);
                selectedStruct.setRootSelected(false);
                if (isFront) {
                    selectedStruct.setIndex(index);
                } else {
                    selectedStruct.setIndex(index + 1);
                }
            } else {
                Log.e(TAG ,"不能找到对应的公式：" + pLatexView);
            }
        }
    }

    //删除已存在的竖线
    private void deleteLine() {
        //如果已经存在，则删除
        if (formulaViews.containsKey(LineView.class.getSimpleName())) {
            FormulaView formulaView = formulaViews.remove(LineView.class.getSimpleName());
            if (formulaView.getParentFormulaViewName() != null) {
                FormulaView parentFormulaView = formulaViews.get(formulaView.getParentFormulaViewName());
                if (parentFormulaView != null) {
                    int index = parentFormulaView.getChildViewIndex(formulaView.getParentClickViewName(), formulaView.getLatexView());
                    parentFormulaView.removeChildView(formulaView.getParentClickViewName(), index);
                } else {
                    Log.e(TAG ,"没有找到相应的公式：" + formulaView.getParentFormulaViewName());
                }
            } else {
                if(getRootView()!=null){
                    getRootView().removeView(formulaView);
                }
            }
        }
        selectedStruct = null;
    }

    public void updateSelectedStructIndex(int index) {
        selectedStruct.setIndex(index);
    }

    //添加可编辑公式
    public void addMathFormula(FormulaView subViewGroup, SelectedStruct selectedStruct) {
        if (subViewGroup == null) {
            Log.e(TAG ,"subViewGroup 为 null");
            return;
        }

        if (selectedStruct == null) {
            Log.e(TAG ,"selected struct为null，请检查代码");
            return;
        }

        String sLatexViewName = selectedStruct.getSelectedLatexView();
        String sClickViewName = selectedStruct.getSelectedClickView();
        int sIndex = selectedStruct.getIndex();
        boolean isRootSelected = selectedStruct.isRootSelected();

        if (sLatexViewName != null && !sLatexViewName.isEmpty()
                && sClickViewName != null && !sClickViewName.isEmpty()) {
            if (formulaViews.containsKey(sLatexViewName)) {
                FormulaView formulaView = formulaViews.get(sLatexViewName);
                //在没有光标情况下
                if (sIndex == -1) {
                    //在根布局被选中情况下，则查找其父布局，将新公式添加后该公式后面
                    if (isRootSelected) {
                        String pLatexName = formulaView.getParentFormulaViewName();
                        String pClickName = formulaView.getParentClickViewName();
                        if (pLatexName == null) {
                            if(getRootView()!=null){
                                for (int i = 0; i < getRootView().getChildCount(); ++i) {
                                    View view = getRootView().getChildAt(i);
                                    if (view.equals(formulaView)) {
                                        getRootView().addView(subViewGroup, i + 1);
                                        break;
                                    }
                                }
                            }
                        } else {
                            FormulaView pFormulaView = formulaViews.get(pLatexName);
                            if (pFormulaView != null) {
                                int index = pFormulaView.getChildViewIndex(pClickName, formulaView.getLatexView());
                                pFormulaView.addChildView(pClickName, index + 1, subViewGroup);
                            } else {
                                Log.e(TAG ,"有选中数据，在整体布局中没有，请查找代码问题");
                            }
                        }
                    } else {
                        //在某元素被选中状态时，插入公式类型，则直接插入
                        formulaView.addChildView(sClickViewName, -1, subViewGroup);
                    }
                } else {
                    //存在光标
                    formulaView.addChildView(sClickViewName, sIndex, subViewGroup);
                }
            } else {
                Log.e(TAG ,"没有找到对应公式：" + sLatexViewName);
            }
        } else {
            //在根节点的光标处，添加公式
            if (sIndex != -1) {
                //添加公式后，公式某元素必为选中状态，光标消失
                ViewGroup viewGroup = getRootView();
                if (viewGroup != null) {
                    viewGroup.addView(subViewGroup, sIndex);
                }
                changeDoneViewState(true);
            } else {
                Log.e(TAG ,"在没有选中元素的情况下，必然会有光标，请检查代码");
            }
        }

        formulaViews.put(subViewGroup.getLatexView(), subViewGroup);
        moveCoordinate();
    }

    //添加简单符号
    public void addSimpleSymbol(SimpleSymbolView symbolView) {
        if (symbolView == null) {
            Log.e(TAG ,"symbolView 为 null");
            return;
        }

        if (selectedStruct == null) {
            Log.e(TAG ,"select struct为null，请检查代码，查明原因");
            return;
        }

        String sLatexViewName = selectedStruct.getSelectedLatexView();
        String sClickViewName = selectedStruct.getSelectedClickView();
        int sIndex = selectedStruct.getIndex();
        boolean isRootSelected = selectedStruct.isRootSelected();

        if (sLatexViewName != null && !sLatexViewName.isEmpty()
                && sClickViewName != null && !sClickViewName.isEmpty()) {
            if (formulaViews.containsKey(sLatexViewName)) {
                FormulaView formulaView = formulaViews.get(sLatexViewName);
                //在没有光标情况下
                if (sIndex == -1) {
                    //在根布局被选中情况下，则查找其父布局，将新公式添加后该公式后面
                    if (isRootSelected) {
                        String pLatexName = formulaView.getParentFormulaViewName();
                        String pClickName = formulaView.getParentClickViewName();
                        if (pLatexName == null) {
                            if(getRootView()!=null){
                                for (int i = 0; i < getRootView().getChildCount(); ++i) {
                                    View view = getRootView().getChildAt(i);
                                    if (view.equals(formulaView)) {
                                        getRootView().addView(symbolView, i + 1);
                                        addLine(pLatexName, pClickName, i + 2, formulaView.getLevel());
                                        break;
                                    }
                                }
                            }

                        } else {
                            FormulaView pFormulaView = formulaViews.get(pLatexName);
                            if (pFormulaView != null) {
                                int index = pFormulaView.getChildViewIndex(pClickName, formulaView.getLatexView());
                                pFormulaView.addChildView(pClickName, index + 1, symbolView);
                                addLine(pLatexName, pClickName, index + 2, pFormulaView.getLevel());
                            } else {
                                Log.e(TAG ,"有选中数据，在整体布局中没有，请查找代码问题");
                            }
                        }
                    } else {
                        //在某元素被选中状态时，插入符号类型，则直接插入
                        formulaView.addChildView(sClickViewName, -1, symbolView);
                        if (isSpecial(formulaView, sClickViewName)) {
                            addLine(sLatexViewName, sClickViewName, -1, formulaView.getLevel());
                        } else {
                            addLine(sLatexViewName, sClickViewName, -1, formulaView.getLevel() + 1);
                        }
                    }
                } else {
                    //存在光标
                    formulaView.addChildView(sClickViewName, sIndex, symbolView);
                    selectedStruct.setIndex(sIndex + 1);
                }
            } else {
                Log.e(TAG ,"没有找到对应公式：" + sLatexViewName);
            }
        } else {
            //在根节点的光标处，添加公式
            if (sIndex != -1) {
                //添加符号后，光标后移一位
                ViewGroup viewGroup = getRootView();
                if (viewGroup != null) {
                    viewGroup.addView(symbolView, sIndex);
                }
                changeDoneViewState(true);
                selectedStruct.setIndex(sIndex + 1);
            } else {
                Log.e(TAG ,"在没有选中元素的情况下，必然会有光标，请检查代码");
            }
        }
        moveCoordinate();
    }

    //删除数学公式
    public void deleteMathFormula() {
        if (selectedStruct == null) {
            Log.e(TAG ,"select struct为null，请检查代码，查明原因");
            return;
        }

        String sLatexViewName = selectedStruct.getSelectedLatexView();
        String sClickViewName = selectedStruct.getSelectedClickView();
        int sIndex = selectedStruct.getIndex();
        boolean isRootSelected = selectedStruct.isRootSelected();

        if (sLatexViewName != null && !sLatexViewName.isEmpty()
                && sClickViewName != null && !sClickViewName.isEmpty()) {
            if (formulaViews.containsKey(sLatexViewName)) {
                FormulaView formulaView = formulaViews.get(sLatexViewName);
                //在没有光标情况下
                if (sIndex == -1) {
                    //在根布局被选中情况下，则查找其父布局，将该公式干掉
                    if (isRootSelected) {
                        String pLatexName = formulaView.getParentFormulaViewName();
                        String pClickName = formulaView.getParentClickViewName();
                        if (pLatexName == null) {
                            if(getRootView()!=null){
                                for (int i = 0; i < getRootView().getChildCount(); ++i) {
                                    View view = getRootView().getChildAt(i);
                                    if (view.equals(formulaView)) {
                                        getRootView().removeView(formulaView);
                                        addLine(pLatexName, pClickName, i, formulaView.getLevel());
                                        formulaViews.remove(sLatexViewName);
                                        //设置确定按钮的状态
                                        if (getRootView().getChildCount() == 1) {
                                            changeDoneViewState(false);
                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            FormulaView pFormulaView = formulaViews.get(pLatexName);
                            if (pFormulaView != null) {
                                int index = pFormulaView.getChildViewIndex(pClickName, formulaView.getLatexView());
                                pFormulaView.removeChildView(pClickName, index);
                                //在此状态下，必然删除公式，所以要删除数据
                                formulaViews.remove(sLatexViewName);
                            } else {
                                Log.e(TAG ,"有选中数据，在整体布局中没有，请查找代码问题");
                            }
                        }
                    } else {
                        //在某元素被选中状态时，如果内有数据，则删除子元素，如果内没有数据，则选中整个公式
                        if (!formulaView.hasChildCount(sClickViewName)) {
                            formulaView.selectRootView();
                        } else {
                            addLine(sLatexViewName, sClickViewName, -1, formulaView.getLevel());
                        }
                    }
                } else {
                    //存在光标
                    formulaView.removeChildView(sClickViewName, sIndex - 1);
                }
            } else {
                Log.e(TAG ,"没有找到对应公式：" + sLatexViewName);
            }
        } else {
            //在根节点的光标处，删除前一条数据
            if (sIndex != -1) {
                if (sIndex == 0) {
                    return;
                }
                //添加符号后，光标后移一位
                ViewGroup viewGroup = getRootView();
                if (viewGroup != null) {
                    View childView = viewGroup.getChildAt(sIndex -1);
                    if (childView instanceof FormulaView) {
                        FormulaView formulaView = (FormulaView) childView;
                        formulaView.selectRootView();
                    } else if (childView instanceof SimpleSymbolView) {
                        viewGroup.removeView(childView);
                        selectedStruct.setIndex(sIndex - 1);
                        //设置确定按钮的状态
                        if (viewGroup.getChildCount() == 1) {
                            changeDoneViewState(false);
                        }
                    } else {
                        Log.e(TAG ,"不能解析的公式：" + childView.getClass().getSimpleName());
                    }
                }
            } else {
                Log.e(TAG ,"在没有选中元素的情况下，必然会有光标，请检查代码");
            }
        }
    }

    //某个控件被选中了
    public void notifyClicked(String latexView, String clickViewName, boolean isRootSelected) {
        Log.d(TAG ,"选中了：" + latexView + "..." + clickViewName);

        //首先判断点击的控件是否是编辑框，如果是，则在尾部添加竖线
        if (latexView.equals(EditView.class.getSimpleName())) {
            addRootLine();
        } else {
            //如果点击的不是编辑框，则删除内部的竖线
            deleteLine();
            selectedStruct = new SelectedStruct();
            selectedStruct.setSelectedLatexView(latexView);
            selectedStruct.setSelectedClickView(clickViewName);
            selectedStruct.setRootSelected(isRootSelected);
            selectedStruct.setIndex(-1);

        }

        //设置其他控件为非选中状态
        for (Map.Entry<String, FormulaView> entry : formulaViews.entrySet()) {
            if (!entry.getKey().equals(latexView)) {
                entry.getValue().clearBackground();
            }
        }
    }

    public void resetData() {
        selectedStruct = null;
        formulaViews.clear();
    }

    //调整确定按钮状态
    private void changeDoneViewState(boolean isDone) {
        TextView textView = LateXConfig.getInstance().getTvConfirm();
        if (textView != null) {
            if (isDone) {
                // 改变view为确定按钮
                textView.setText("确定");
                textView.setBackgroundColor(Color.parseColor("#6492ff"));
            } else {
                // 改变view为灰色
                textView.setText("关闭");
                textView.setBackgroundColor(Color.parseColor("#b8b8b8"));
            }
        }
    }

    //转换为latex公式语法
    public ConvertResult toLatexString() {
        ConvertResult convertResult = new ConvertResult();
        ViewGroup viewGroup = getRootView();
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof SimpleSymbolView) {
                    SimpleSymbolView symbolView = (SimpleSymbolView) childView;
                    symbolView.toLatexString(convertResult);
                } else if (childView instanceof FormulaView) {
                    FormulaView formulaView = (FormulaView) childView;
                    if (formulaView.getLatexView().equals(LineView.class.getSimpleName())) {
                        //不做任何处理
                    } else {
                        formulaView.toLatexString(convertResult);
                        if (!convertResult.isSuccess) {
                            return convertResult;
                        }
                    }
                } else {
                    convertResult.message = "包含不能识别的内容";
                    convertResult.isSuccess = false;
                    return convertResult;
                }
            }
        }

        if (convertResult.message.isEmpty()) {
            //如果数据为空，则返回false
            convertResult.message = "输入框中内容为空";
            convertResult.isSuccess = false;
        } else {
            Log.d(TAG ,"latex公式为:" + convertResult.message);
            convertResult.isSuccess = true;
        }

        return convertResult;
    }

    private void moveCoordinate() {
        if (formulaViews.containsKey(LineView.class.getSimpleName())) {
            FormulaView lineView = formulaViews.get(LineView.class.getSimpleName());
            int[] location = new int[2];
            lineView.getLocationOnScreen(location);
            ViewGroup etView = getRootView();
            if (etView != null) {
                HorizontalScrollView hsvLatex = LateXConfig.getInstance().getHsvLatex();
                hsvLatex.scrollTo(location[0] + lineView.getWidth() + etView.getWidth(), location[1]);
            }
        }
    }

    public void release() {
        if (formulaViews != null) {
            formulaViews.clear();
        }
    }
}

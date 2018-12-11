package com.tianshaokai.mathkeyboard.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tianshaokai.mathkeyboard.manager.ConvertResult;
import com.tianshaokai.mathkeyboard.manager.Parser;
import com.tianshaokai.mathkeyboard.manager.ViewAssembleManager;
import com.tianshaokai.mathkeyboard.utils.UUIDUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 公式view基类
 */

public class FormulaView extends LinearLayout {
    private static final String TAG = "FormulaView";
    protected String latexView;
    protected int level = 1;            //默认层级，1
    private ViewGroup rootView;
    private boolean isRootSelected = false;
    private String parentFormulaViewName, parentClickViewName;

    protected FormulaView(Context context, int level) {
        super(context);
        this.level = level;
        latexView = getClass().getSimpleName() + "&" + UUIDUtil.generator();
    }

    protected FormulaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        latexView = getClass().getSimpleName() + "&" + UUIDUtil.generator();
    }

    protected FormulaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        latexView = getClass().getSimpleName() + "&" + UUIDUtil.generator();
    }

    /**
     * 获取latexview名称
     *
     * @return
     */
    public String getLatexView() {
        return this.latexView;
    }

    //获取view所在层次
    public int getLevel() {
        return this.level;
    }


    private Map<String, ViewGroup> clickViews = new HashMap<>();

    /**
     * 设置可点击控件
     *
     * @param view
     * @param isFirst 初始化时，是否被选中
     * @param isRoot  是否是布局根节点
     */
    public void setCanClickView(ViewGroup view, boolean isFirst, boolean isRoot) {
        clickViews.put(getResources().getResourceEntryName(view.getId()), view);
        view.setOnClickListener(clickListener);

        if (isFirst) {
            view.setBackgroundColor(Color.parseColor("#CC33B5E5"));
            notifyClicked(getResources().getResourceEntryName(view.getId()));
        }

        if (isRoot) {
            rootView = view;
        }
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //特殊处理下，如果这里是编辑框，则不做任何处理
            if (FormulaView.this.getClass().getSimpleName().equals(EditView.class.getSimpleName())) {
                notifyClicked(null);
                return;
            }

            int id = v.getId();


            //如果是rootview，则设置为整体view已选中状态
            if (rootView == null) {
                Log.e(TAG, "此布局没有root view");
                return;
            }

            if (id == rootView.getId()) {
                isRootSelected = true;
            } else {
                isRootSelected = false;
            }

            //更新选中元素
            for (Map.Entry<String, ViewGroup> view : clickViews.entrySet()) {
                if (view.getValue().getId() == id) {
                    view.getValue().setBackgroundColor(Color.parseColor("#CC33B5E5"));
                    notifyClicked(getResources().getResourceEntryName(id));
                } else {
                    view.getValue().setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    };

    //提示其他功能，该控件被点击
    private void notifyClicked(String clickViewName) {
        ViewAssembleManager.getInstance().notifyClicked(latexView, clickViewName, isRootSelected);
    }

    public void clearBackground() {
        for (Map.Entry<String, ViewGroup> view : clickViews.entrySet()) {
            view.getValue().setBackgroundColor(Color.TRANSPARENT);
        }

        isRootSelected = false;
    }

    public boolean isRootSelected() {
        return isRootSelected;
    }

    public void selectRootView() {
        if (rootView == null) {
            Log.e(TAG,"此布局没有root view");
            return;
        }

        rootView.performClick();
    }

    //获取父公式名称
    public String getParentFormulaViewName() {
        return parentFormulaViewName;
    }

    //设置父公式名称
    public void setParentFormulaViewName(String parentViewName) {
        this.parentFormulaViewName = parentViewName;
    }

    //获取所在元素名称
    public String getParentClickViewName() {
        return this.parentClickViewName;
    }

    public void setParentClickViewName(String parentClickViewName) {
        this.parentClickViewName = parentClickViewName;
    }

    public boolean hasChildCount(String clickViewName) {
        if (!clickViews.containsKey(clickViewName)) {
            Log.e(TAG,"在保存的数组中，没有找到对应的控件：" + clickViewName);
            return false;
        }

        ViewGroup clickViewGroup = clickViews.get(clickViewName);
        if (clickViewGroup.getChildCount() > 1) {
            return true;
        } else {
            return false;
        }
    }

    public View getIndexPreviousView(String clickViewName, int index) {
        if (index > 0) {
            if (!clickViews.containsKey(clickViewName)) {
                return null;
            }

            ViewGroup selectedClickViewGroup = clickViews.get(clickViewName);
            //保证在可编辑布局内，只有一个子节点存在
            if (selectedClickViewGroup.getChildCount() <= 1) {
                return null;
            } else {
               return selectedClickViewGroup.getChildAt(index - 1);
            }
        } else {
            return null;
        }
    }

    //在布局尾部添加子节点
    public void addChildView(String clickViewName, int index, SimpleSymbolView symbolView) {
        if (!clickViews.containsKey(clickViewName)) {
            Log.e(TAG,"在保存的数组中，没有找到对应的控件：" + clickViewName);
            return;
        }
        ViewGroup selectedClickViewGroup = clickViews.get(clickViewName);
        //保证在可编辑布局内，只有一个子节点存在
        if (selectedClickViewGroup.getChildCount() <= 1) {
            //首先把虚框设置为不可显示状态
            selectedClickViewGroup.getChildAt(0).setVisibility(View.GONE);
        }

        //添加子公式
        if (index != -1) {
            selectedClickViewGroup.addView(symbolView, index);
        } else {
            selectedClickViewGroup.addView(symbolView);
        }

        symbolView.setParentLatexView(latexView);
        symbolView.setParentClickView(clickViewName);
    }

    //添加子节点，在指定index位置添加子节点
    public void addChildView(String clickViewName, int index, FormulaView subViewGroup) {
        if (!clickViews.containsKey(clickViewName)) {
            Log.e(TAG,"在保存的数组中，没有找到对应的控件：" + clickViewName);
            return;
        }

        ViewGroup clickViewGroup = clickViews.get(clickViewName);
        if (clickViewGroup.getChildCount() <= 1) {
            clickViewGroup.getChildAt(0).setVisibility(View.GONE);
        }

        if (index == -1) {
            clickViewGroup.addView(subViewGroup);
        } else {
            clickViewGroup.addView(subViewGroup, index);
        }

        subViewGroup.setParentFormulaViewName(latexView);
        subViewGroup.setParentClickViewName(clickViewName);
    }

    //删除被选中元素内指定index位置子节点
    public void removeChildView(String clickViewName, int index) {
        ViewGroup viewGroup = clickViews.get(clickViewName);

        if (viewGroup == null || viewGroup.getChildCount() <= 1) {
            Log.e(TAG,"该元素只有一个子节点了，不能操作删除操作");
            return;
        }

        View childView;
        if (index == -1) {
            childView = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        } else {
            childView = viewGroup.getChildAt(index);
        }


        if (childView instanceof FormulaView) {
            //把尾部的公式置为选中状态
            FormulaView formulaView = (FormulaView) childView;
            if (!formulaView.isRootSelected()) {
                formulaView.selectRootView();
            } else {
                viewGroup.removeView(childView);

                //把子元素内容已清空，需要显示虚框，同时把虚框置为选中状态
                if (viewGroup.getChildCount() == 1) {
                    showDashedBox(clickViewName);
                    viewGroup.performClick();
                } else {
                    //如果不是删除光标，则在原位置插入光标
                    if (!formulaView.getLatexView().equals(LineView.class.getSimpleName())) {
                        ViewAssembleManager.getInstance().addLine(latexView, clickViewName, index, level + 1);
                    }
                }
            }
        } else if (childView instanceof SimpleSymbolView) {
            //直接干掉
            viewGroup.removeView(childView);
            //如果只包含光标，则置为空
            if (viewGroup.getChildCount() == 2) {
                View view = viewGroup.getChildAt(1);
                if (view instanceof LineView) {
                    showDashedBox(clickViewName);
                    viewGroup.performClick();
                }
            } else {
                //删除图标后，要把光标的位置调整下
                ViewAssembleManager.getInstance().updateSelectedStructIndex(index);
            }
        } else {
            Log.e(TAG,"不认识的子节点类型：" + childView.getClass().getSimpleName());
        }
    }

    //获取元素内子元素所在位置
    public int getChildViewIndex(String clickViewName, String childViewName) {
        if (!clickViews.containsKey(clickViewName)) {
            Log.e(TAG,"在保存的数组中，没有找到对应的控件：" + clickViewName + "为了保证不崩溃，则删除第一个数据");
            return 0;
        }

        ViewGroup clickViewGroup = clickViews.get(clickViewName);

        for (int index = 1; index < clickViewGroup.getChildCount(); ++index) {
            View view = clickViewGroup.getChildAt(index);
            if (view instanceof SimpleSymbolView) {
                SimpleSymbolView symbolView = (SimpleSymbolView) view;
                if (symbolView.getSymbolName().equals(childViewName)) {
                    return index;
                }
            } else if (view instanceof FormulaView) {
                FormulaView formulaView = (FormulaView) view;
                if (formulaView.getLatexView().equals(childViewName)) {
                    return index;
                }
            } else {
                Log.e(TAG,"存在不能识别元素：" + view.toString());
            }
        }

        return 0;
    }

    /**
     * 显示虚框
     */
    private void showDashedBox(String clickViewName) {
        if (!clickViews.containsKey(clickViewName)) {
            Log.e(TAG,"在保存的数组中，没有找到对应的控件：" + clickViewName);
            return;
        }

        ViewGroup clickViewGroup = clickViews.get(clickViewName);
        //首先保证在可编辑布局内，只有一个子节点存在
        if (clickViewGroup.getChildCount() != 1) {
            for (int i = 1; i < clickViewGroup.getChildCount(); ++i) {
                clickViewGroup.removeViewAt(i);
            }
        }

        //首先把虚框设置为显示状态
        clickViewGroup.getChildAt(0).setVisibility(View.VISIBLE);
    }

    public boolean isEmpty(String clickViewName) {
        ViewGroup viewGroup = clickViews.get(clickViewName);
        if (viewGroup.getChildCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSpecial(String clickName) {
        return false;
    }

    public void toLatexString(ConvertResult convertResult) {
        convertResult.message = "未实现" + this.getClass().getSimpleName() + "类型公式转换";
        convertResult.isSuccess = false;
    }

    protected void toLatexString(ViewGroup clickView, ConvertResult convertResult) {
        if (clickView.getChildCount() > 1) {
            for (int i = 1; i < clickView.getChildCount(); ++i) {
                View childView = clickView.getChildAt(i);
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
                            return;
                        }
                    }
                } else {
                    convertResult.message = "包含不能识别的内容";
                    convertResult.isSuccess = false;
                    return;
                }
            }
            convertResult.isSuccess = true;
        } else {
            convertResult.message = "公式：" + this.getClass().getSimpleName() + "中" +
                    getResources().getResourceEntryName(clickView.getId())
                    + "为空";
            convertResult.isSuccess = false;
        }
    }

    public void parseLatexString(String string) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + "未实现latex解析");
    }

    protected void parseLatexString(ViewGroup viewGroup, String string) {
        Log.d(TAG, latexView + ">>>" + getResources().getResourceEntryName(viewGroup.getId()) + " latex：" + string);
        viewGroup.performClick();

        Parser.latex2View(getContext(), string);
    }

    @Override
    public String toString() {
        return latexView + " > " + level;
    }
}

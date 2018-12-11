package com.tianshaokai.mathkeyboard.manager;

/**
 * 被选中元素，结构体
 *
 * 可能出现情况：
 * 1. latexView, clickView为null，即没有选中元素，则必存在光标，index必不为-1
 *      此中情况，只有在光标插入到根节点时出现
 * 2. latexView, clickView不为null, 即有选中元素或者非一级光标，分两种情况：
 *   a. index为-1，则选中的是公式元素
 *   b. index不为-1，则是插入的光标
 */

public class SelectedStruct {

    //公式名称，被选中元素名称
    private String selectedLatexView, selectedClickView;
    private boolean isRootSelected = false;
    //插入位置，现在光标所在位置
    private int index = -1;

    public SelectedStruct() {

    }


    public String getSelectedLatexView() {
        return selectedLatexView;
    }

    public void setSelectedLatexView(String selectedLatexView) {
        this.selectedLatexView = selectedLatexView;
    }

    public String getSelectedClickView() {
        return selectedClickView;
    }

    public void setSelectedClickView(String selectedClickView) {
        this.selectedClickView = selectedClickView;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isRootSelected() {
        return isRootSelected;
    }

    public void setRootSelected(boolean rootSelected) {
        isRootSelected = rootSelected;
    }
}

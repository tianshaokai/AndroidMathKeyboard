package com.tianshaokai.mathkeyboard.manager;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.tianshaokai.mathkeyboard.widget.FormulaView;

/**
 * latex配置类
 */

public class LateXConfig {

    private static LateXConfig instance;

    public static LateXConfig getInstance() {
        synchronized (LateXConfig.class) {
            if (instance == null) {
                instance = new LateXConfig();
            }
        }

        return instance;
    }

    private FormulaView rl_root;
    private TextView tvConfirm;
    private Context context;
    private HorizontalScrollView hsvLatex;

    //设置跟节点
    public void setEditRoot(FormulaView rl_root, HorizontalScrollView hsvLatex) {
        this.rl_root = rl_root;
        this.context = rl_root.getContext().getApplicationContext();
        this.hsvLatex = hsvLatex;
        ViewAssembleManager.getInstance().resetData();
        //初始化，添加竖线
        ViewAssembleManager.getInstance().addRootLine();
    }

    public void setConfirmView(TextView tvConfirm) {
        this.tvConfirm = tvConfirm;
    }

    public TextView getTvConfirm() {
        return tvConfirm;
    }

    public ViewGroup getEditRoot() {
        return this.rl_root;
    }

    public Context getContext() {
        return this.context;
    }

    public HorizontalScrollView getHsvLatex() {
        return hsvLatex;
    }

    public void setHsvLatex(HorizontalScrollView hsvLatex) {
        this.hsvLatex = hsvLatex;
    }

    public void setRl_root(FormulaView rl_root) {
        this.rl_root = rl_root;
    }
}

package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtils {


    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static float getFontDensity(Context context) {
        return getDisplayMetrics(context).scaledDensity;
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 单位转换: dp -> px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

    /**
     * 单位转换:px -> dp
     *
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        return (int) (px / getDensity(context) + 0.5);
    }

    /**
     * 单位转换: sp -> px
     *
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
        return (int) (getFontDensity(context) * sp + 0.5);
    }

    /**
     * 单位转换:px -> sp
     *
     * @param px
     * @return
     */
    public static int px2sp(Context context, float px) {
        return (int) (px / getFontDensity(context) + 0.5);
    }
}

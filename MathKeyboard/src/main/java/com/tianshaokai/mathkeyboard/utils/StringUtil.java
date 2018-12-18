package com.tianshaokai.mathkeyboard.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关操作类
 */
public class StringUtil {

    /**
     * 查找指定字符串在某个字符串中出现的次数
     * @param srcText   源字符串
     * @param findText  要查找的字符串
     * @return  出现的次数
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
}

package com.tianshaokai.mathkeyboard.utils;

import android.util.Log;

import java.util.HashMap;

public class RichTextManager {
    private static final String TAG = "RichTextManager";
    private static final HashMap<String,HashMap<String,RichText>> instances = new HashMap<>();

    /**
     * 绑定RichText到指定tag
     * @param tag
     * @param richText
     */
    public static void bind(String tag,RichText richText){
        HashMap<String,RichText> richTextMap = instances.get(tag);
        if (richTextMap == null || richTextMap.size() == 0){
            richTextMap = new HashMap<>();
            instances.put(tag,richTextMap);
        }
        if (richTextMap.containsKey(richText.getId())){
            Log.d(TAG, "bind--->该" + richText.getId() + "对应的RichText已存在集合，先释放资源并移除，再绑定新的");
            RichText temp = richTextMap.get(richText.getId());
            if (null != temp){
                temp.release();
                temp = null;
            }
            richTextMap.remove(richText.getId());
        }
        richTextMap.put(richText.getId(),richText);
    }

    /**
     * 清除tag绑定的所有RichText，并释放所有的图片缓存
     * @param tag
     */
    public static void clear(String tag){
        Log.d(TAG, "clear--->清除tag绑定的所有RichText，并释放所有的图片缓存，tag：" + tag);
        HashMap<String,RichText> richTextMap = instances.get(tag);
        if (null != richTextMap){
            for (RichText richText:richTextMap.values()){
                if (null != richText){
                    richText.release();
                    richText = null;
                }
            }
        }else {
            Log.d(TAG, "clear--->该tag绑定的RichText集合为空,tag："  + tag);
        }
        instances.remove(tag);
    }

    /**
     * 释放所有tag绑定的RichText资源
     */
    public static void release(){
        Log.d(TAG, "release--->释放所有tag绑定的RichText资源");
        for (HashMap<String,RichText> richTextMap:instances.values()){
            if (null != richTextMap){
                for (RichText richText:richTextMap.values()){
                    if (null != richText){
                        richText.release();
                        richText = null;
                    }
                }
                richTextMap.clear();
            }
        }
        instances.clear();
    }
}

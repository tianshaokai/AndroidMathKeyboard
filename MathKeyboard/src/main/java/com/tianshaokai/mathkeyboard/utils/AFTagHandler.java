package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AFTagHandler implements Html.TagHandler {

    private int startIndex = 0;
    private int stopIndex = 0;

    private ColorStateList mOriginColors;
    private RichText.OnImgClickListener mImgClickListener;

    public AFTagHandler(ColorStateList originColors, RichText.OnImgClickListener imgClickListener){
        mOriginColors = originColors;
        mImgClickListener = imgClickListener;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        processAttributes(xmlReader);

        if(tag.equalsIgnoreCase("span")){
            if(opening){
                startSpan(tag, output, xmlReader);
            }else{
                endSpan(tag, output, xmlReader);
                attributes.clear();
            }
        }else if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 处理标签<img>
            // 获取长度
            if (null != mImgClickListener) {
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();

                // 使图片可点击并监听点击事件
                output.setSpan(new ClickableImage(imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void startSpan(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }

    public void endSpan(String tag, Editable output, XMLReader xmlReader){
        stopIndex = output.length();

        String color = attributes.get("color");
        String size = attributes.get("size");
        String style = attributes.get("style");
        if (!TextUtils.isEmpty(style)){
            analysisStyle(startIndex,stopIndex,output,style);
        }
        if (!TextUtils.isEmpty(size)) {
            size = size.split("px")[0];
        }
        if(!TextUtils.isEmpty(color)){
            if (color.startsWith("@")) {
                Resources res = Resources.getSystem();
                String name = color.substring(1);
                int colorRes = res.getIdentifier(name, "color", "android");
                if (colorRes != 0) {
                    output.setSpan(new ForegroundColorSpan(colorRes), startIndex, stopIndex,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                try {
                    output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    reductionFontColor(startIndex,stopIndex,output);
                }
            }
        }
    }

    final HashMap<String, String> attributes = new HashMap<String, String>();

    private void processAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);

            /**
             * MSH: Look for supported attributes and add to hash map.
             * This is as tight as things can get :)
             * The data index is "just" where the keys and values are stored.
             */
            for(int i = 0; i < len; i++)
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
        }
        catch (Exception e) {
        }
    }

    /**
     * 还原为原来的颜色
     * @param startIndex
     * @param stopIndex
     * @param editable
     */
    private void reductionFontColor(int startIndex,int stopIndex,Editable editable){
        if (null != mOriginColors){
            editable.setSpan(new TextAppearanceSpan(null, 0, 0, mOriginColors, null),
                    startIndex, stopIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            editable.setSpan(new ForegroundColorSpan(0xff2b2b2b), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 解析style属性
     * @param startIndex
     * @param stopIndex
     * @param editable
     * @param style
     */
    private void analysisStyle(int startIndex,int stopIndex,Editable editable,String style){
        String[] attrArray = style.split(";");
        Map<String,String> attrMap = new HashMap<>();
        if (null != attrArray){
            for (String attr:attrArray){
                String[] keyValueArray = attr.split(":");
                if (null != keyValueArray && keyValueArray.length == 2){
                    // 记住要去除前后空格
                    attrMap.put(keyValueArray[0].trim(),keyValueArray[1].trim());
                }
            }
        }

        String color = attrMap.get("color");
        String fontSize = attrMap.get("font-size");
        if (!TextUtils.isEmpty(fontSize)) {
            fontSize = fontSize.split("px")[0];
        }
        if(!TextUtils.isEmpty(color)){
            if (color.startsWith("@")) {
                Resources res = Resources.getSystem();
                String name = color.substring(1);
                int colorRes = res.getIdentifier(name, "color", "android");
                if (colorRes != 0) {
                    editable.setSpan(new ForegroundColorSpan(colorRes), startIndex, stopIndex,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                try {
                    editable.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    reductionFontColor(startIndex,stopIndex,editable);
                }
            }
        }
    }

    private long lastClickTime = 0;

    private class ClickableImage extends ClickableSpan {

        private String url;

        public ClickableImage(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 进行图片点击之后的处理
            long currentTime = Calendar.getInstance().getTimeInMillis();
            int MIN_CLICK_DELAY_TIME = 2 * 1000;
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                if (null != mImgClickListener){
                    mImgClickListener.onImgClick(url);
                }
            }
        }
    }
}

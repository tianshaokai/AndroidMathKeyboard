package com.tianshaokai.mathkeyboard.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public HtmlUtil() {
    }

    public static HashMap<String, String> getImgWidthHeightByHtmlStr(String htmlStr) {
        HashMap<String, String> hashMap = new HashMap();
        Document document = Jsoup.parse(htmlStr);
        Elements imgElements = document.select("img");

        Element element;
        String widthHeightStr;
        for(Iterator var4 = imgElements.iterator(); var4.hasNext(); hashMap.put(element.attr("src"), widthHeightStr)) {
            element = (Element)var4.next();
            Attributes attributes = element.attributes();
            widthHeightStr = "";
            if (null != attributes) {
                String style = attributes.get("style");
                style = style.replaceAll(" ", "");
                String reWidth = "width:([0-9]+)";
                Pattern p = Pattern.compile(reWidth, 34);
                Matcher m = p.matcher(style);
                String reHeight;
                if (m.find()) {
                    reHeight = m.group(1);
                    widthHeightStr = widthHeightStr + reHeight;
                }

                reHeight = "height:([0-9]+)";
                p = Pattern.compile(reHeight, 34);
                m = p.matcher(style);
                if (m.find()) {
                    String height = m.group(1);
                    widthHeightStr = widthHeightStr + "," + height;
                }
            }
        }

        return hashMap;
    }
}

package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.logging.Logger;

public class RichText {
    private static final String TAG = "RichText";
    private String richText;                // 富文本数据
    private boolean isResize;               // 是否根据img标签内的style属性的宽高重新设置大小，默认不重新设置
    private boolean isDownload;             // 是否下载图片,默认下载
    private boolean imgClickable;           // 是否可以点击图片,默认不可以点击
    private int margin = 100;               // 富文本显示区域距离屏幕两侧的边距，图片宽度不能超过最大值=屏幕宽度-边距，默认100，超过后会按最大值算
    private String tag;                     // tag,默认为context全类名
    private Context context;
    private TextView textView;              // 要设置富文本的控件
    private AFImageGetter imageGetter;
    private AFTagHandler tagHandler;
    private OnImgClickListener imgClickListener;    // 图片点击监听

    private RichText(boolean isResize, boolean isDownload) {
        this.isResize = isResize;
        this.isDownload = isDownload;
    }

    private RichText() {
        this(false, true);
    }

    /**
     * 设置富文本数据，创建对象
     *
     * @param richText 富文本数据
     * @return 当前对象
     */
    public static RichText fromHtml(String richText) {
        RichText r = new RichText();
        r.richText = richText;
        return r;
    }

    /**
     * 设置上下文
     *
     * @param context 上下文
     * @return 当前对象
     */
    public RichText with(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 设置是否重新设置大小
     *
     * @param isResize 是否重新设置大小
     * @return 当前对象
     */
    public RichText isResize(boolean isResize) {
        this.isResize = isResize;
        return this;
    }

    /**
     * 设置是否下载图片
     *
     * @param isDownload
     * @return
     */
    public RichText isDownload(boolean isDownload) {
        this.isDownload = isDownload;
        return this;
    }

    /**
     * 设置是否可以点击图片
     * @param imgClickable
     * @return
     */
    public RichText setImgClickable(boolean imgClickable) {
        this.imgClickable = imgClickable;
        return this;
    }

    /**
     * 设置图片点击事件
     * @param imgClickListener
     * @return
     */
    public RichText setImgClickListener(OnImgClickListener imgClickListener) {
        this.imgClickListener = imgClickListener;
        return this;
    }

    /**
     * 设置边距
     * @param margin
     * @return
     */
    public RichText setMargin(int margin){
        this.margin = margin;
        return this;
    }

    /**
     * 设置tag，为空默认为context全类名
     * @param tag
     */
    public RichText setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 设置显示富文本数据的控件
     *
     * @param textView 控件
     */
    public RichText into(TextView textView, AFCallback callback) {
        this.textView = textView;
        if (TextUtils.isEmpty(tag)){
            tag = context.getClass().getName();
        }
        RichTextManager.bind(tag,this);
        if (imgClickable) {
            this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        HashMap<String, String> hashMap = null;
        if (isResize) {
            hashMap = HtmlUtil.getImgWidthHeightByHtmlStr(richText);
        }
        if (null != imageGetter) {
            imageGetter.recycleBitmap();
        }
        try {
            imageGetter = new AFImageGetter(context,this, textView, hashMap,margin,isDownload,callback);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        tagHandler = new AFTagHandler(textView.getTextColors(), imgClickListener);
        textView.setText(Html.fromHtml(richText, imageGetter, tagHandler));
        return this;
    }

    public String getId(){
        if (null != textView){
            return String.valueOf(textView.hashCode());
        }
        return UUIDUtil.generator();
    }

    public String getRichText() {
        return richText;
    }

    public void release() {
        if (null != imageGetter) {
            imageGetter.recycleBitmap();
            imageGetter = null;
        }
        textView = null;
        context = null;
    }

    public void refresh(){
        if (textView == null){
            Log.d(TAG, "--->textView 为空");
            return;
        }
        Log.d(TAG, "--->RichText 刷新");
        textView.setText(Html.fromHtml(richText, imageGetter, tagHandler));
    }

    public interface OnImgClickListener{

        void onImgClick(String filePath);
    }
}

package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

public class AFImageGetter implements Html.ImageGetter {

    private final String TAG = "AFImageGetter";
    private URLDrawable urlDrawable = null;
    private TextView textView;
    private Context context;
    private HashMap<String, String> imageMap = null;
    private AFCallback callback;
    private int maxWidth,imgTagCount = 0,count = 0;
    private boolean isDownload;
    private RichText r;
    private Bitmap defaultBitmap = null;
    private boolean isRefresh = false;
    private HashMap<String,Bitmap> bitmapMap = new HashMap<>();

    public AFImageGetter(Context context, TextView textView, int margin, boolean isDownload) {
        this(context,null,textView,null,margin,isDownload,null);
    }

    public AFImageGetter(Context context, TextView textView, HashMap<String, String> imageMap, int margin, boolean isDownload, AFCallback callback) {
        this(context, null,textView, imageMap,margin, isDownload,callback);
    }

    public AFImageGetter(Context context, RichText r,TextView textView, HashMap<String, String> imageMap, int margin, boolean isDownload, AFCallback callback) {
        this.textView = textView;
        this.r = r;
        this.context = context;
        this.imageMap = imageMap;
        this.callback = callback;
        this.isDownload = isDownload;
        maxWidth = DisplayUtils.getScreenWidth(context) - margin;
        if (r != null) {
            isRefresh = false;
            count = 0;
            imgTagCount = StringUtil.appearNumber(r.getRichText(), "<img");
        }
        recycleBitmapMap();
    }

    @Override
    public Drawable getDrawable(final String source) {
        urlDrawable = new URLDrawable();
        try {
            if (!source.startsWith("http")){
                boolean is2Px = false;
                if (source.matches("data:image.*base64.*")) {
                    Log.d(TAG, "--->加载base64图片：" + source);
                    is2Px = true;
                }else {
//                    Log.d(TAG,"--->加载的地址为本地图片：" + source);
                    is2Px = false;
                }
                loadLocalBitmap(source, source, is2Px);
                count++;
                if (count == imgTagCount && null != r) {
                    if (isRefresh) {
                        r.refresh();
                    } else {
//                        Log.d(TAG,"getDrawable--->isRefresh为false,不刷新");
                    }
                }
                return urlDrawable;
            }
            if (isDownload) {
                final String filename = source.substring(source.lastIndexOf("/") + 1);
                final String path = FileUtil.getPackageDCIMPath(context);
                String fullPath = path + "/" + filename;
                final File file = new File(fullPath);
                if (file.exists()) {
                    loadLocalBitmap(source, fullPath, true);
                    count++;
                    if(count == imgTagCount && null != r){
                        if (isRefresh) {
                            r.refresh();
                        }else {
//                            Log.d(TAG,"getDrawable--->isRefresh为false,不刷新");
                        }
                    }
                } else {
                    isRefresh = true;
                    loadDefaultBitmap();
                    if (callback == null) {
                        callback = new AFCallback<String>() {
                            @Override
                            public void onStart(String uuid) {
                                Log.d(TAG,"开始缓冲" + path + "/" + filename);
                            }

                            @Override
                            public void onProcess(int i) {
//                                Log.d("Img", "下载进度：" + i);
                            }

                            @Override
                            public void onSuccess(String s) {
                                Log.d(TAG,"下载完成" + path + "/" + filename);
                                count++;
                                if(count == imgTagCount && null != r){
                                    r.refresh();
                                    isRefresh = false;
                                }
                            }

                            @Override
                            public void onFailed(String s) {
                                Log.e(TAG,"下载失败");
                                try {
                                    file.delete();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                    }
//                    DownloadManager.getInstance().download(context,new URL(source), fullPath, callback);
                }
            } else {
                loadByGlide(source,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlDrawable;
    }

    /**
     * 通过Glide加载
     * @param source
     */
    private void loadByGlide(final String source, final boolean is2Px){
        if (null != context) {
//            Glide.with(context.getApplicationContext()).load(source).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    if (null != imageMap) {
//                        String widthHeight = imageMap.get(source);
//                        if (!TextUtils.isEmpty(widthHeight)) {
//                            String[] whArray = widthHeight.split(",");
//                            if (null != whArray && whArray.length == 2 &&
//                                    !TextUtils.isEmpty(whArray[0]) && !TextUtils.isEmpty(whArray[1])) {
//                                // 获得图片的宽高
//                                int newWidth = PixelUtil.dip2px(context, Float.parseFloat(whArray[0]));
//                                int newHeight = PixelUtil.dip2px(context, Float.parseFloat(whArray[1]));
//                                // 得到新的图片
//                                if (newWidth > maxWidth) {
//                                    newWidth = maxWidth;
//                                }
//                                urlDrawable.bitmap = BitmapUtil.scaleImage(resource, newWidth, newHeight);
//                                setTextBitmap(source, urlDrawable.bitmap);
//                            } else {
//                                getBitmap(source, is2Px, resource);
//                            }
//                        } else {
//                            getBitmap(source, is2Px, resource);
//                        }
//                    } else {
//                        getBitmap(source, is2Px, resource);
//                    }
//                }
//            });
        } else {
//            Log.e(TAG,"——context为空");
        }
    }

    /**
     * 加载默认图片
     */
    private void loadDefaultBitmap(){
//        if (defaultBitmap == null){
//            defaultBitmap = BitmapUtil.loadBitmap(context, R.drawable.default_image,100,100);
//        }
//        urlDrawable.bitmap = defaultBitmap;
//        urlDrawable.setBounds(0, 0, urlDrawable.bitmap.getWidth(), urlDrawable.bitmap.getHeight());
//        textView.invalidate();
//        textView.setText(textView.getText());//不加这句显示不出来图片，原因不详
    }

    /**
     * 加载本地图片
     * @param source
     * @param fullPath
     */
    private void loadLocalBitmap(String source,String fullPath,boolean is2Px){
//        Log.d(TAG,"loadLocalBitmap--->图片已存在本地，直接返回");
        Bitmap originBitmap = BitmapUtil.loadBitmap(context,fullPath);
        if (null != imageMap) {
            String widthHeight = imageMap.get(source);
            if (!TextUtils.isEmpty(widthHeight)) {
                String[] whArray = widthHeight.split(",");
                if (null != whArray && whArray.length == 2 &&
                        !TextUtils.isEmpty(whArray[0]) && !TextUtils.isEmpty(whArray[1])) {
                    int newWidth = DisplayUtils.dp2px(context, Float.parseFloat(whArray[0]));
                    int newHeight = DisplayUtils.dp2px(context, Float.parseFloat(whArray[1]));
                    // 得到新的图片
                    if (newWidth > maxWidth) {
                        newWidth = maxWidth;
                    }
                    urlDrawable.bitmap = BitmapUtil.scaleImage(originBitmap, newWidth, newHeight);
                    setTextBitmap(source, urlDrawable.bitmap);
                } else {
                    getBitmap(source, is2Px, originBitmap);
                }
            } else {
                getBitmap(source, is2Px, originBitmap);
            }
        } else {
            getBitmap(source, is2Px, originBitmap);
        }
    }

    private void getBitmap(String source, boolean is2Px, Bitmap originBitmap) {
        if (is2Px) {
            int bmWidth = DisplayUtils.dp2px(context, originBitmap.getWidth());
            if (bmWidth > maxWidth) {
                bmWidth = maxWidth;
            }
            urlDrawable.bitmap = BitmapUtil.scaleImage(originBitmap, bmWidth, DisplayUtils.dp2px(context, originBitmap.getHeight()));
        }else {
            int bmWidth = originBitmap.getWidth();
            if (bmWidth > maxWidth) {
                urlDrawable.bitmap = BitmapUtil.scaleImage(originBitmap, maxWidth, originBitmap.getHeight());
            }else {
                urlDrawable.bitmap = originBitmap;
            }
        }
        setTextBitmap(source, urlDrawable.bitmap);
    }

    private void setTextBitmap(String source, Bitmap bitmap) {
        if (null != bitmap) {
            urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            textView.invalidate();
            textView.setText(textView.getText());//不加这句显示不出来图片，原因不详
        }
        addBitmap(source, bitmap);
    }

    public class URLDrawable extends BitmapDrawable {
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (bitmap != null) {
                try {
                    canvas.drawBitmap(bitmap, 0, 0, getPaint());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void recycleBitmap() {
        textView = null;
        context = null;
        if (null != urlDrawable) {
            if (null != urlDrawable.bitmap) {
                urlDrawable.bitmap.recycle();
                urlDrawable.bitmap = null;
            }
        }
        urlDrawable = null;
        if (null != defaultBitmap){
            defaultBitmap.recycle();
            defaultBitmap = null;
        }

        recycleBitmapMap();
    }

    private void recycleBitmapMap(){
//        Log.d(TAG,"recycleBitmapMap--->释放bitmap集合中的所有bitmap");
        if (null != bitmapMap && bitmapMap.size() > 0){
            for (Bitmap bitmap:bitmapMap.values()){
                if (null != bitmap){
                    bitmap.recycle();
                }
            }
            bitmapMap.clear();
            bitmapMap = null;
        }else {
//            Log.d(TAG,"recycleBitmapMap--->bitmap集合为空");
        }
    }

    private void addBitmap(String key,Bitmap bitmap){
        if (bitmap == null){
//            Log.e(TAG,"--->bitmap为空，不添加");
            return;
        }
        if (bitmapMap == null){
            bitmapMap = new HashMap<>();
        }
        if (bitmapMap.containsKey(key)) {
//            Log.d(TAG,"addBitmap--->该key：" + key + "对应的bitmap已存在集合里，重新生成key");
            key = key + "/" + UUIDUtil.generator();
//            Log.d(TAG,"addBitmap--->重新生成的key：" + key);
//            Bitmap temp = bitmapMap.get(key);
//            temp.recycle();
//            bitmapMap.remove(key);
        }
//        Log.d(TAG,"addBitmap--->添加bitmap到集合，key：" + key);
        bitmapMap.put(key,bitmap);
    }
}
package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.tianshaokai.jlatexmath.core.AjLatexMath;
import com.tianshaokai.jlatexmath.core.Insets;
import com.tianshaokai.jlatexmath.core.TeXConstants;
import com.tianshaokai.jlatexmath.core.TeXFormula;
import com.tianshaokai.jlatexmath.core.TeXIcon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LatexUtil {

    public static String latexSavePath = "";

    public static void init(Context context){
        latexSavePath = Environment.getExternalStorageDirectory() + "/Android/data/"
                + context.getPackageName() + "/files/latex/";
        File file = new File(latexSavePath);
        if (!file.exists()){
            file.mkdirs();
        }
        AjLatexMath.init(context);
    }

    public static void asyncAnalysisLatex(final Context context, final String latexContent, final LatexCallback callback){
        asyncAnalysisLatex(context, latexContent, 16,150,callback);
    }

    public static void asyncAnalysisLatex(final Context context, final String latexContent,final int margin, final LatexCallback callback){
        asyncAnalysisLatex(context, latexContent, 16,margin,callback);
    }

    public static void asyncAnalysisLatex(final Context context, final String latexContent, final int textSize,final int margin, final LatexCallback callback){

//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                String content = LatexUtil.analysisLatex(context, latexContent,textSize,margin);
//                subscriber.onNext(content);
//                subscriber.onCompleted();
//            }
//        })
//                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onNext(String content) {
//                        if (null != callback){
//                            callback.onAnalysisLatex(content);
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        if (null != callback){
//                            callback.onError(e.getMessage());
//                        }
//                    }
//                });
    }

    public static synchronized String analysisLatex(Context context, String latexContent) {
        return analysisLatex(context, latexContent,16,200);
    }

    public static synchronized String analysisLatex(Context context, String latexContent,int margin) {
        return analysisLatex(context, latexContent,16,margin);
    }

    public static synchronized String analysisLatex(Context context,String latexContent,int textSize,int margin){
        String mLatexContent = latexContent;

        if (latexContent.contains("$")) {
            latexContent = latexContent.replace("$$", "$");
            mLatexContent = latexContent;
//            Logger.getLogger().d("--->latexContent：" + latexContent);
            boolean isStart = true;
            int start = -1, end = -1;
            for (int i = 0; i < latexContent.length(); i++) {
                char item = latexContent.charAt(i);
                if (item == '$') {
                    if (isStart) {
                        start = i;
                        isStart = false;
                    } else {
                        end = i;
                        isStart = true;

                        String teX$ = latexContent.substring(start, end + 1);
                        String teX = teX$.replace("$", "");
                        if (!TextUtils.isEmpty(teX.trim())) {
//                            Logger.getLogger().d("--->teX$：" + teX$);
                            boolean isLeft = true;
                            String imgTag = createImgTag(context, teX, isLeft, textSize, margin);
                            mLatexContent = mLatexContent.replace(teX$, imgTag);
                        } else {//如果teX$是$$，去掉$$
                            mLatexContent = mLatexContent.replace(teX$, "");
                        }
                    }
                }
            }
        }
//        Logger.getLogger().d("--->mLatexContent：" + mLatexContent);
        return mLatexContent;
    }

    private static String createImgTag(Context context, String latex, boolean isLeft,int textSize,int margin) {
        File file = new File(latexSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }

//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
//        String filePath = latexSavePath + "IMG_" + timeStamp + ".png";
        String fileName = stringToMD5(latex) + ".png";
        String filePath = latexSavePath + fileName;
//        Logger.getLogger().d("--->filePath：" + filePath);
        String imgTag = "<img src='" + filePath + "' />";
        if (new File(filePath).exists()){
            return imgTag;
        }
        try {
            saveBitmap(context, latex, new File(filePath), isLeft,textSize,margin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imgTag;
    }

    private static void saveBitmap(Context context, String teX, File file, boolean isLeft,int textSize,int margin) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        int align = TeXConstants.ALIGN_LEFT;
        if (!isLeft) {
            align = TeXConstants.ALIGN_CENTER;
        }
        TeXFormula formula = new TeXFormula(teX);
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(textSize)
                .setWidth(TeXConstants.UNIT_PIXEL, w - margin, align)
                .setIsMaxWidth(isLeft)
                .setInterLineSpacing(TeXConstants.UNIT_PIXEL,
                        AjLatexMath.getLeading(textSize)).build();
        icon.setInsets(new Insets(5, 5, 5, 5));

        Bitmap image = Bitmap.createBitmap(icon.getIconWidth(), icon.getIconHeight(),
                Bitmap.Config.ARGB_4444);

        Canvas g2 = new Canvas(image);
        g2.drawColor(Color.TRANSPARENT);
        icon.paintIcon(g2, 0, 0);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 80, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.flush();
                    fos.close();
                    image.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将字符串转成MD5值
     * @param string 需要转换的字符串
     * @return 字符串的MD5值
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public static void clearData(){
        try {
            FileUtil.delAllFile(latexSavePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LatexCallback{

        void onAnalysisLatex(String content);

        void onError(String errMsg);
    }
}

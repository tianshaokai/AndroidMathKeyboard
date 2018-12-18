package com.tianshaokai.mathkeyboard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BitmapUtil {

    /**
     * 异步加载bitmap，根据屏幕宽高对图片进行缩放，减少内存占用
     * @param context
     * @param filePath
     * @param callback
     */
    public static void asyncLoadBitmap(Context context,final String filePath, final BitmapCallback callback){

        asyncLoadBitmap(filePath,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context),false,callback);
    }

    /**
     * 异步加载bitmap，根据屏幕宽高对图片进行缩放，减少内存占用
     * @param context
     * @param filePath
     * @param callback
     */
    public static void asyncLoadBitmap(Context context,final String filePath,boolean hdMode, final BitmapCallback callback){

        asyncLoadBitmap(filePath,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context),hdMode,callback);
    }

    /**
     * 异步加载bitmap，根据目标宽高对图片进行缩放，减少内存占用
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @param callback
     */
    public static void asyncLoadBitmap(final String filePath, final int reqWidth, final int reqHeight, final boolean hdMode, final BitmapCallback callback){
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> bitmap) throws Exception {
                bitmap.onNext(loadBitmap(filePath,reqWidth,reqHeight,hdMode));
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (null != callback){
                            callback.onLoadBitmap(bitmap);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        if (null != callback){
                            callback.onError(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 异步加载bitmap，根据屏幕宽高对图片进行缩放，减少内存占用
     * @param context
     * @param res
     * @param callback
     */
    public static void asyncLoadBitmap(Context context,int res, final BitmapCallback callback){

        asyncLoadBitmap(context, res,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context), callback);
    }

    /**
     * 异步加载bitmap，根据目标宽高对图片进行缩放，减少内存占用
     * @param res
     * @param reqWidth
     * @param reqHeight
     * @param callback
     */
    public static void asyncLoadBitmap(final Context context,final int res, final int reqWidth, final int reqHeight, final BitmapCallback callback){
        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> bitmap) throws Exception {
                bitmap.onNext(loadBitmap(context,res,reqWidth,reqHeight));
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (null != callback){
                            callback.onLoadBitmap(bitmap);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        if (null != callback){
                            callback.onError(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 加载bitmap，根据屏幕宽高对图片进行缩放，减少内存占用
     * @param filePath
     * @return
     */
    public static Bitmap loadBitmap(Context context,String filePath){

        return loadBitmap(filePath,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context),false);
    }

    /**
     * 加载bitmap，根据屏幕宽高对图片进行缩放，减少内存占用
     * @param filePath
     * @return
     */
    public static Bitmap loadBitmap(Context context,String filePath,boolean hdMode){

        return loadBitmap(filePath,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context),hdMode);
    }

    /**
     * 加载bitmap，根据目标宽高对图片进行缩放，减少内存占用
     * @param filePath
     * @return
     */
    public static Bitmap loadBitmap(String filePath,int reqWidth,int reqHeight,boolean hdMode){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 解析图片的时候，只解析长度和宽度，不载入图片，这样就节省内存开支
        if (hdMode){
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = null;
        if (filePath.matches("data:image.*base64.*")) {
            String base_64_source = filePath.replaceAll("data:image.*base64", "");
            byte[] data = Base64.decode(base_64_source, Base64.DEFAULT);
            // 只加载bitmap宽高数据
            BitmapFactory.decodeByteArray(data, 0, data.length,options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        }else {
            // 只加载bitmap宽高数据
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            // 根据缩放比例加载bitmap，减少内存
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;
    }

    /**
     * 加载bitmap，根据屏幕宽高对图片进行缩放
     * @param context
     * @param res
     * @return
     */
    public static Bitmap loadBitmap(Context context,int res){
        return loadBitmap(context,res,DisplayUtils.getScreenWidth(context),DisplayUtils.getScreenHeight(context));
    }

    /**
     * 加载bitmap，根据目标宽高对图片进行缩放，减少内存占用
     * @param res
     * @return
     */
    public static Bitmap loadBitmap(Context context,int res,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 解析图片的时候，只解析长度和宽度，不载入图片，这样就节省内存开支
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = null;
        BitmapFactory.decodeResource(context.getResources(), res,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        // 根据缩放比例加载bitmap，减少内存
        bitmap = BitmapFactory.decodeResource(context.getResources(), res,options);
        return bitmap;
    }

    /**
     * 根据目标宽高计算缩放比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
//        Log.d("wdcloud","calculateInSampleSize---->缩放前：图片宽:" + width + ",目标宽:" + reqWidth + ";图片高:" + height + ",目标高:" + reqHeight);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
//        Log.d("wdcloud","calculateInSampleSize--->计算宽高后inSampleSize：" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 根据宽高缩放bitmap，会释放原图
     * @param resource
     * @return
     */
    public static Bitmap scaleImage(Bitmap resource,int reqWidth,int reqHeight) {
        return scaleImage(resource, reqWidth, reqHeight,-1,true);
    }

    /**
     * 根据宽高缩放bitmap
     * @param resource
     * @param isRecycleSrc 是否释放原图
     * @return
     */
    public static Bitmap scaleImage(Bitmap resource,int reqWidth,int reqHeight,boolean isRecycleSrc) {
        return scaleImage(resource, reqWidth, reqHeight,-1,isRecycleSrc);
    }

    /**
     * 根据宽高缩放bitmap，可以设置最大缩放值
     * @param resource
     * @return
     */
    public static Bitmap scaleImage(Bitmap resource,int reqWidth,int reqHeight,float maxScale,boolean isRecycleSrc) {
        if (resource == null || resource.isRecycled()){
            return null;
        }
        // 获得图片的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();
//        Log.d("wdcloud","scaleImage---->缩放前：图片宽:" + width + ",目标宽:" + reqWidth + ";图片高:" + height + ",目标高:" + reqHeight);

        float scale = 1.0f;
        if (width > reqWidth && height <= reqHeight) {

            // 图片宽度大于控件宽度，图片高度小于控件高度
            scale = reqWidth * 1.0f / width;

        }else if (height > reqHeight && width <= reqWidth) {

            // 图片高度度大于控件宽高，图片宽度小于控件宽度
            scale = reqHeight * 1.0f / height;

        }else if (height > reqHeight && width > reqWidth) {

            // 图片宽度大于控件宽度，图片高度大于控件高度
            scale = Math.min(reqHeight * 1.0f / height, reqWidth * 1.0f / width);

        }else if (height < reqHeight && width < reqWidth) {

            // 图片宽度小于控件宽度，图片高度小于控件高度
            scale = Math.min(reqHeight * 1.0f / height, reqWidth * 1.0f / width);
        }
        if (maxScale != -1 && scale > maxScale){
            scale = maxScale;
//            Logger.getLogger().d("scaleImage--->缩放值超过最大缩放值：scale："+ scale +"，maxScale："+ maxScale);
        }
//        Log.d("wdcloud","scaleImage--->计算宽高后scale：" + scale);
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, true);
        if (newBitmap.equals(resource)){
            return newBitmap;
        }
        if (isRecycleSrc) {
            resource.recycle();
        }
        return newBitmap;
    }

    /**
     * 旋转图片
     * @param srcBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap srcBitmap,float degrees){
        if (srcBitmap == null || srcBitmap.isRecycled()) {
            return null;
        }
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees,width / 2,height / 2);
        // 旋转
        Bitmap newBM = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, false);
        if (newBM.equals(srcBitmap)) {
            return newBM;
        }
        srcBitmap.recycle();
        return newBM;
    }

    /**
     * 改变Bitmap背景色
     * @param color
     * @param originBitmap
     * @return
     */
    public static Bitmap drawBgBitmap(int color, Bitmap originBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), originBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, originBitmap.getWidth(), originBitmap.getHeight(), paint);
        canvas.drawBitmap(originBitmap, 0, 0, paint);
        return bitmap;
    }

    public interface BitmapCallback{

        void onLoadBitmap(Bitmap bitmap);

        void onError(String errMsg);
    }
}

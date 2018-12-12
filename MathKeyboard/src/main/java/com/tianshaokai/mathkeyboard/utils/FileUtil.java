package com.tianshaokai.mathkeyboard.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件相关的工具类
 */

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static final String PICTURE = "picture";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";

    /****************文件路径 部分********************/

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static void createDir(String path) {
        if (path == null || path.isEmpty()) {
            Log.e("wdedu", "文件夹地址错误：" + path);
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            Log.d("createDir","--->目录：" + path +" 创建结果：" + isSuccess);
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static File createFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            Log.e("wdedu", "文件地址错误：" + filePath);
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                return file;
            } catch (Exception e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 判断文件或文件路径是否存在
     *
     * @param filePath
     * @return
     */
    public static Boolean isAvailable(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            Log.e("wdedu", "文件地址为空");
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            Log.e("wdedu", "文件地址不正确：" + filePath);
            return false;
        }
    }

    /**
     * 获取data/data/packageName绝对路径
     *
     * @param context
     * @return
     */
    public static String getContextFileDir(Context context) {
        if (context == null) {
            Log.e("wdedu", "context值未空");
            return null;
        }
        return context.getFilesDir().getAbsolutePath();
    }

    public static File getSDRootDirectory() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }
    /*********************文件信息 部分********************/


    /**
     * 读取文件全部内容
     * @param file
     * @return
     */
    public static String readFile(File file){
        return readFile(file,-1);
    }

    /**
     * 读取文件指定大小内容，如1024 * 1024
     * @param file  要读取的文件
     * @param readLength 读取的长度，精确到字节
     * @return
     */
    public static String readFile(File file,int readLength){
        String content = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            int byteCount = fis.available();
            if (readLength != -1 && byteCount > readLength){
                byteCount = readLength;
            }
            byte[] buffer = new byte[byteCount];

            fis.read(buffer,0,byteCount);
            content = new String(buffer,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 过滤指定后缀
     * @param extension 小写后缀名，如 .log,.txt
     * @return
     */
    public static FilenameFilter getFileExtensionFilter(final String extension) {
        return new FilenameFilter() {
            public boolean accept(File file, String name) {
                boolean ret = name.toLowerCase().endsWith(extension);
                return ret;
            }
        };
    }

    /**
     * 根据路径获取后缀名
     *
     * @param filePath
     * @return
     */
    @Nullable
    public static String getFileExtension(String filePath) {
        String extension = "";
        if (filePath != null && !filePath.isEmpty()) {
            extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        return extension;
    }


    /**
     * 获取文件路径，通过uri
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String createMediaFilePath(String packagePath, String type) {
        File packageFile = new File(packagePath);
        if (!packageFile.exists()) {
            packageFile.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = packagePath + File.separator;
        switch (type) {
            case PICTURE:
                filePath += "IMG_" + timeStamp + ".png";
                break;
            case AUDIO:
                filePath += "AUDIO_" + timeStamp + ".amr";
                break;
            case VIDEO:
                filePath += "VIDEO_" + timeStamp + ".mp4";
                break;
        }
        return filePath;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveImageToPackagePath(Context context, Bitmap bitmap) {
        String dcimPath = getPackageDCIMPath(context);
        String imagePath = FileUtil.createMediaFilePath(dcimPath, PICTURE);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);// 把数据写入文件
        return imagePath;
    }

    /**
     * 获取相册 图片路径
     *
     * @param context
     * @param uri     如果选择最近相册 则 uri 返回Null
     * @return
     */
    public static String saveImageToPackagePath(Context context, Uri uri) {
        String imagePath = "";    //获得图片的uri
        String[] projection = {"_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        if (cursor.moveToFirst()) {
            imagePath = cursor.getString(column_index);
            Log.e(TAG, imagePath);
        }
        return imagePath;
    }

    /**
     * 获取包内相册路径
     *
     * @param context
     * @return
     */
    public static String getPackageDCIMPath(Context context) {
        if (context == null) {
            return "";
        }
        String dcimPath = "";
        File filePath = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (filePath == null) {
            dcimPath = getStorageDCIMPath();
        } else {
            dcimPath = filePath.getAbsolutePath();
        }
        return dcimPath;
    }

    /**
     * 获取包内声音路径
     *
     * @param context
     * @return
     */
    public static String getPackageAudioPath(Context context) {
        if (context == null) {
            return "";
        }
        String audioPath = "";
        File filePath = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (filePath == null) {
            audioPath = getStorageAudioPath();
        } else {
            audioPath = filePath.getAbsolutePath();
        }
        return audioPath;
    }

    /**
     * 获取包内视频路径
     *
     * @param context
     * @return
     */
    public static String getPackageMoviePath(Context context) {
        if (context == null) {
            return "";
        }
        String moviePath = "";
        File filePath = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (filePath == null) {
            moviePath = getStorageMoviePath();
        } else {
            moviePath = filePath.getAbsolutePath();
        }
        return moviePath;
    }

    /**
     * 获取包内视频路径
     *
     * @param context
     * @return
     */
    public static String getPackageDocumentPath(Context context) {
        if (context == null) {
            return "";
        }
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String documentsPath = "";
        if (isKitKat){
            File filePath = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (filePath == null) {
                documentsPath = getStorageDownloadPath();
            } else {
                documentsPath = filePath.getAbsolutePath();
            }
        }else {
            documentsPath = getStorageDownloadPath();
        }
        return documentsPath;
    }

    /**
     * 获取包内下载路径
     * @param context
     * @return
     */
    public static String getPackageDownloadPath(Context context) {
        if (context == null) {
            return "";
        }
        String downloadPath = "";
        File filePath = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (filePath == null) {
            downloadPath = getStorageDownloadPath();
        } else {
            downloadPath = filePath.getAbsolutePath();
        }
        return downloadPath;
    }

    /**
     * 获取包内crash路径
     *
     * @param context
     * @return
     */
    public static String getPackageCrashPath(Context context) {
        if (context == null) {
            return "";
        }
        String crashPath = "";
        File filePath = context.getApplicationContext().getExternalFilesDir("Crash");
        if (filePath == null) {
            crashPath = getStorageDownloadPath();
        } else {
            crashPath = filePath.getAbsolutePath();
        }
        return crashPath;
    }

    /**
     * 获取外部相册路径
     *
     * @return
     */
    public static String getStorageDCIMPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 获取外部音频路径
     *
     * @return
     */
    public static String getStorageAudioPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取外部视频路径
     *
     * @return
     */
    public static String getStorageMoviePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取外部文档目录
     *
     * @return
     */
    public static String getStorageDocumentPath() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        } else {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }
    }

    /**
     * 获取外部下载目录
     *
     * @return
     */
    public static String getStorageDownloadPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取外部包路径
     *
     * @param context
     * @return
     */
    public static String getExternalPackagePath(Context context) {
        if (context != null) {
            File extPath = Environment.getExternalStorageDirectory();
            String pckPath = extPath.getAbsolutePath() + "/Android/data/" + context.getPackageName();
            return pckPath;
        }
        return "";
    }

    /**
     * 清除缓存
     */
    public static void clearCache(Context context) {
        try {
            delAllFile(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) throws Exception {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) throws Exception {
        delAllFile(folderPath); //删除完里面所有内容
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete(); //删除空文件夹
    }
    //---------------------------------------------------add by fmm -----

    /**
     * 移动文件
     *
     * @param srcFileName 源文件完整路径
     * @param destDirName 目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName) {


        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            try {
                destDir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return srcFile.renameTo(new File(destDirName));
    }

    /**
     * 把byte数组转换为文件
     * @param bfile
     * @param filePath
     * @param fileName
     * @return
     */
    public static File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
}

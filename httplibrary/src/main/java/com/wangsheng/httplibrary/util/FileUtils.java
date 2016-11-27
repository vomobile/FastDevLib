package com.wangsheng.httplibrary.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;


public class FileUtils {

    private static final String LOG_TAG = "FileUtils";

    public static File updateDir = null;
    public static File updateFile = null;
    /***********保存升级APK的目录***********/
    public static final String TlApplication = "TlUpdateApplication";

    public static boolean isCreateFileSucess;
    /**
     * 获取外置卡的目录
     *
     * @param context 上下文
     * @return cachePath
     */
    public static String getExtDir(Context context) {
        if (context == null) {
            return "";
        }
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 挂载了外部存储器
            cachePath = Environment.getExternalStorageDirectory() + File.separator;
        } else {
            cachePath = File.separator;
        }
        return cachePath;
    }

    /**
     * 获取文件路径<br /> eg. /data/data/com.example/files/
     *
     * @param mContext 当前容器
     * @return 文件路径
     */
    public static String getFileDir(Context mContext) {
        if (mContext == null) {
            return "";
        }
        return mContext.getFilesDir().getPath() + File.separator;
    }

    /**
     * 根据文件判断该文件是否存在
     *
     * @param file 文件
     * @return 文件对象
     */
    public static boolean exists(String file) {
        return new File(file).exists();
    }

    /**
     * 保存下载文件
     *
     * @param in
     * @param fileName
     * @return
     */
    public static String saveTempFile(InputStream in, String fileName) {
        //文件存放目录
        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                ConDict.TempDir);
        // 若照片文件存放目录不存在则创建
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(LOG_TAG, "Failed to create directory");
                return null;
            }
        }
        String filepath = mediaStorageDir.getPath() + File.separator + fileName;
        if (new File(filepath).exists()) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            filepath = mediaStorageDir.getPath() + File.separator + "temp" + System.currentTimeMillis() + suffix;
        }
        OutputStream out = null;
        BufferedInputStream bufin = null;
        BufferedOutputStream bufout = null;
        try {
            out = new FileOutputStream(new File(filepath));
//            bufin = new BufferedInputStream(in);
            bufout = new BufferedOutputStream(out);
            byte[] bytes = new byte[102400];
            while (in.read(bytes) != -1) {
                bufout.write(bytes);
            }
            bufout.flush();
            return filepath;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (null != bufin) {
                try {
                    bufin.close();
                } catch (IOException e) {
                }
            }
            if (null != bufout) {
                try {
                    bufout.close();
                } catch (IOException e) {
                }
            }
        }
        return null;

    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static File getAvailableCacheDir(Context context) {
        return context.getCacheDir();
    }

    /**
     * 获取MimeType
     *
     * @param fileUrl
     * @return String
     */
    public static String getMimeType(String fileUrl) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);

        return type;
    }


    /**
     * @param input InputStream类型的输入流
     * @return String
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String readStream(InputStream input)
            throws IOException {
        /*
		 * input.read(bytes):获取bytes的长度的字节数到bytes中， 也就是说read一次读取的字节数为bytes字节数组的长度
         */
        byte[] bytes = new byte[1024];
        StringBuffer buffer = new StringBuffer();
        int n = -1;
        while ((n = input.read(bytes)) != -1) {
            buffer.append(new String(bytes, 0, n, "UTF-8"));
        }
        input.close();
        return buffer.toString();
    }

    /**
     * @param file 文本
     * @return String
     * @throws IOException
     */
    public static byte[] readData(File file)
            throws IOException {
        InputStream in = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        byte[] data = out.toByteArray(); // 得到二进制数据
        out.close();
        in.close();
        return data;
    }


    /**
     * 保存文件
     *
     * @param data   文件数据
     * @param file   文件
     * @param append 是否有签名
     * @throws IOException
     */
    public static void writeData(byte[] data, File file, boolean append)
            throws IOException {
        if (data == null || data.length == 0) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file, append);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 获取Assets中的资源
     *
     * @throws IOException
     */
    public static InputStream getAssets(Context context, String filePath)
            throws IOException {
        return context.getAssets().open(filePath);
    }


    // 以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

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

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                                .valueOf(id));

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
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[]
            selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /*将调试信息保存到sdcard上*/
    public static void put(String s, String name) {
        try {
            FileOutputStream outStream = new FileOutputStream("/sdcard/" + name + ".txt", true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "gb2312");
            writer.write(s);
            writer.write("/n");
            writer.flush();
            writer.close();//记得关闭
            outStream.close();
        } catch (Exception e) {
            Log.e("m", "file write error");
        }
    }

    /**
     * 方法描述：createFile方法
     * @param   app_name
     * @return
     */
    public static void createFile(String app_name) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            isCreateFileSucess = true;
            updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + TlApplication +"/");
            updateFile = new File(updateDir + "/" + app_name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }
        }else{
            isCreateFileSucess = false;
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = is.read(b, 0, 1024)) != -1) {
            baos.write(b, 0, len);
            baos.flush();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * 保存文件
     *
     * @throws IOException
     */
    public static String writeData(byte[] data, String fileName) throws IOException
    {
        //文件存放目录
        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                ConDict.TempDir);
        // 若照片文件存放目录不存在则创建
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(LOG_TAG, "Failed to create directory");
            }
        }
        String filepath = mediaStorageDir.getPath() + File.separator + fileName;
        if (new File(filepath).exists()) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            filepath = mediaStorageDir.getPath() + File.separator + "temp" + System.currentTimeMillis() + suffix;
        }
        if (data == null || data.length == 0)
        {
            return "";
        }
        File file = new File(filepath);
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        if (!file.exists())
        {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        return filepath;
    }
}

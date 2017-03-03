package com.example.imagelrucacherdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Richie on 2017/3/3.
 */

public class FileUtils {

    //sd卡  根目录

    public static String sd_path = Environment.getExternalStorageDirectory().getPath();

    //手机缓存根目录
    public static String mData_path = null;

    //保存image 目录名
    public static final String FOLDER_NAME = "/AndroidImage";

    public FileUtils(Context context) {

        mData_path = context.getCacheDir().getPath();
    }


    //获取储存Image的目录

    private String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? sd_path + FOLDER_NAME : mData_path + FOLDER_NAME;
    }


    //保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录

    public void saveBitmap(String filename, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return;
        }

        String path = getStorageDirectory();
        File folderfile = new File(path);
        if (!folderfile.exists()) {
            folderfile.mkdir();
        }
        File file = new File(path + File.separator + filename);
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

    }


    //从手机或者sd卡获取bitmap

    public Bitmap getBitmap(String fileName) {
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }


    //判断文件是否存在
    public boolean isFileExists(String filename) {
        return new File(getStorageDirectory() + File.separator + filename).exists();
    }


    //获取文件的大小

    public long getFileSize(String filename) {

        return new File(getStorageDirectory() + File.separator + filename).length();
    }


    //删除手机获取sd卡的图片缓存和目录
    public void  delete(){
        File dirFile = new File(getStorageDirectory());
          //如果不存在 则直接返回
        if (!dirFile.exists()){
                return;
            }
        if (dirFile.isDirectory()){
            String[] children = dirFile.list();
            for (int i = 0; i <children.length ; i++) {
                new File(dirFile,children[i]).delete();
            }
        }

        dirFile.delete();
    }


}

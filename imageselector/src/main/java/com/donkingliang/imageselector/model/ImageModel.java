package com.donkingliang.imageselector.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.donkingliang.imageselector.entry.Folder;
import com.donkingliang.imageselector.entry.Image;
import com.donkingliang.imageselector.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageModel {

    /**
     * 从SDCard加载图片
     *
     * @param context
     * @param onlyImage
     * @param callback
     */
    public static void loadImageForSDCard(final Context context,final boolean onlyImage,final DataCallback callback) {
        //由于扫描图片是耗时的操作，所以要在子线程处理。
        new Thread(new Runnable() {
            @Override
            public void run() {
                //扫描图片
                ContentResolver mContentResolver = context.getContentResolver();
//                Cursor  mCursor = mContentResolver.query(MediaStore.Files.getContentUri("external"),
//                        null,
//                        MediaStore.Files.FileColumns.MIME_TYPE + " =? ", new String[]
////                                {"image/jpeg","image/png", "image/webp","video/mp4", "video/ogg","video/webm"}, null);
//                                {"video/mp4"}, null);

                //"image/jpeg", "image/png", "image/webp","application/octet-stream","video/mp4", "video/ogg","video/webm"

//            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            ContentResolver mContentResolver = context.getContentResolver();
//            Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
//                            MediaStore.Images.Media.DATA,
//                            MediaStore.Images.Media.DISPLAY_NAME,
//                            MediaStore.Images.Media.DATE_ADDED,
//                            MediaStore.Images.Media._ID,
//                            MediaStore.Images.Media.MIME_TYPE},
//                    null,
//                    null,
//                    MediaStore.Images.Media.DATE_ADDED);
                ArrayList<Image> images = new ArrayList<>();
                Cursor mCursorImage = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);

                //读取扫描到的图片
                if (mCursorImage != null) {

                    while (mCursorImage.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursorImage.getString(
                                mCursorImage.getColumnIndex(MediaStore.Images.Media.DATA));
                        //获取图片名称
                        String name = mCursorImage.getString(
                                mCursorImage.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        //获取图片时间
                        long time = mCursorImage.getLong(
                                mCursorImage.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                        //获取图片类型
                        String mimeType = mCursorImage.getString(
                                mCursorImage.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));

                        //过滤未下载完成或者不存在的文件
                        if (!"downloading".equals(getExtensionName(path)) && checkImgExists(path)) {
                            images.add(new Image(path, time, name, mimeType));
                        }
                    }
                    mCursorImage.close();
                }

                //只要图片 就不要去查视频了
                if (!onlyImage){
                    Cursor mCursorVideo = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                            MediaStore.Video.Media.DEFAULT_SORT_ORDER);

                    //读取扫描到的视频
                    if (mCursorVideo != null) {
                        while (mCursorVideo.moveToNext()) {
                            // 获取图片的路径
                            String path = mCursorVideo.getString(
                                    mCursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                            //获取图片名称
                            String name = mCursorVideo.getString(
                                    mCursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
                            //获取图片时间
                            long time = mCursorVideo.getLong(
                                    mCursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));

                            //获取图片类型
                            String mimeType = mCursorVideo.getString(
                                    mCursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE));

                            //过滤未下载完成或者不存在的文件
                            if (!"downloading".equals(getExtensionName(path)) && checkImgExists(path)) {
                                images.add(new Image(path, time, name, mimeType));
                            }
                        }
                        mCursorVideo.close();
                    }
                }



                //实现Comparator进行排序  重写 comparator 按照时间排序
                Collections.sort(images, new Comparator<Image>() {
                    @Override
                    public int compare(Image o1, Image o2) {
                        if (((Image) o2).getTime()>((Image) o1).getTime()){
                            return 1;
                        }else if (((Image) o2).getTime()<((Image) o1).getTime()){
                            return  -1;
                        }else{
                            return 0;
                        }
                    }
                });
//                Collections.reverse(images);
                callback.onSuccess(splitFolder(images));
            }
        }).start();
    }

    /**
     * 检查图片是否存在。ContentResolver查询处理的数据有可能文件路径并不存在。
     *
     * @param filePath
     * @return
     */
    private static boolean checkImgExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 把图片按文件夹拆分，第一个文件夹保存所有的图片
     *
     * @param images
     * @return
     */
    private static ArrayList<Folder> splitFolder(ArrayList<Image> images) {
        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(new Folder("全部图片", images));

        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String path = images.get(i).getPath();
                String name = getFolderName(path);
                if (StringUtils.isNotEmptyString(name)) {
                    Folder folder = getFolder(name, folders);
                    folder.addImage(images.get(i));
                }
            }
        }
        return folders;
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 根据图片路径，获取图片文件夹名称
     *
     * @param path
     * @return
     */
    private static String getFolderName(String path) {
        if (StringUtils.isNotEmptyString(path)) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    private static Folder getFolder(String name, List<Folder> folders) {
        if (!folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                Folder folder = folders.get(i);
                if (name.equals(folder.getName())) {
                    return folder;
                }
            }
        }
        Folder newFolder = new Folder(name);
        folders.add(newFolder);
        return newFolder;
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Folder> folders);
    }
}

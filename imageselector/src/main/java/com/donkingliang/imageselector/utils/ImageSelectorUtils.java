package com.donkingliang.imageselector.utils;

import android.app.Activity;

import com.donkingliang.imageselector.ClipImageActivity;
import com.donkingliang.imageselector.ImageSelectorActivity;

import java.util.ArrayList;

/**
 * 提供给外界相册的调用的工具类
 */
@Deprecated
public class ImageSelectorUtils {

    //图片选择的结果
    public static final String SELECT_RESULT = ImageSelector.SELECT_RESULT;

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhoto(Activity activity, int requestCode) {
        openPhoto(activity, requestCode, false,false, 0);
    }

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     * @param onlyImage
     * @param selected    接收从外面传进来的已选择的图片列表。当用户原来已经有选择过图片，现在重新打开
     *                    选择器，允许用户把先前选过的图片传进来，并把这些图片默认为选中状态。
     */
    public static void openPhoto(Activity activity, int requestCode,boolean onlyImage, ArrayList<String> selected) {
        openPhoto(activity, requestCode, false,onlyImage, 0, selected);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param onlyImage      只要图片（不要视频）
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle,boolean onlyImage, int maxSelectCount) {
        openPhoto(activity, requestCode, isSingle,onlyImage, maxSelectCount, null);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param onlyImage      只要图片（不要视频）
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param selected       接收从外面传进来的已选择的图片列表。当用户原来已经有选择过图片，现在重新打开
     *                       选择器，允许用户把先前选过的图片传进来，并把这些图片默认为选中状态。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle,boolean onlyImage, int maxSelectCount, ArrayList<String> selected) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle,onlyImage,true,
                true, maxSelectCount,true,selected);
    }

    /**
     * 打开相册，单选图片并剪裁。
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhotoAndClip(Activity activity, int requestCode) {
        ClipImageActivity.openActivity(activity, requestCode, true, true, null);
    }
}

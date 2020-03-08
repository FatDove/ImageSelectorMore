package com.donkingliang.imageselector.entry.MessageBean;

/**
 * 作者：章鱼哥00 on 2019/8/24.
 * CSDN：翻滚吧章鱼 https://blog.csdn.net/weixin_37558974
 * 邮箱：287651776@qq.com
 * 版本：v2.0
 * <p>
 *
 * 「页面返回 带参」 -- EventBus 使用
 */
public class ActivityResultBean {
    int requestCode;
    int resultCode;
    String photoPath; //图片地址

    public ActivityResultBean(int requestCode, int resultCode, String photoPath) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.photoPath = photoPath;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

package com.donkingliang.imageselector.Video;

/**
 * description: 存储清晰度的数据bean
 * autour: zq
 * date: 2017/9/29 0029 下午 4:36
 * update: 2017/9/29 0029
 * version:
*/
public class SwitchVideoModel {
    private String url;
    private String name;

    public SwitchVideoModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
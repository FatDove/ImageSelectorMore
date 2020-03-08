package com.donkingliang.imageselector.entry;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 图片实体类
 */
public class Image implements Parcelable,Comparable<Image>{

    private String path;
    private long time;
    private String name;
    private String mimeType;

    public Image(String path, long time, String name, String mimeType) {
        this.path = path;
        this.time = time;
        this.name = name;
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isGif() {
        return "image/gif".equals(mimeType);
    }

    public boolean isVideo() {
        if ("video/mp4".equals(mimeType)){
            return true;
        }else if ("video/ogg".equals(mimeType)){
            return true;
        }else if ("video/webm".equals(mimeType)){
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.time);
        dest.writeString(this.name);
        dest.writeString(this.mimeType);
    }

    protected Image(Parcel in) {
        this.path = in.readString();
        this.time = in.readLong();
        this.name = in.readString();
        this.mimeType = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int compareTo(@NonNull Image anther) {


        return 0;
    }
}

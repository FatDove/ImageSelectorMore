package com.fan.updatagithubtest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.donkingliang.imageselector.entry.MessageBean.ActivityResultBean;
import com.donkingliang.imageselector.utils.ImageSelector;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class MainActivity extends AppCompatActivity {

    private Button single;
    private Button more;
    private Button tagging;
    private final int  RequestCode = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_main);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {

        single = (Button) findViewById(R.id.single);
        more = (Button) findViewById(R.id.more);
        tagging = (Button) findViewById(R.id.tag);
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setCrop(true)  // 设置是否使用图片剪切功能。
                        .setSingle(true)  //设置是否单选
                        .onlyImage(true)  //只要图片（不要视频）
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(MainActivity.this, RequestCode); // 打开相册
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
//                        .setCrop(true)  // 设置是否使用图片剪切功能。
//                        .setSingle(false)  //设置是否单选
                        .onlyImage(false)  //只要图片（不要视频）
                        .setMaxSelectCount(9)
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(MainActivity.this, RequestCode); // 打开相册

            }
        });

        tagging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
//                        .setCrop(true)  // 设置是否使用图片剪切功能。
//                        .setSingle(false)  //设置是否单选
                        .onlyImage(false)  //只要图片（不要视频）
                        .isTagging(true)//是否需要标注
                        .setMaxSelectCount(9)
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(MainActivity.this, RequestCode); // 打开相册
            }
        });

    }


    //从 【标注图片】 过来
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void PhotoPath(ActivityResultBean activityResultBean) {
        if (activityResultBean != null) {
            Log.e("activityResultBean", activityResultBean.getPhotoPath() + "???");
            String photoPath = activityResultBean.getPhotoPath();
            ArrayList<String> pathArr = new ArrayList<>();
            pathArr.add(photoPath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //从图片选择器回来
            case RequestCode:
                if (data != null) {
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                    boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
                    boolean isFull = data.getBooleanExtra(ImageSelector.IS_FULL, false);
                    Log.d("isCameraImage", "是否是拍照图片：" + isCameraImage);
                    if (images == null || images.size() <= 0) {
                        Toast.makeText(this, "取消设置", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

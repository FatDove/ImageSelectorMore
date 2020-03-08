package com.fan.updatagithubtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.donkingliang.imageselector.utils.ImageSelector;


public class MainActivity extends AppCompatActivity {

    private Button single;
    private Button more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_main);
//        startActivity(intent);

        initView();
    }

    private void initView() {

        single = (Button) findViewById(R.id.single);
        more = (Button) findViewById(R.id.more);
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setCrop(true)  // 设置是否使用图片剪切功能。
                        .setSingle(true)  //设置是否单选
                        .onlyImage(true)  //只要图片（不要视频）
                        .isTagging(false)//是否需要标注
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(MainActivity.this, 1); // 打开相册
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
                        .start(MainActivity.this, 1); // 打开相册

            }
        });

    }
}

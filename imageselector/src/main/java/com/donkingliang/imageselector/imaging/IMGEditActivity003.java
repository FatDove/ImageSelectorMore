package com.donkingliang.imageselector.imaging;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.donkingliang.imageselector.R;

/**
 * Created by felix on 2017/11/14 下午2:26.
 */

public class IMGEditActivity003 extends AppCompatActivity {

    private static final int MAX_WIDTH = 1024;

    private static final int MAX_HEIGHT = 1024;

    public static final String EXTRA_IMAGE_URI = "IMAGE_URI";

    public static final String EXTRA_IMAGE_SAVE_PATH = "IMAGE_SAVE_PATH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_edit_activity);
    }
}

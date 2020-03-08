package com.donkingliang.imageselector.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.imageselector.R;
import com.donkingliang.imageselector.entry.Image;
import com.donkingliang.imageselector.utils.ImageUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    //    private List<PhotoView> viewList = new ArrayList<>(4);
    List<Image> mImgList;
    private OnItemClickListener mListener;
    private StandardGSYVideoPlayer videoPlayer;

    public ImagePagerAdapter(Context context, List<Image> imgList) {
        this.mContext = context;
//        createImageViews();
        mImgList = imgList;
    }

    //    private void createImageViews() {
//        for (int i = 0; i < 4; i++) {
//            PhotoView imageView = new PhotoView(mContext);
//            imageView.setAdjustViewBounds(true);
//            viewList.add(imageView);
//        }
//    }
    public void releaseVideo() {
        GSYVideoManager.onPause();
        if (videoPlayer != null) {
//            videoPlayer.onVideoPause();
        }
    }

    @Override
    public int getCount() {
        return mImgList == null ? 0 : mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
//        if (object instanceof PhotoView) {
//            PhotoView view = (PhotoView) object;
//            view.setImageDrawable(null);
//            viewList.add(view);
//            container.removeView(view);
//        }
//        if (videoPlayer!=null){
//            videoPlayer.getCurrentPlayer().release();
//        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        final PhotoView currentView = viewList.remove(0);
        final PhotoView currentView = new PhotoView(mContext);
        final Image image = mImgList.get(position);
        container.addView(currentView);
        if (image.isGif()) {
            currentView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(mContext).load(new File(image.getPath()))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(currentView);
        } else {
            if (image.getName().contains(".avi") || image.getName().contains(".mov") || image.getName().contains(".FLV") || image.getName().contains(".3GP") || image.getName().contains(".mp4") || image.getName().contains(".rmvb") || image.getName().contains(".rm")) {
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.sample_video001, null);
                StandardGSYVideoPlayer videoPlayer2 = inflate.findViewById(R.id.simple_player);
                videoPlayer2 = inflate.findViewById(R.id.simple_player);
//                String filePath="/storage/emulated/0/Download/CacheChatVideo/chiye9b5a42a6884268941bfc56ff90ace589.mp4/06007c710442ce5cc640163f323e9170.mp4";
//                videoPlayer2.setUp(filePath, true, "");
                videoPlayer2.setUp(image.getPath(), true, "");
                //增加封面
                ImageView imageView = new ImageView(mContext);
                Glide.with(mContext).load(image.getPath()).into(imageView);
                videoPlayer2.setThumbImageView(imageView);
                //增加title
                container.addView(inflate);//千万别忘记添加到container
                return inflate;
            } else {
                Glide.with(mContext).asBitmap()
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .load(new File(image.getPath())).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int bw = resource.getWidth();
                        int bh = resource.getHeight();
                        if (bw > 8192 || bh > 8192) {
                            Bitmap bitmap = ImageUtil.zoomBitmap(resource, 8192, 8192);
                            setBitmap(currentView, bitmap);
                        } else {
                            setBitmap(currentView, resource);
                        }
                    }
                });
            }
        }
        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position, image);
                }
            }
        });
        return currentView;
    }

    private void setBitmap(PhotoView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        if (bitmap != null) {
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            int vw = imageView.getWidth();
            int vh = imageView.getHeight();
            if (bw != 0 && bh != 0 && vw != 0 && vh != 0) {
                if (1.0f * bh / bw > 1.0f * vh / vw) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    float offset = (1.0f * bh * vw / bw - vh) / 2;
                    adjustOffset(imageView, offset);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Image image);
    }

    private void adjustOffset(PhotoView view, float offset) {
        PhotoViewAttacher attacher = view.getAttacher();
        try {
            Field field = PhotoViewAttacher.class.getDeclaredField("mBaseMatrix");
            field.setAccessible(true);
            Matrix matrix = (Matrix) field.get(attacher);
            matrix.postTranslate(0, offset);
            Method method = PhotoViewAttacher.class.getDeclaredMethod("resetMatrix");
            method.setAccessible(true);
            method.invoke(attacher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

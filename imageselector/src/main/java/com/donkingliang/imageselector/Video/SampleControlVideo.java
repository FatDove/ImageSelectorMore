package com.donkingliang.imageselector.Video;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;

import com.donkingliang.imageselector.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import moe.codeest.enviews.ENDownloadView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * description: 自定义播放控件(不得使用butterKnife)
 * autour: zq
 * date: 2017/9/23 0023 上午 10:40
 * update: 2017/9/23 0023
 * version:
 */

public class SampleControlVideo extends StandardGSYVideoPlayer {

    //记住切换数据源类型
    private int mType = 0;

    private int mTransformSize = 0;

    //数据源
    private int mSourcePosition = 0;
    //切换清晰度
    private TextView mSwitchSize;
    /**
     * 默认倍速
     */
    private float speed = 1;
    /**
     * 存储清晰度的集合
     */
    private List<SwitchVideoModel> mUrlList = new ArrayList<>();
    /**
     * 音频地址
     */
    private String audioUrl, playUrl;

    private String mTypeText = "标准";

    /**
     * 与进度条平行的播放键
     */
    private ImageView bottomPlay;
    /**
     * 切换音频的标记
     */
    private Boolean isAudio = false;// true视频 false音频
    //播放类型
    private Boolean isAudiotype = false; //false视频状态 true音频状态
    private LinearLayout back, switchSizeLayout;
    private SeekBar videoSeekBar;
    private TextView show_text;
    private RadioGroup radioGroup;


    /**
     * 存放当前可播放课程的地址集合
     */
    private List<CourseListData> courseDataList = new ArrayList<>();
    String playType;//local本地 onLine在线


    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public SampleControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleControlVideo(Context context) {
        super(context);
    }

    public SampleControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void init(Context context) {
        super.init(context);
        this.mContext = context;
        initView();
//****************************************************************************************************
//****************************************************************************************************
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
//****************************************************************************************************
//****************************************************************************************************
    }

    /**
     * 传入播放器布局
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.sample_video;
    }

    private void initView() {
        mSwitchSize = (TextView) findViewById(R.id.switchSize);
        bottomPlay = (ImageView) findViewById(R.id.course_detail_play);
        back = (LinearLayout) findViewById(R.id.back_lin);
        videoSeekBar = (SeekBar) findViewById(R.id.progress);
        show_text = (TextView) findViewById(R.id.show_text);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        switchSizeLayout = (LinearLayout) findViewById(R.id.switchSize_layout);//选择标清高清布局

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        initListener();
    }
    public void gotoPlay(String type) { //local本地
        playType = type;
        if ("local".equals(type)) {
            mSwitchSize.setVisibility(GONE);
            getStartButton().setVisibility(GONE);
            clickStartIcon();
            //点击播放后隐藏播放按钮
            getStartButton().setVisibility(INVISIBLE);
        } else {
            mSwitchSize.setVisibility(VISIBLE);
            getStartButton().setVisibility(VISIBLE);
        }
    }
    //隐藏 ‘标清’ 按钮
    public void visiSwitchSize(boolean isVisi) {
        if (isVisi) {
            mSwitchSize.setVisibility(VISIBLE);
        } else {
            mSwitchSize.setVisibility(GONE);
        }
    }


    @Override
    protected void showWifiDialog() {
        if ("local".equals(playType)) {
            startPlayLogic();
            mSwitchSize.setVisibility(GONE);
        } else {
            super.showWifiDialog();
            mSwitchSize.setVisibility(VISIBLE);
        }
    }


    private void initListener() {
        //切换视频清晰度
        mSwitchSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchSizeLayout.getVisibility() == GONE) {
                    switchSizeLayout.setVisibility(VISIBLE);
                } else {
                    switchSizeLayout.setVisibility(GONE);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switchSizeLayout.setVisibility(GONE);
                //标清
                if (i == R.id.low_btn) {
                    showSwitch(0);
                    //高清
                } else if (i == R.id.high_btn) {
                    showSwitch(1);
                }
            }
        });
        //底部播放按钮的点击事件
        bottomPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHadPlay) {
                    return;
                }
                clickStartIcon();
            }
        });
//****************************************************************************************************
//****************************************************************************************************
        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
//                Log.i("ceshiseekBar", String.valueOf(GSYVideoManager.instance().getMediaPlayer().getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                    if (isIfCurrentIsFullscreen()) {
                        Debuger.printfLog("onClickSeekbarFullscreen");
                        mVideoAllCallBack.onClickSeekbarFullscreen(mOriginUrl, mTitle, this);
                    } else {
                        Debuger.printfLog("onClickSeekbar");
                        mVideoAllCallBack.onClickSeekbar(mOriginUrl, mTitle, this);
                    }
                }
                if (GSYVideoManager.instance().getMediaPlayer() != null && mHadPlay) {
                    try {
                        int time = seekBar.getProgress() * getDuration() / 100;

                        GSYVideoManager.instance().getMediaPlayer().seekTo(time);
                    } catch (Exception e) {
                        Debuger.printfWarning(e.toString());
                    }
                }
            }
        });
//****************************************************************************************************
//****************************************************************************************************
    }
    public void videoPause() {
        clickStartIcon();
    }


    /**
     * 播放的时候改变UI的状态
     */
    @Override
    protected void changeUiToPlayingShow() {
        Debuger.printfLog("changeUiToPlayingShow");
        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        //隐藏原本播放器按钮
        setViewShowState(mStartButton, GONE);
        setViewShowState(mLoadingProgressBar, GONE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).setVisibility(View.GONE);
        }
        updateStartImage();
        //隐藏我自定义的播放按钮
        getStartButton().setVisibility(View.GONE);
        //底部播放键设置为暂停背景
        bottomPlay.setBackgroundResource(R.mipmap.videoplayer_bottom_pause);
    }


    @Override
    protected void changeUiToCompleteShow() {
        Debuger.printfLog("changeUiToCompleteShow");

        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);
        //隐藏我自定义的播放按钮
        getStartButton().setVisibility(View.VISIBLE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        Log.i("lalala", "不知道changeUiToCompleteShow");
        updateStartImage();
    }

    /**
     * 暂停时候改变的UI状态
     */
    @Override
    protected void changeUiToPauseShow() {
        Debuger.printfLog("changeUiToPauseShow");

        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
//        隐藏原本播放器按钮
        setViewShowState(mStartButton, GONE);
        setViewShowState(mLoadingProgressBar, GONE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).setVisibility(View.GONE);
        }
        updateStartImage();
        updatePauseCover();
        getStartButton().setVisibility(View.VISIBLE);
        //底部播放键设置为播放背景
        bottomPlay.setBackgroundResource(R.mipmap.videoplayer_bottom_play);
    }


    /**
     * 需要在尺寸发生变化的时候重新处理
     */


    @Override
    protected void changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear();
        Log.i("lala", "动1了");
    }

    @Override
    protected void changeUiToNormal() {
        Debuger.printfLog("changeUiToNormal");
        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, VISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);
        updateStartImage();
        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }


    /**
     * 处理镜像旋转
     * 注意，暂停时
     */
    protected void resolveTransform() {
        switch (mTransformSize) {
            case 1: {
                Matrix transform = new Matrix();
                transform.setScale(-1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);

                mTextureView.invalidate();
            }
            break;
            case 2: {
                Matrix transform = new Matrix();
                transform.setScale(1, -1, 0, mTextureView.getHeight() / 2);
                mTextureView.setTransform(transform);

                mTextureView.invalidate();
            }
            break;
            case 0: {
                Matrix transform = new Matrix();
                transform.setScale(1, 1, mTextureView.getWidth() / 2, 0);
                mTextureView.setTransform(transform);

                mTextureView.invalidate();
            }
            break;
        }
    }


    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     *
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        SampleControlVideo sampleVideo = (SampleControlVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        sampleVideo.mSourcePosition = mSourcePosition;
        sampleVideo.mType = mType;
        sampleVideo.mTransformSize = mTransformSize;
        //加的
        sampleVideo.mUrlList = mUrlList;
        sampleVideo.mTypeText = mTypeText;
        sampleVideo.audioUrl = audioUrl;
        sampleVideo.courseDataList = courseDataList;
        sampleVideo.isAudio = isAudio;
        sampleVideo.isAudiotype = isAudiotype;
        //sampleVideo.resolveTransform();
        sampleVideo.resolveTypeUI();
        //sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return sampleVideo;
    }

    /**
     * 退出全屏时将对应处理参数逻辑返回给非播放器
     *
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        //退出全屏时时显示音频播放按钮
        if (gsyVideoPlayer != null) {
            SampleControlVideo sampleVideo = (SampleControlVideo) gsyVideoPlayer;
            mSourcePosition = sampleVideo.mSourcePosition;
            mType = sampleVideo.mType;
            mTransformSize = sampleVideo.mTransformSize;
            //新加的
            mTypeText = sampleVideo.mTypeText;
            audioUrl = sampleVideo.audioUrl;
            courseDataList = sampleVideo.courseDataList;
            isAudio = sampleVideo.isAudio;
            isAudiotype = sampleVideo.isAudiotype;
            sampleVideo.resolveTypeUI();
        }
    }

    /**
     * 处理显示逻辑
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureAvailable(surface, width, height);
        resolveRotateUI();
//        resolveTransform();
    }

    /**
     * 旋转逻辑
     */
    private void resolveRotateUI() {
        if (!mHadPlay) {
            return;
        }
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (!mHadPlay) {
            return;
        }
        if (mType == 1) {

            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
        } else if (mType == 2) {

            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
        } else if (mType == 3) {

            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        } else if (mType == 4) {

            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        } else if (mType == 0) {

            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        }
        changeTextureViewShowType();
        if (mTextureView != null)
            mTextureView.requestLayout();
        mSwitchSize.setText(mTypeText);

        if ("local".equals(playType)) {
            mSwitchSize.setVisibility(GONE);
            getStartButton().setVisibility(GONE);

        }
    }

    /**
     * 设置播放清晰度集合
     *
     * @param url 播放url
     * @return
     */
    public void setTypeList(List<SwitchVideoModel> url) {
        mUrlList = url;
    }

    /**
     * 设置播放音频
     *
     * @param url 播放url
     * @return
     */
    public void setAudioUrl(String url) {
        audioUrl = url;
    }

    /**
     * 设置播放的数据集合
     *
     * @param data
     */
    public void setCourseList(List<CourseListData> data) {
        this.courseDataList.clear();
        this.courseDataList.addAll(data);
    }

    /**
     * 弹出切换清晰度
     */
    private void showSwitch(int i) {
        if (!mHadPlay) {
            return;
        }
        final String name = mUrlList.get(i).getName();
        if (mSourcePosition != i) {
            if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                    || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)
                    && GSYVideoManager.instance().getMediaPlayer() != null) {
                final String url = mUrlList.get(i).getUrl();
                onVideoPause();
                final long currentPosition = mCurrentPosition;
                GSYVideoManager.instance().releaseMediaPlayer();
                cancelProgressTimer();
                hideAllWidget();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUp(url, mCache, mCachePath, mTitle);
                        setSeekOnStart(currentPosition);
                        startPlayLogic();
                        cancelProgressTimer();
                        hideAllWidget();
                    }
                }, 500);
                mTypeText = name;
                mSwitchSize.setText(name);
                mSourcePosition = i;
            }
        } else {
            Toast.makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 点击切换音频的回调
     */
    public interface AudioClickListener {
        void onClick(View view, int position);
    }

//****************************************************************************************************
//****************************************************************************************************

    /**
     * 触摸显示滑动进度dialog，如需要自定义继承重写即可，记得重写dismissProgressDialog
     */
    @Override
    @SuppressWarnings("ResourceType")
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getActivityContext()).inflate(com.shuyu.gsyvideoplayer.R.layout.video_progress_dialog, null);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(com.shuyu.gsyvideoplayer.R.id.duration_progressbar));
            if (mDialogProgressBarDrawable != null) {
                mDialogProgressBar.setProgressDrawable(mDialogProgressBarDrawable);
            }
            mDialogSeekTime = ((TextView) localView.findViewById(com.shuyu.gsyvideoplayer.R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(com.shuyu.gsyvideoplayer.R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(com.shuyu.gsyvideoplayer.R.id.duration_image_tip));
            mProgressDialog = new Dialog(getActivityContext(), com.shuyu.gsyvideoplayer.R.style.video_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(getWidth(), getHeight());
            if (mDialogProgressNormalColor != -11) {
                mDialogTotalTime.setTextColor(mDialogProgressNormalColor);
            }
            if (mDialogProgressHighLightColor != -11) {
                mDialogSeekTime.setTextColor(mDialogProgressHighLightColor);
            }
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.TOP;
            localLayoutParams.width = getWidth();
            localLayoutParams.height = getHeight();
            int location[] = new int[2];
            getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        if (totalTimeDuration > 0)
            mDialogProgressBar.setProgress(seekTimePosition * 100 / totalTimeDuration);
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(com.shuyu.gsyvideoplayer.R.drawable.video_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(com.shuyu.gsyvideoplayer.R.drawable.video_backward_icon);
        }

    }

    //****************************************************************************************************
//****************************************************************************************************

    @Override
    protected void changeUiToError() {
        super.changeUiToError();

    }

    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();


    }

    /**
     * 自定义控件点击自己消失
     */
    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();

    }

    @Override
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();

    }

    /**
     * 自定义控件过几分钟自己消失
     */
    @Override
    protected void hideAllWidget() {
        super.hideAllWidget();

    }

    @Override
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}

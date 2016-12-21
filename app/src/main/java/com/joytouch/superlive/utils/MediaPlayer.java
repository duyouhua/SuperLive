package com.joytouch.superlive.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.AudioPlayer;
import com.pili.pldroid.player.PlayerCode;
import com.pili.pldroid.player.widget.VideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by yj on 2016/4/25.
 * 视屏播放器
 */
public class MediaPlayer implements IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnPreparedListener
                            , android.media.MediaPlayer.OnBufferingUpdateListener{
    private AudioPlayer mAudioPlayer;
    private View palyer;
    private VideoView mVideoView;
    private ImageView logo;
    private TextView progress;
    private LinearLayout progressll;
    private ImageView bg;
    private long mLastPosition;


    public MediaPlayer(View palyer,Context context) {
        this.palyer = palyer;
        mVideoView = (VideoView) palyer.findViewById(R.id.mvideoview);
        bg  = (ImageView) palyer.findViewById(R.id.player);
        logo = (ImageView) palyer.findViewById(R.id.logo);
        progress = (TextView) palyer.findViewById(R.id.excption_content);
        progressll = (LinearLayout) palyer.findViewById(R.id.logo_ll);

        progressll.setVisibility(View.GONE);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnPreparedListener(this);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0); // 设置解码方式1是硬解码，0是软解码
            options.setInteger(AVOptions.KEY_BUFFER_TIME, 1000); //
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000); // 设置超时时间
            options.setString(AVOptions.KEY_FFLAGS, AVOptions.VALUE_FFLAGS_NOBUFFER); // "nobuffer"
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);//是否为在线直播

        mVideoView.setAVOptions(options);
    }
    //
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        /*
        * 播放结束后，会回调该接口，有以下注意事项：

        如果是播放文件，则是播放到文件结束后产生回调
        如果是在线直播，则会在获取数据超时后产生回调，回调前会先播放完已缓冲的数据
        如果播放过程中产生onError，并且没有处理的话，最后也会回调本接口
        如果播放前设置了 setLooping(true)，则播放结束后会自动重新开始，不会回调本接口
        * */
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {

        if (what == -10000) {
            endAnimation();
            switch (extra) {
                case PlayerCode.EXTRA_CODE_404_NOT_FOUND://没有发现视屏源
                    bg.setVisibility(View.VISIBLE);
                    progress.setText("不能发现视屏源！");
                    break;
                case PlayerCode.EXTRA_CODE_CONNECTION_REFUSED://连接拒绝
                    bg.setVisibility(View.VISIBLE);
                    progress.setText("服务器连接异常！");
                    break;
                case PlayerCode.EXTRA_CODE_CONNECTION_TIMEOUT://连接超时
                    progress.setText("请求视屏超时！");
                    break;
                case PlayerCode.EXTRA_CODE_EMPTY_PLAYLIST://空的播放列表
                    progress.setText("没有视屏源！");
                    break;
                case PlayerCode.EXTRA_CODE_INVALID_URI://无效的连接
                    progress.setText("视屏源无效！");
                    break;
                case PlayerCode.EXTRA_CODE_IO_ERROR://网路异常
                    progress.setText("网络异常，请检查网络！");
                    break;
                case PlayerCode.EXTRA_CODE_STREAM_DISCONNECTED://连接断开
                    progress.setText("网络断开，请检查网络！");
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                progressll.setVisibility(View.VISIBLE);
                startAnimation();
                progress.setText("正在加载0%");
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END://停止缓冲
                progressll.setVisibility(View.GONE);
                endAnimation();
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        //当播放器准备就绪后，会回调该接口
            progressll.setVisibility(View.VISIBLE);

    }

    public void onStart() {
        if (mVideoView != null && mLastPosition != 0) {
            mVideoView.seekTo(mLastPosition);
            mVideoView.start();
        }
    }

    public void onStop() {
        if (mVideoView != null) {
            mLastPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    public void player(String source){
        mVideoView.setVideoPath(source);
        mVideoView.start();
    }
    //开启动画
    private void startAnimation(){
        RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setInterpolator(new LinearInterpolator());//不停顿
        an.setRepeatCount(-1);//重复次数
        an.setFillAfter(true);//停在最后
        an.setDuration(600);
        logo.startAnimation(an);
    }
    //关闭动画
    private void endAnimation(){
        logo.clearAnimation();
    }

    @Override
    public void onBufferingUpdate(android.media.MediaPlayer mediaPlayer, int i) {
        progress.setText("正在加载"+i+"%");
    }

    private interface ErroListener{
        void onErro();
    }
}

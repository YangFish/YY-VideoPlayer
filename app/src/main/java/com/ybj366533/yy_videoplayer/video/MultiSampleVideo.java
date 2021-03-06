package com.ybj366533.yy_videoplayer.video;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;

//import com.danikula.videocache.HttpProxyCacheServer;
//
import com.ybj366533.videoplayer.utils.Debuger;
import com.ybj366533.videoplayer.video.base.MiGuVideoViewBridge;
import com.ybj366533.yy_videoplayer.video.manager.CustomManager;

import java.io.File;

import tv.danmaku.ijk.media.player.IjkLibLoader;

/**
 * 多个同时播放的播放控件
 * Created by guoshuyu on 2018/1/31.
 */

public class MultiSampleVideo extends SampleCoverVideo {

    private final static String TAG = "MultiSampleVideo";

    public MultiSampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MultiSampleVideo(Context context) {
        super(context);
    }

    public MultiSampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        post(new Runnable() {
                            @Override
                            public void run() {
                                //todo 判断如果不是外界造成的就不处理
                            }
                        });
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        break;
                }
            }
        };
    }

    @Override
    public void setIjkLibLoader(IjkLibLoader libLoader) {

    }

    @Override
    public MiGuVideoViewBridge getGSYVideoManager() {
        return CustomManager.getCustomManager(getKey());
    }

    @Override
    protected boolean backFromFull(Context context) {
        return CustomManager.backFromWindowFull(context, getKey());
    }

    @Override
    protected void releaseVideos() {
        CustomManager.releaseAllVideos(getKey());
    }

//    @Override
//    protected HttpProxyCacheServer getProxy(Context context, File file) {
//        return null;
//    }
//


    @Override
    protected int getFullId() {
        return CustomManager.FULLSCREEN_ID;
    }

    @Override
    protected int getSmallId() {
        return CustomManager.SMALL_ID;
    }

    public String getKey() {
        if (mPlayPosition == -22) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayPosition never set. ********");
        }
        if (TextUtils.isEmpty(mPlayTag)) {
            Debuger.printfError(getClass().getSimpleName() + " used getKey() " + "******* PlayTag never set. ********");
        }
        return TAG + mPlayPosition + mPlayTag;
    }
}

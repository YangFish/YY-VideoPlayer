package com.ybj366533.videoplayer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

//import com.danikula.videocache.HttpProxyCacheServer;
import com.ybj366533.videoplayer.listener.MediaPlayerListener;
import com.ybj366533.videoplayer.utils.CommonUtil;
import com.ybj366533.videoplayer.video.base.BaseVideoPlayer;

import java.io.File;

import tv.danmaku.ijk.media.player.IjkLibLoader;


/**
 * 视频管理，单例
 */

public class VideoManager extends VideoBaseManager {

    public static final int SMALL_ID = R.id.small_id;

    public static final int FULLSCREEN_ID = R.id.full_id;

    public static String TAG = "VideoManager";

    @SuppressLint("StaticFieldLeak")
    private static VideoManager videoManager;

    /***
     * @param libLoader 是否使用外部动态加载so
     * */
    private VideoManager(IjkLibLoader libLoader) {
        init(libLoader);
    }

    /**
     * 单例管理器
     */
    public static synchronized VideoManager instance() {
        if (videoManager == null) {
            videoManager = new VideoManager(ijkLibLoader);
        }
        return videoManager;
    }

    /**
     * 同步创建一个临时管理器
     */
    public static synchronized VideoManager tmpInstance(MediaPlayerListener listener) {
        VideoManager gsyVideoManager = new VideoManager(ijkLibLoader);
        gsyVideoManager.buffterPoint = videoManager.buffterPoint;
        gsyVideoManager.optionModelList = videoManager.optionModelList;
        gsyVideoManager.cacheFile = videoManager.cacheFile;
        gsyVideoManager.playTag = videoManager.playTag;
        gsyVideoManager.mMapHeadData = videoManager.mMapHeadData;
        gsyVideoManager.currentVideoWidth = videoManager.currentVideoWidth;
        gsyVideoManager.currentVideoHeight = videoManager.currentVideoHeight;
        gsyVideoManager.context = videoManager.context;
        gsyVideoManager.lastState = videoManager.lastState;
        gsyVideoManager.playPosition = videoManager.playPosition;
        gsyVideoManager.timeOut = videoManager.timeOut;
        gsyVideoManager.videoType = videoManager.videoType;
        gsyVideoManager.needMute = videoManager.needMute;
        gsyVideoManager.needTimeOutOther = videoManager.needTimeOutOther;
        gsyVideoManager.setListener(listener);
        return gsyVideoManager;
    }

    /**
     * 替换管理器
     */
    public static synchronized void changeManager(VideoManager gsyVideoManager) {
        videoManager = gsyVideoManager;
    }


    /**
     * 获取缓存代理服务
     */
//    protected static HttpProxyCacheServer getProxy(Context context) {
//        HttpProxyCacheServer proxy = VideoManager.instance().proxy;
//        return proxy == null ? (VideoManager.instance().proxy =
//                VideoManager.instance().newProxy(context)) : proxy;
//    }


    /**
     * 获取缓存代理服务,带文件目录的
     */
//    public static HttpProxyCacheServer getProxy(Context context, File file) {
//
//        //如果为空，返回默认的
//        if (file == null) {
//            return getProxy(context);
//        }
//
//        //如果已经有缓存文件路径，那么判断缓存文件路径是否一致
//        if (VideoManager.instance().cacheFile != null
//                && !VideoManager.instance().cacheFile.getAbsolutePath().equals(file.getAbsolutePath())) {
//            //不一致先关了旧的
//            HttpProxyCacheServer proxy = VideoManager.instance().proxy;
//
//            if (proxy != null) {
//                proxy.shutdown();
//            }
//            //开启新的
//            return (VideoManager.instance().proxy =
//                    VideoManager.instance().newProxy(context, file));
//        } else {
//            //还没有缓存文件的或者一致的，返回原来
//            HttpProxyCacheServer proxy = VideoManager.instance().proxy;
//
//            return proxy == null ? (VideoManager.instance().proxy =
//                    VideoManager.instance().newProxy(context, file)) : proxy;
//        }
//    }

    /**
     * 退出全屏，主要用于返回键
     *
     * @return 返回是否全屏
     */
    @SuppressWarnings("ResourceType")
    public static boolean backFromWindowFull(Context context) {
        boolean backFrom = false;
        ViewGroup vp = (ViewGroup) (CommonUtil.scanForActivity(context)).findViewById(Window.ID_ANDROID_CONTENT);
        View oldF = vp.findViewById(FULLSCREEN_ID);
        if (oldF != null) {
            backFrom = true;
            CommonUtil.hideNavKey(context);
            if (VideoManager.instance().lastListener() != null) {
                VideoManager.instance().lastListener().onBackFullscreen();
            }
        }
        return backFrom;
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public static void releaseAllVideos() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onCompletion();
        }
        VideoManager.instance().releaseMediaPlayer();
    }


    /**
     * 暂停播放
     */
    public static void onPause() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onVideoPause();
        }
    }

    /**
     * 恢复播放
     */
    public static void onResume() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onVideoResume();
        }
    }


    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作,直播设置为false
     */
    public static void onResume(boolean seek) {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onVideoResume(seek);
        }
    }

    /**
     * 当前是否全屏状态
     *
     * @return 当前是否全屏状态， true代表是。
     */
    @SuppressWarnings("ResourceType")
    public static boolean isFullState(Activity activity) {
        ViewGroup vp = (ViewGroup) (CommonUtil.scanForActivity(activity)).findViewById(Window.ID_ANDROID_CONTENT);
        final View full = vp.findViewById(FULLSCREEN_ID);
        BaseVideoPlayer gsyVideoPlayer = null;
        if (full != null) {
            gsyVideoPlayer = (BaseVideoPlayer) full;
        }
        return gsyVideoPlayer != null;
    }

}
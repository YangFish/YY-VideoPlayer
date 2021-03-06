package com.ybj366533.videoplayer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

//import com.danikula.videocache.HttpProxyCacheServer;
import com.ybj366533.videoplayer.utils.CommonUtil;
import com.ybj366533.videoplayer.video.base.BaseVideoPlayer;

import java.io.File;

import tv.danmaku.ijk.media.player.IjkLibLoader;

import static com.ybj366533.videoplayer.utils.CommonUtil.hideNavKey;

/**
 * 视频管理，单例
 */

public class VideoADManager extends VideoBaseManager {

    public static final int SMALL_ID = R.id.ad_small_id;

    public static final int FULLSCREEN_ID = R.id.ad_full_id;

    public static String TAG = "VideoADManager";

    @SuppressLint("StaticFieldLeak")
    private static VideoADManager videoManager;

    //单例模式实在不好给instance()加参数，还是直接设为静态变量吧
    //自定义so包加载类
    private static IjkLibLoader ijkLibLoader;

    /***
     * @param libLoader 是否使用外部动态加载so
     * */
    private VideoADManager(IjkLibLoader libLoader) {
        ijkLibLoader = libLoader;
        init(libLoader);
    }


    /**
     * 获取缓存代理服务
     */
//    protected static HttpProxyCacheServer getProxy(Context context) {
//        HttpProxyCacheServer proxy = VideoADManager.instance().proxy;
//        return proxy == null ? (VideoADManager.instance().proxy =
//                VideoADManager.instance().newProxy(context)) : proxy;
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
//        if (VideoADManager.instance().cacheFile != null
//                && !VideoADManager.instance().cacheFile.getAbsolutePath().equals(file.getAbsolutePath())) {
//            //不一致先关了旧的
//            HttpProxyCacheServer proxy = VideoADManager.instance().proxy;
//
//            if (proxy != null) {
//                proxy.shutdown();
//            }
//            //开启新的
//            return (VideoADManager.instance().proxy =
//                    VideoADManager.instance().newProxy(context, file));
//        } else {
//            //还没有缓存文件的或者一致的，返回原来
//            HttpProxyCacheServer proxy = VideoADManager.instance().proxy;
//
//            return proxy == null ? (VideoADManager.instance().proxy =
//                    VideoADManager.instance().newProxy(context, file)) : proxy;
//        }
//    }


    /**
     * 单例管理器
     */
    public static synchronized VideoADManager instance() {
        if (videoManager == null) {
            videoManager = new VideoADManager(ijkLibLoader);
        }
        return videoManager;
    }

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
            hideNavKey(context);
            if (VideoADManager.instance().lastListener() != null) {
                VideoADManager.instance().lastListener().onBackFullscreen();
            }
        }
        return backFrom;
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public static void releaseAllVideos() {
        if (VideoADManager.instance().listener() != null) {
            VideoADManager.instance().listener().onCompletion();
        }
        VideoADManager.instance().releaseMediaPlayer();
    }


    /**
     * 暂停播放
     */
    public static void onPause() {
        if (VideoADManager.instance().listener() != null) {
            VideoADManager.instance().listener().onVideoPause();
        }
    }

    /**
     * 恢复播放
     */
    public static void onResume() {
        if (VideoADManager.instance().listener() != null) {
            VideoADManager.instance().listener().onVideoResume();
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
        BaseVideoPlayer videoPlayer = null;
        if (full != null) {
            videoPlayer = (BaseVideoPlayer) full;
        }
        return videoPlayer != null;
    }
}
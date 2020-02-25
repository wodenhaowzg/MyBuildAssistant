package com.azx.mybuildassistant.santiyun;

import java.io.File;

public abstract class SanTiYunBaseTask extends BaseTask {

    protected static final String CMD_STOP_FLAG = "FINISH";

    // 三体云代码仓库路径
    protected static final String SANTIYUN_PATH = MACHINE_PATH + "/Downloads/Learns/Guo_Company_Svn";
    // 三体云 SDK 路径
    protected static final String SANTIYUN_SDK_PATH = "/GitLab/3TClient/SDK";
    // SDK 工程路径
    protected static final String STAND_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + "/Demo/android/WS_ANDROID_MOMODemo";

    protected static final String OLD_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + "/Demo/android/WS_ANDROID_DEMO";

    protected static final String QUANMIN_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + "/Demo/android/QUANMIN_LIVE_DEMO";

    protected static final String ENTERCONFAPI_MODULE_NAME = "enterconfapi";
    protected static final String ENTERCONFAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + ENTERCONFAPI_MODULE_NAME;

    protected static final String MYAUDIO_MODULE_NAME = "myaudio";
    protected static final String MYAUDIO_MODULE_PATH = OLD_SDK_PROJECT_PATH + File.separator + MYAUDIO_MODULE_NAME;

    protected static final String MYVIDEO_MODULE_NAME = "myvideo";
    protected static final String MYVIDEO_MODULE_PATH = OLD_SDK_PROJECT_PATH + File.separator + MYVIDEO_MODULE_NAME;

    protected static final String WSTECHAPI_MODULE_NAME = "wstechapi";
    protected static final String WSTECHAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + WSTECHAPI_MODULE_NAME;

    // Github
    protected static final String GITHUB_PATH = SANTIYUN_PATH + "/GitHub";

    protected static final String LIVE_PROJECT_PATH = GITHUB_PATH + File.separator + "Android-Live" + File.separator + "app";

    protected static final String VIDEO_CHAT_PROJECT_PATH = GITHUB_PATH + File.separator + "VideoChat" + File.separator + "app";

    protected static final String AUDIO_CHAT_PROJECT_PATH = GITHUB_PATH + File.separator + "AudioChat" + File.separator + "app";
}

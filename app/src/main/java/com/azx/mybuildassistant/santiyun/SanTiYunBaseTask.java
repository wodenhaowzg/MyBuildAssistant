package com.azx.mybuildassistant.santiyun;

import java.io.File;

public abstract class SanTiYunBaseTask extends BaseTask {

    // 三体云代码仓库路径
    private static final String SANTIYUN_PATH = MACHINE_PATH + File.separator + "Downloads" + File.separator +
            "Learns" + File.separator +
            "Guo_Company_Svn";

    // 三体云在github上的Demo的存放路径
    private static final String GITHUB_PATH = SANTIYUN_PATH + File.separator + "GitHub";

    // 三体云 SDK 路径
    private static final String SANTIYUN_SDK_PATH = File.separator + "GitLab" + File.separator +
            "3TClient" + File.separator +
            "SDK";
    // SDK 工程路径
    protected static final String STAND_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + File.separator
            + "Demo" + File.separator
            + "android" + File.separator
            + "WS_ANDROID_MOMODemo";
    // MOMO 使用的 SDK 工程路径
    private static final String OLD_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + File.separator + "Demo" + File.separator +
            "android" + File.separator +
            "WS_ANDROID_DEMO";
    // 全民分支使用的 SDK 工程路径
    protected static final String QUANMIN_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + File.separator + "Demo" + File.separator +
            "android" + File.separator +
            "QUANMIN_LIVE_DEMO";

    // SDK 中的 enterconfapi 模块
    private static final String ENTERCONFAPI_MODULE_NAME = "enterconfapi";
    protected static final String ENTERCONFAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + ENTERCONFAPI_MODULE_NAME;
    // SDK 中的 myaudio 模块
    private static final String MYAUDIO_MODULE_NAME = "myaudio";
    protected static final String MYAUDIO_MODULE_PATH = OLD_SDK_PROJECT_PATH + File.separator + MYAUDIO_MODULE_NAME;
    // SDK 中的 myvideo 模块
    private static final String MYVIDEO_MODULE_NAME = "myvideo";
    protected static final String MYVIDEO_MODULE_PATH = OLD_SDK_PROJECT_PATH + File.separator + MYVIDEO_MODULE_NAME;
    // SDK 中的 wstechapi 模块
    private static final String WSTECHAPI_MODULE_NAME = "wstechapi";
    protected static final String WSTECHAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + WSTECHAPI_MODULE_NAME;

    // 标准版 SDK 工程所在的 Demo
    static final String STAND_SDK_APP_PROJECT_PATH = STAND_SDK_PROJECT_PATH + File.separator + "app";
    // 测试入会Demo
    static final String TEST_SDK_PROJECT_PATH = SANTIYUN_PATH + SANTIYUN_SDK_PATH + File.separator + "Demo" + File.separator + "android" + File.separator + "TTT_ANDROID_TEST_DEMO" + File.separator + "app";
    // 连麦直播Demo
    static final String LIVE_PROJECT_PATH = GITHUB_PATH + File.separator + "Android-Live" + File.separator + "app";
    // 视频通话Demo
    static final String VIDEO_CHAT_PROJECT_PATH = GITHUB_PATH + File.separator + "VideoChat" + File.separator + "app";
    // 音频通话Demo
    static final String AUDIO_CHAT_PROJECT_PATH = GITHUB_PATH + File.separator + "AudioChat" + File.separator + "app";

    // 连麦直播Demo的build.gradle文件
    public static final String LIVE_PROJECT_BUILD_GRADLE_FILE = LIVE_PROJECT_PATH + File.separator + "build.gradle";
    // 视频通话Demo的build.gradle文件
    public static final String VIDEO_CHAT_PROJECT_BUILD_GRADLE_FILE = VIDEO_CHAT_PROJECT_PATH + File.separator + "build.gradle";
    // 音频通话Demo的build.gradle文件
    public static final String AUDIO_CHAT_BUILD_GRADLE_FILE = AUDIO_CHAT_PROJECT_PATH + File.separator + "build.gradle";
    // 标准版 SDK 工程所在的 Demo 的 build.gradle文件
    public static final String STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE = STAND_SDK_APP_PROJECT_PATH + File.separator + "build.gradle";
    // 测试入会 Demo 的build.gradle文件
    public static final String TEST_SDK_PROJECT_BUILD_GRADLE_FILE = TEST_SDK_PROJECT_PATH + File.separator + "build.gradle";

    @Override
    public int start() {
        return 0;
    }
}

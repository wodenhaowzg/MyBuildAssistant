package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.SanTiYunBaseTask;

import java.io.File;

public abstract class BuildBaseTask extends SanTiYunBaseTask {

    static final String AAR_SRC = "/wstechapi-debug.aar";

    // STAND
    static String globalConfigFileName = File.separator + "GlobalConfig.java";
    public static String globalConfigFilePath = ENTERCONFAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/library" + globalConfigFileName;
    static String tttRtcEngineFileName = File.separator + "TTTRtcEngine.java";
    protected static String tttRtcEngineFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi" + tttRtcEngineFileName;
    static String tttRtcEngineImplFileName = File.separator + "TTTRtcEngineImpl.java";
    protected static String tttRtcEngineImplFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + tttRtcEngineImplFileName;
    static String wstechBuildGradleName = File.separator + "build.gradle";
    protected static String wstechBuildGradlePath = WSTECHAPI_MODULE_PATH + wstechBuildGradleName;
    static String unityFileName = File.separator + "TTTRtcUnity.java";
    protected static String unityFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + unityFileName;
    static String screenMainifestFileName = File.separator + "AndroidManifest.xml";
    public static String screenMainifestFilePath =  STAND_SDK_PROJECT_PATH + File.separator + "myandroidscreenrecordandcrop/src/main/" + screenMainifestFileName;

    // 音频Demo的build.gradle文件
    static String audioChatDemoBuildFileName = File.separator + "build.gradle";
    public static String audioChatDemoBuildFilePath = ENTERCONFAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/library" + audioChatDemoBuildFileName;

    protected static final String LIB = WSTECHAPI_MODULE_PATH + "/libs";
    protected static final String LIB_ARMEABI_V7 = "/armeabi-v7a";
    protected static final String LIB_ARMEABI_V7_PATH = LIB + LIB_ARMEABI_V7;
    protected static final String LIB_ARM64_V8 = "/arm64-v8a";
    protected static final String LIB_ARM64_V8_PATH = LIB + LIB_ARM64_V8;

    protected final String MYVIDEO_LIB_PATH = MYVIDEO_MODULE_PATH + File.separator + "libs";
    protected final String MYVIDEO_ASSETS_NAME = File.separator + "assets";
    protected final String MYVIDEO_ASSETS_PATH = MYVIDEO_MODULE_PATH + File.separator + "src" + File.separator + "main" + MYVIDEO_ASSETS_NAME;
    protected final String MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME = File.separator + "CameraPreviewInput.java";
    protected final String MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH = MYVIDEO_MODULE_PATH + "/src/main/java/com/wushuangtech/videocore/imageprocessing/input" + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME;

    final String wstech_link_enterconfapi = "api project(':enterconfapi')";
    final String wstech_link_myaudio = "api project(':myaudio')";
    final String wstech_link_myvideo = "api project(':myvideo')";
    final String wstech_link_screen = "api project(':myandroidscreenrecordandcrop')";
    final String wstech_link_myvideoimprove = "api project(':myvideoimprove')";

    final String wstech_embed_enterconfapi = "embed project(path: ':enterconfapi', configuration:'default')";
    final String wstech_embed_myaudio = "embed project(path: ':myaudio', configuration:'default')";
    final String wstech_embed_myvideo = "embed project(path: ':myvideo', configuration:'default')";
    final String wstech_embed_screen = "embed project(path: ':myandroidscreenrecordandcrop', configuration:'default')";
    final String wstech_embed_myvideoimprove = "embed project(path: ':myvideoimprove', configuration:'default')";

    final String wstech_ijk_java = "api project(':wsijkplayer_java')";
    final String wstech_ijk_exo = "api project(':wsijkplayer_exo')";
    protected final String wstech_embed_ijk_java = "embed project(path: ':wsijkplayer_java', configuration:'default')";
    protected final String wstech_embed_ijk_exo = "embed project(path: ':wsijkplayer_exo', configuration:'default')";
}

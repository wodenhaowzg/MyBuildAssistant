package com.azx.mybuildassistant.tal;

import com.azx.mybuildassistant.BaseTask;
import com.azx.mybuildassistant.Constants;

import java.io.File;

public class BaseTaskImpl extends BaseTask {

    static final String AAR_SRC = "/wstechapi-debug.aar";
    // SDK 工程路径
    protected static final String STAND_SDK_PROJECT_PATH = Constants.TAL_WORKSPACE + File.separator + "corertc-android";

    // SDK 中的 enterconfapi 模块
    private static final String ENTERCONFAPI_MODULE_NAME = "enterconfapi";
    protected static final String ENTERCONFAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + ENTERCONFAPI_MODULE_NAME;
    // SDK 中的 myaudio 模块
    private static final String MYAUDIO_MODULE_NAME = "myaudio";
    protected static final String MYAUDIO_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + MYAUDIO_MODULE_NAME;
    // SDK 中的 myvideo 模块
    protected static final String MYVIDEO_MODULE_NAME = "myvideo";
    protected static final String MYVIDEO_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + MYVIDEO_MODULE_NAME;
    // SDK 中的 wstechapi 模块
    private static final String WSTECHAPI_MODULE_NAME = "wstechapi";
    protected static final String WSTECHAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + WSTECHAPI_MODULE_NAME;
    // 标准版 SDK 工程所在的 Demo
    static final String STAND_SDK_APP_PROJECT_PATH = STAND_SDK_PROJECT_PATH + File.separator + "app";
    // 标准版 SDK 工程所在的 Demo 的 build.gradle文件
    public static final String STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE = STAND_SDK_APP_PROJECT_PATH + File.separator + "build.gradle";

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
    public static String screenMainifestFilePath = STAND_SDK_PROJECT_PATH + File.separator + "myandroidscreenrecordandcrop/src/main/" + screenMainifestFileName;

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

    String replaceDependencies(String line) {
        if (line.contains("//apply plugin: 'com.kezong.fat-aar'")) {
            return line.replaceAll("//", "");
        } else if (line.contains(wstech_link_enterconfapi)) {
            return "//" + wstech_link_enterconfapi;
        } else if (line.contains(wstech_link_myaudio)) {
            return "//" + wstech_link_myaudio;
        } else if (line.contains(wstech_link_myvideo)) {
            return "//" + wstech_link_myvideo;
        } else if (line.contains(wstech_link_screen)) {
            return "//" + wstech_link_screen;
        } else if (line.contains(wstech_link_myvideoimprove)) {
            return "//" + wstech_link_myvideoimprove;
        } else if (line.contains(wstech_ijk_java)) {
            return "//" + wstech_ijk_java;
        } else if (line.contains(wstech_ijk_exo)) {
            return "//" + wstech_ijk_exo;
        } else if (line.contains("api fileTree(")) {
            return "api fileTree(include: ['*.jar'], dir: 'libs')";
        } else if (line.contains(wstech_embed_enterconfapi) ||
                line.contains(wstech_embed_myaudio) ||
                line.contains(wstech_embed_myvideo) ||
                line.contains(wstech_embed_myvideoimprove) ||
                line.contains(wstech_embed_screen)) {
            return line.replaceAll("//", "");
        }
        return line;
    }
}

package com.azx.mybuildassistant.sdk;

import com.azx.mybuildassistant.BaseTask;
import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.utils.MyTextUtils;

import java.io.File;

public class SDKInfo extends BaseTask {

    public final String AAR_SRC = "/wstechapi-debug.aar";
    // SDK 工程路径
    public String STAND_SDK_PROJECT_PATH;
    public String TEMP_SAVE = Constants.GITHUB_PATH + Constants.PROJECT_NAME + File.separator + "temp_save";

    // SDK 中的 enterconfapi 模块
    public final String ENTERCONFAPI_MODULE_NAME = "enterconfapi";
    public String ENTERCONFAPI_MODULE_PATH;
    // SDK 中的 myaudio 模块
    public final String MYAUDIO_MODULE_NAME = "myaudio";
    public String MYAUDIO_MODULE_PATH;
    // SDK 中的 myvideo 模块
    public final String MYVIDEO_MODULE_NAME = "myvideo";
    public String MYVIDEO_MODULE_PATH;
    // SDK 中的 wstechapi 模块
    public final String WSTECHAPI_MODULE_NAME = "wstechapi";
    public String WSTECHAPI_MODULE_PATH;
    // 标准版 SDK 工程所在的 Demo
    public String STAND_SDK_APP_PROJECT_PATH;

    public String globalConfigFileName = File.separator + "GlobalConfig.java";
    public String globalConfigFilePath;
    public String tttRtcEngineFileName = File.separator + "TTTRtcEngine.java";
    public String tttRtcEngineFilePath;
    public String tttRtcEngineImplFileName = File.separator + "TTTRtcEngineImpl.java";
    public String tttRtcEngineImplFilePath;
    public String wstechBuildGradleName = File.separator + "build.gradle";
    public String wstechBuildGradlePath;
    public String unityFileName = File.separator + "TTTRtcUnity.java";
    public String unityFilePath;
    public String screenMainifestFileName = File.separator + "AndroidManifest.xml";
    public String screenMainifestFilePath;

    public String LIB;
    public String LIB_ARMEABI_V7 = "/armeabi-v7a";
    public String LIB_ARMEABI_V7_PATH;
    public String LIB_ARM64_V8 = "/arm64-v8a";
    public String LIB_ARM64_V8_PATH;

    public String MYVIDEO_LIB_PATH;
    public String MYVIDEO_ASSETS_NAME = File.separator + "assets";
    public String MYVIDEO_ASSETS_PATH;
    public String MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME = File.separator + "CameraPreviewInput.java";
    public String MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH;

    public String wstech_link_enterconfapi = "api project(':enterconfapi')";
    public String wstech_link_myaudio = "api project(':myaudio')";
    public String wstech_link_myvideo = "api project(':myvideo')";
    public String wstech_link_screen = "api project(':myandroidscreenrecordandcrop')";
    public String wstech_link_myvideoimprove = "api project(':myvideoimprove')";

    public String wstech_embed_enterconfapi = "embed project(path: ':enterconfapi', configuration:'default')";
    public String wstech_embed_myaudio = "embed project(path: ':myaudio', configuration:'default')";
    public String wstech_embed_myvideo = "embed project(path: ':myvideo', configuration:'default')";
    public String wstech_embed_screen = "embed project(path: ':myandroidscreenrecordandcrop', configuration:'default')";
    public String wstech_embed_myvideoimprove = "embed project(path: ':myvideoimprove', configuration:'default')";

    public String wstech_ijk_java = "api project(':wsijkplayer_java')";
    public String wstech_ijk_exo = "api project(':wsijkplayer_exo')";
    public String wstech_embed_ijk_java = "embed project(path: ':wsijkplayer_java', configuration:'default')";
    public String wstech_embed_ijk_exo = "embed project(path: ':wsijkplayer_exo', configuration:'default')";

    public SDKInfo(String projectRootPath) {
        this(projectRootPath, null, null);
    }

    public SDKInfo(String projectRootPath, String myaudioModuleRootPath, String myvideoModuleRootPath) {
        if (!MyTextUtils.isEmpty(projectRootPath)) {
            STAND_SDK_PROJECT_PATH = projectRootPath;
        }

        if (!MyTextUtils.isEmpty(myaudioModuleRootPath)) {
            MYAUDIO_MODULE_PATH = myaudioModuleRootPath;
        } else {
            MYAUDIO_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + MYAUDIO_MODULE_NAME;
        }

        if (!MyTextUtils.isEmpty(myvideoModuleRootPath)) {
            MYVIDEO_MODULE_PATH = myvideoModuleRootPath;
        } else {
            MYVIDEO_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + MYVIDEO_MODULE_NAME;
        }

        ENTERCONFAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + ENTERCONFAPI_MODULE_NAME;
        WSTECHAPI_MODULE_PATH = STAND_SDK_PROJECT_PATH + File.separator + WSTECHAPI_MODULE_NAME;
        STAND_SDK_APP_PROJECT_PATH = STAND_SDK_PROJECT_PATH + File.separator + "app";

        LIB = WSTECHAPI_MODULE_PATH + "/libs";
        LIB_ARMEABI_V7_PATH = LIB + LIB_ARMEABI_V7;
        LIB_ARM64_V8_PATH = LIB + LIB_ARM64_V8;
        MYVIDEO_LIB_PATH = MYVIDEO_MODULE_PATH + File.separator + "libs";
        MYVIDEO_ASSETS_PATH = MYVIDEO_MODULE_PATH + File.separator + "src" + File.separator + "main" + MYVIDEO_ASSETS_NAME;
        MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH = MYVIDEO_MODULE_PATH + "/src/main/java/com/wushuangtech/videocore/imageprocessing/input" + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME;

        globalConfigFilePath = ENTERCONFAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/library" + globalConfigFileName;
        tttRtcEngineFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi" + tttRtcEngineFileName;
        tttRtcEngineImplFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + tttRtcEngineImplFileName;
        wstechBuildGradlePath = WSTECHAPI_MODULE_PATH + wstechBuildGradleName;
        unityFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + unityFileName;
        screenMainifestFilePath = STAND_SDK_PROJECT_PATH + File.separator + "myandroidscreenrecordandcrop/src/main/" + screenMainifestFileName;
    }

    public String replaceDependencies(String line) {
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

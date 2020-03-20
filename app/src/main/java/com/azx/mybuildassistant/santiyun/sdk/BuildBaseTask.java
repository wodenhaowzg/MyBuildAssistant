package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.SanTiYunBaseTask;

import java.io.File;
import java.text.SimpleDateFormat;

public abstract class BuildBaseTask extends SanTiYunBaseTask {

    static final String AAR_SRC = "/wstechapi-release.aar";
    final String TEMP_SAVE = MACHINE_PATH + "/Downloads/Learns/MyGithubs/MyBuildAssistant/temp_save";

    // STAND
    String globalConfigFileName = File.separator + "GlobalConfig.java";
    String globalConfigFilePath = ENTERCONFAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/library" + globalConfigFileName;
    String tttRtcEngineFileName = File.separator + "TTTRtcEngine.java";
    String tttRtcEngineFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi" + tttRtcEngineFileName;
    String tttRtcEngineImplFileName = File.separator + "TTTRtcEngineImpl.java";
    String tttRtcEngineImplFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + tttRtcEngineImplFileName;
    String wstechBuildGradleName = File.separator + "build.gradle";
    String wstechBuildGradlePath = WSTECHAPI_MODULE_PATH + wstechBuildGradleName;
    String unityFileName = File.separator + "TTTRtcUnity.java";
    String unityFilePath = WSTECHAPI_MODULE_PATH + File.separator + "src/main/java/com/wushuangtech/wstechapi/internal" + unityFileName;

    static final String LIB = WSTECHAPI_MODULE_PATH + "/libs";
    static final String LIB_ARMEABI_V7 = "/armeabi-v7a";
    static final String LIB_ARMEABI_V7_PATH = LIB + LIB_ARMEABI_V7;
    static final String LIB_ARM64_V8 = "/arm64-v8a";
    static final String LIB_ARM64_V8_PATH = LIB + LIB_ARM64_V8;

    final String MYVIDEO_LIB_PATH = MYVIDEO_MODULE_PATH + File.separator + "libs";
    final String MYVIDEO_ASSETS_NAME = File.separator + "assets";
    final String MYVIDEO_ASSETS_PATH = MYVIDEO_MODULE_PATH + File.separator + "src" + File.separator + "main" + MYVIDEO_ASSETS_NAME;
    final String MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME = File.separator + "CameraPreviewInput.java";
    final String MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH = MYVIDEO_MODULE_PATH + "/src/main/java/com/wushuangtech/videocore/imageprocessing/input" + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME;


    final String wstech_link_enterconfapi = "api project(':enterconfapi')";
    final String wstech_link_myaudio = "api project(':myaudio')";
    final String wstech_link_myvideo = "api project(':myvideo')";
    final String wstech_link_screen = "api project(':myandroidscreenrecordandcrop')";

    final String wstech_embed_enterconfapi = "embed project(path: ':enterconfapi', configuration:'default')";
    final String wstech_embed_myaudio = "embed project(path: ':myaudio', configuration:'default')";
    final String wstech_embed_myvideo = "embed project(path: ':myvideo', configuration:'default')";
    final String wstech_embed_screen = "embed project(path: ':myandroidscreenrecordandcrop', configuration:'default')";

    final String wstech_ijk_java = "api(name: 'ijkplayer_java', ext: 'aar')";
    final String wstech_ijk_exo = "api(name: 'ijkplayer_exo', ext: 'aar')";
    final String wstech_embed_ijk_java = "embed (name:'ijkplayer_java'";
    final String wstech_embed_ijk_exo = "embed (name:'ijkplayer_exo'";

    Branch branch = Branch.STAND;
    String targetAarFileName;
    File desAarFile;
    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");

    @Override
    public int start() {
        return 0;
    }

    enum Branch {
        STAND, QUANMIN
    }
}

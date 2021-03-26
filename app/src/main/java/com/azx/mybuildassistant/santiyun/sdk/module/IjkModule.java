package com.azx.mybuildassistant.santiyun.sdk.module;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

public class IjkModule extends BaseModule {

    private static final String TAG = IjkModule.class.getSimpleName();

    private static final String IJK_MODULE_FFMPEG_SO = "/libijkffmpeg_ttt.so";
    private static final String IJK_MODULE_PLAYER_SO = "/libijkplayer_ttt.so";
    private static final String IJK_MODULE_SDL_SO = "/libijksdl_ttt.so";
//    private static final String IJK_MODULE_JAVA_AAR = "/ijkplayer_java.aar";
//    private static final String IJK_MODULE_EXO_AAR = "/ijkplayer_exo.aar";
    private static final String IJK_MODULE_JAVA_FILE = "/TTTRtcIjkModule.java";
    private static final String IJK_MODULE_JAVA_FILE_PATH = WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/internal" + IJK_MODULE_JAVA_FILE;

    private static final String IJK_FLAG_CREATE_IJK_RENDERER_VIEW = "IJK_MODULE_CreateIjkRendererView";
    private static final String IJK_FLAG_HANDLE_IJK_MODULE = "IJK_MODULE_handleIJKModule";
    private static final String IJK_FLAG_PACKAGE = "ttt.ijk.media.exo.widget.media.IjkVideoView";

    private static final String IJK_FUNCTION_CREATE_IJK_RENDERER_VIEW = "IjkVideoView CreateIjkRendererView(";

    @Override
    public boolean changeCodeToBuild(VersionSelect.VersionBean versionBean) {
        if (!versionBean.ijkModule) {
            boolean b = handleSoLib(versionBean);
            if (!b) {
                MyLog.error(TAG, "handleSoLib -> 处理代码失败！");
                return false;
            }

            boolean b3 = handleIjkCode();
            if (!b3) {
                MyLog.error(TAG, "handleIjkModule -> 处理代码失败！");
                return false;
            }
        }
        return true;
    }

    private boolean handleSoLib(VersionSelect.VersionBean versionBean) {
//        boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + IJK_MODULE_FFMPEG_SO, TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_FFMPEG_SO);
//        if (!b) {
//            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_FFMPEG_SO 文件失败！");
//            return false;
//        }
//        boolean b1 = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + IJK_MODULE_PLAYER_SO, TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_PLAYER_SO);
//        if (!b1) {
//            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_PLAYER_SO 文件失败！");
//            return false;
//        }
//        boolean b2 = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + IJK_MODULE_SDL_SO, TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_SDL_SO);
//        if (!b2) {
//            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_SDL_SO 文件失败！");
//            return false;
//        }
//        if (versionBean.v8Module) {
//            boolean b3 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + IJK_MODULE_FFMPEG_SO, TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_FFMPEG_SO);
//            if (!b3) {
//                MyLog.error(TAG, "handleIjkModule -> 移动 V8 IJK_MODULE_FFMPEG_SO 文件失败！");
//                return false;
//            }
//            boolean b4 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + IJK_MODULE_PLAYER_SO, TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_PLAYER_SO);
//            if (!b4) {
//                MyLog.error(TAG, "handleIjkModule -> 移动 V8 IJK_MODULE_PLAYER_SO 文件失败！");
//                return false;
//            }
//            boolean b5 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + IJK_MODULE_SDL_SO, TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_SDL_SO);
//            if (!b5) {
//                MyLog.error(TAG, "handleIjkModule -> 移动 V8 IJK_MODULE_SDL_SO 文件失败！");
//                return false;
//            }
//        }
//        boolean b6 = MyFileUtils.moveFile(LIB + IJK_MODULE_JAVA_AAR, TEMP_SAVE + IJK_MODULE_JAVA_AAR);
//        if (!b6) {
//            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_JAVA_AAR 文件失败！");
//            return false;
//        }
//        boolean b7 = MyFileUtils.moveFile(LIB + IJK_MODULE_EXO_AAR, TEMP_SAVE + IJK_MODULE_EXO_AAR);
//        if (!b7) {
//            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_EXO_AAR 文件失败！");
//            return false;
//        }
        boolean b8 = MyFileUtils.moveFile(IJK_MODULE_JAVA_FILE_PATH, TEMP_SAVE + IJK_MODULE_JAVA_FILE);
        if (!b8) {
            MyLog.error(TAG, "handleIjkModule -> 移动 IJK_MODULE_JAVA_FILE 文件失败！");
            return false;
        }
        return true;
    }

    private boolean handleIjkCode() {
        boolean b = MyFileUtils.modifyFileContent(tttRtcEngineImplFilePath, new MyFileUtils.FileContentListener() {

            boolean startReplace;

            @Override
            public String lineTextContent(String line) {
                if (line.contains(IJK_FUNCTION_CREATE_IJK_RENDERER_VIEW)) {
                    startReplace = true;
                    return "";
                } else if (line.contains(IJK_FLAG_CREATE_IJK_RENDERER_VIEW)) {
                    startReplace = false;
                    return "";
                } else {
                    if (startReplace) {
                        return "";
                    }
                }

                if (line.contains(IJK_FLAG_HANDLE_IJK_MODULE)) {
                    return "return null;";
                }

                if (line.contains(IJK_FLAG_PACKAGE)) {
                    return "";
                }
                return line;
            }
        });
        if (!b) {
            MyLog.error(TAG, "handleIjkCode -> 处理 tttRtcEngineImplFilePath 文件代码失败！");
            return false;
        }

        boolean b1 = MyFileUtils.modifyFileContent(tttRtcEngineFilePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains(IJK_FUNCTION_CREATE_IJK_RENDERER_VIEW)) {
                    return "";
                }

                if (line.contains(IJK_FLAG_PACKAGE)) {
                    return "";
                }
                return line;
            }
        });
        if (!b1) {
            MyLog.error(TAG, "handleIjkCode -> 处理 tttRtcEngineFilePath 文件代码失败！");
            return false;
        }

        boolean b2 = MyFileUtils.modifyFileContent(wstechBuildGradlePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains(wstech_embed_ijk_java) ||
                        line.contains(wstech_embed_ijk_exo)) {
                    return "";
                }
                return line;
            }
        });
        if (!b2) {
            MyLog.error(TAG, "handleIjkCode -> 处理 wstechBuildGradlePath 文件代码失败！");
            return false;
        }
        return true;
    }

    @Override
    public boolean restoreCode(VersionSelect.VersionBean versionBean) {
        if (!versionBean.ijkModule) {
//            MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_FFMPEG_SO, LIB_ARMEABI_V7_PATH + IJK_MODULE_FFMPEG_SO);
//            MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_PLAYER_SO, LIB_ARMEABI_V7_PATH + IJK_MODULE_PLAYER_SO);
//            MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + IJK_MODULE_SDL_SO, LIB_ARMEABI_V7_PATH + IJK_MODULE_SDL_SO);
//            MyFileUtils.moveFile(TEMP_SAVE + IJK_MODULE_JAVA_AAR, LIB + IJK_MODULE_JAVA_AAR);
//            MyFileUtils.moveFile(TEMP_SAVE + IJK_MODULE_EXO_AAR, LIB + IJK_MODULE_EXO_AAR);
            if (versionBean.v8Module) {
//                MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_FFMPEG_SO, LIB_ARM64_V8_PATH + IJK_MODULE_FFMPEG_SO);
//                MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_PLAYER_SO, LIB_ARM64_V8_PATH + IJK_MODULE_PLAYER_SO);
//                MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + IJK_MODULE_SDL_SO, LIB_ARM64_V8_PATH + IJK_MODULE_SDL_SO);
            }
            MyFileUtils.moveFile(TEMP_SAVE + IJK_MODULE_JAVA_FILE, IJK_MODULE_JAVA_FILE_PATH);
        }
        return true;
    }
}

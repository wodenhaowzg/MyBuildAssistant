package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

class VideoModule extends BaseModule {

    private static final String TAG = VideoModule.class.getSimpleName();

    private static final String VIDEO_MODULE_CODEC_SO = "/libcodec_ttt.so";
    private static final String VIDEO_MODULE_YUV_SO = "/libyuv_ttt.so";
    private static final String VIDEO_MODULE_JAVA_FILE = "/TTTVideoModule.java";
    private static final String VIDEO_MODULE_JAVA_FILE_PATH = WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/internal" + VIDEO_MODULE_JAVA_FILE;
    private static final String VIDEO_MODULE_FLAG_ONE = "IJK_handleSoLibTG";
    private static final String VIDEO_MODULE_FLAG_TWO = "IJK_handleSoLibTGEvent";

    @Override
    boolean changeCodeToBuild(VersionSelect.VersionBean versionBean) {
        if (!versionBean.videoModule) {
            boolean b = handleSoLib(versionBean);
            if (!b) {
                MyLog.e(TAG, "handleSoLib -> 处理代码失败！");
                return false;
            }

            boolean b3 = handleJavaCode();
            if (!b3) {
                MyLog.e(TAG, "handleJavaCode -> 处理代码失败！");
                return false;
            }
        }
        return true;
    }

    private boolean handleSoLib(VersionSelect.VersionBean versionBean) {
        boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + VIDEO_MODULE_CODEC_SO, TEMP_SAVE + LIB_ARMEABI_V7 + VIDEO_MODULE_CODEC_SO);
        if (!b) {
            MyLog.e(TAG, "handleSoLib -> 移动 VIDEO_MODULE_CODEC_SO 文件失败！");
            return false;
        }

        boolean b1 = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + VIDEO_MODULE_YUV_SO, TEMP_SAVE + LIB_ARMEABI_V7 + VIDEO_MODULE_YUV_SO);
        if (!b1) {
            MyLog.e(TAG, "handleSoLib -> 移动 VIDEO_MODULE_YUV_SO 文件失败！");
            return false;
        }
        if (versionBean.v8Module) {
            boolean b2 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + VIDEO_MODULE_CODEC_SO, TEMP_SAVE + LIB_ARM64_V8 + VIDEO_MODULE_CODEC_SO);
            if (!b2) {
                MyLog.e(TAG, "handleSoLib -> 移动 V8 VIDEO_MODULE_CODEC_SO 文件失败！");
                return false;
            }
            boolean b3 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + VIDEO_MODULE_YUV_SO, TEMP_SAVE + LIB_ARM64_V8 + VIDEO_MODULE_YUV_SO);
            if (!b3) {
                MyLog.e(TAG, "handleSoLib -> 移动 V8 VIDEO_MODULE_YUV_SO 文件失败！");
                return false;
            }
        }
        boolean b2 = MyFileUtils.moveFile(VIDEO_MODULE_JAVA_FILE_PATH, TEMP_SAVE + VIDEO_MODULE_JAVA_FILE);
        if (!b2) {
            MyLog.e(TAG, "handleSoLib -> 移动 VIDEO_MODULE_JAVA_FILE 文件失败！");
            return false;
        }
        return true;
    }

    private boolean handleJavaCode() {
        boolean b = MyFileUtils.modifyFileContent(tttRtcEngineImplFilePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains(VIDEO_MODULE_FLAG_ONE) || line.contains(VIDEO_MODULE_FLAG_TWO)) {
                    return "return null;";
                }
                return line;
            }
        });
        if (!b) {
            MyLog.e(TAG, "handleJavaCode -> 处理 tttRtcEngineImplFilePath 文件代码失败！");
            return false;
        }

        boolean b1 = MyFileUtils.modifyFileContent(globalConfigFilePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains("String SDK_VERSION_TYPE =")) {
                    return "public static final String SDK_VERSION_TYPE = \"Voice\";";
                } else if (line.contains("boolean mIsVoiceSDK =")) {
                    return "public static boolean mIsVoiceSDK = true;";
                }
                return line;
            }
        });
        if (!b1) {
            MyLog.e(TAG, "handleJavaCode -> 处理 globalConfigFilePath 文件代码失败！");
            return false;
        }

        boolean b2 = MyFileUtils.modifyFileContent(unityFilePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains(VIDEO_MODULE_FLAG_ONE) || line.contains(VIDEO_MODULE_FLAG_TWO)) {
                    return "return null;";
                }
                return line;
            }
        });
        if (!b2) {
            MyLog.e(TAG, "handleJavaCode -> 处理 unityFilePath 文件代码失败！");
            return false;
        }
        return true;
    }

    @Override
    boolean restoreCode(VersionSelect.VersionBean versionBean) {
        if (!versionBean.videoModule) {
            restoreV7MoveFile(VIDEO_MODULE_CODEC_SO);
            restoreV7MoveFile(VIDEO_MODULE_YUV_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(VIDEO_MODULE_CODEC_SO);
                restoreV8MoveFile(VIDEO_MODULE_YUV_SO);
            }
            MyFileUtils.moveFile(TEMP_SAVE + VIDEO_MODULE_JAVA_FILE, VIDEO_MODULE_JAVA_FILE_PATH);
        }
        return true;
    }
}

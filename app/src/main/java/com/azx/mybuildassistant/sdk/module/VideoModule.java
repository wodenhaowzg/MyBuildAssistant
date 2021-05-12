package com.azx.mybuildassistant.sdk.module;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.sdk.SDKInfo;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

public class VideoModule extends BaseModule {

    private static final String TAG = VideoModule.class.getSimpleName();

    private static final String VIDEO_MODULE_CODEC_SO = "/libcodec_ttt.so";
    private static final String VIDEO_MODULE_YUV_SO = "/libyuv_ttt.so";
    private static final String VIDEO_MODULE_JAVA_FILE = "/TTTVideoModule.java";
    private final String VIDEO_MODULE_JAVA_FILE_PATH;
    private static final String VIDEO_MODULE_FLAG_ONE = "VideoFlag_handleVideoModule_config";
    private static final String VIDEO_MODULE_FLAG_TWO = "VideoFlag_handleVideoModule_event";

    private static final String VIDEO_MODULE_FLAG_THREE = "handleVideoModule_CreateRendererView";
    private static final String VIDEO_MODULE_FUNC_FLAG_THREE = "SurfaceView CreateRendererView(";

    public VideoModule(SDKInfo sdkInfo) {
        super(sdkInfo);
        VIDEO_MODULE_JAVA_FILE_PATH = mSDKInfo.WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/internal" + VIDEO_MODULE_JAVA_FILE;
    }

    @Override
    public boolean changeCodeToBuild(VersionSelect.VersionBean versionBean) {
        if (!versionBean.videoModule) {
            boolean b = handleSoLib(versionBean);
            if (!b) {
                MyLog.error(TAG, "handleSoLib -> 处理代码失败！");
                return false;
            }

            boolean b3 = handleJavaCode();
            if (!b3) {
                MyLog.error(TAG, "handleJavaCode -> 处理代码失败！");
                return false;
            }
        }
        return true;
    }

    private boolean handleSoLib(VersionSelect.VersionBean versionBean) {
        boolean b = MyFileUtils.moveFile(mSDKInfo.LIB_ARMEABI_V7_PATH + VIDEO_MODULE_CODEC_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + VIDEO_MODULE_CODEC_SO);
        if (!b) {
            MyLog.error(TAG, "handleSoLib -> 移动 VIDEO_MODULE_CODEC_SO 文件失败！");
            return false;
        }

        boolean b1 = MyFileUtils.moveFile(mSDKInfo.LIB_ARMEABI_V7_PATH + VIDEO_MODULE_YUV_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + VIDEO_MODULE_YUV_SO);
        if (!b1) {
            MyLog.error(TAG, "handleSoLib -> 移动 VIDEO_MODULE_YUV_SO 文件失败！");
            return false;
        }
        if (versionBean.v8Module) {
            boolean b2 = MyFileUtils.moveFile(mSDKInfo.LIB_ARM64_V8_PATH + VIDEO_MODULE_CODEC_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + VIDEO_MODULE_CODEC_SO);
            if (!b2) {
                MyLog.error(TAG, "handleSoLib -> 移动 V8 VIDEO_MODULE_CODEC_SO 文件失败！");
                return false;
            }
            boolean b3 = MyFileUtils.moveFile(mSDKInfo.LIB_ARM64_V8_PATH + VIDEO_MODULE_YUV_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + VIDEO_MODULE_YUV_SO);
            if (!b3) {
                MyLog.error(TAG, "handleSoLib -> 移动 V8 VIDEO_MODULE_YUV_SO 文件失败！");
                return false;
            }
        }
        boolean b2 = MyFileUtils.moveFile(VIDEO_MODULE_JAVA_FILE_PATH, mSDKInfo.TEMP_SAVE + VIDEO_MODULE_JAVA_FILE);
        if (!b2) {
            MyLog.error(TAG, "handleSoLib -> 移动 VIDEO_MODULE_JAVA_FILE 文件失败！");
            return false;
        }
        return true;
    }

    private boolean handleJavaCode() {
        boolean b3 = MyFileUtils.modifyFileContent(mSDKInfo.tttRtcEngineFilePath, new MyFileUtils.FileContentListener() {

            private boolean addContent;
            private boolean startReplace;

            @Override
            public String lineTextContent(String line) {
                if (line.contains(VIDEO_MODULE_FUNC_FLAG_THREE)) {
                    startReplace = true;
                } else if (line.contains(VIDEO_MODULE_FLAG_THREE)) {
                    startReplace = false;
                } else {
                    if (startReplace) {
                        if (!addContent) {
                            addContent = true;
                            return "return null;";
                        } else {
                            return "";
                        }
                    }
                }
                return line;
            }
        });
        if (!b3) {
            MyLog.error(TAG, "handleJavaCode -> 处理 tttRtcEngineFilePath 文件代码失败！");
            return false;
        }

        boolean b = MyFileUtils.modifyFileContent(mSDKInfo.tttRtcEngineImplFilePath, line -> {
            if (line.contains(VIDEO_MODULE_FLAG_ONE) || line.contains(VIDEO_MODULE_FLAG_TWO)) {
                return "return null;";
            }


            return line;
        });
        if (!b) {
            MyLog.error(TAG, "handleJavaCode -> 处理 tttRtcEngineImplFilePath 文件代码失败！");
            return false;
        }

        boolean b1 = MyFileUtils.modifyFileContent(mSDKInfo.globalConfigFilePath, line -> {
            if (line.contains("String SDK_VERSION_TYPE =")) {
                return "public static final String SDK_VERSION_TYPE = \"Voice\";";
            } else if (line.contains("boolean mIsVoiceSDK =")) {
                return "public static boolean mIsVoiceSDK = true;";
            }
            return line;
        });
        if (!b1) {
            MyLog.error(TAG, "handleJavaCode -> 处理 globalConfigFilePath 文件代码失败！");
            return false;
        }

        boolean b2 = MyFileUtils.modifyFileContent(mSDKInfo.unityFilePath, line -> {
            if (line.contains(VIDEO_MODULE_FLAG_ONE) || line.contains(VIDEO_MODULE_FLAG_TWO)) {
                return "return null;";
            }
            return line;
        });
        if (!b2) {
            MyLog.error(TAG, "handleJavaCode -> 处理 unityFilePath 文件代码失败！");
            return false;
        }
        return true;
    }

    @Override
    public boolean restoreCode(VersionSelect.VersionBean versionBean) {
        if (!versionBean.videoModule) {
            restoreV7MoveFile(VIDEO_MODULE_CODEC_SO);
            restoreV7MoveFile(VIDEO_MODULE_YUV_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(VIDEO_MODULE_CODEC_SO);
                restoreV8MoveFile(VIDEO_MODULE_YUV_SO);
            }
            MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + VIDEO_MODULE_JAVA_FILE, VIDEO_MODULE_JAVA_FILE_PATH);
        }
        return true;
    }
}

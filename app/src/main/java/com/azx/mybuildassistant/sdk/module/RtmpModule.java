package com.azx.mybuildassistant.sdk.module;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.sdk.SDKInfo;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

public class RtmpModule extends BaseModule {

    private static final String TAG = RtmpModule.class.getSimpleName();

    private static final String RTMP_MODULE_SO = "/librtmp.so";
    private static final String RTMP_MODULE_JAVA_FILE = "/TTTRtmpModule.java";
    private final String RTMP_MODULE_JAVA_FILE_PATH;
    private static final String RTMP_MODULE_FLAG_ONE = "RtmpFlag_handleRTMPModule";
    private static final String RTMP_MODULE_FUNC_FLAG_ONE = "Object handleRTMPModule(";

    public RtmpModule(SDKInfo sdkInfo) {
        super(sdkInfo);
        RTMP_MODULE_JAVA_FILE_PATH = mSDKInfo.WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/internal" + RTMP_MODULE_JAVA_FILE;
    }

    @Override
    public boolean changeCodeToBuild(VersionSelect.VersionBean versionBean) {
        if (!versionBean.rtmpModule) {
            boolean b = MyFileUtils.moveFile(mSDKInfo.LIB_ARMEABI_V7_PATH + RTMP_MODULE_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + RTMP_MODULE_SO);
            if (!b) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 RTMP_MODULE_SO 文件失败！");
                return false;
            }
            if (versionBean.v8Module) {
                boolean b1 = MyFileUtils.moveFile(mSDKInfo.LIB_ARM64_V8_PATH + RTMP_MODULE_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + RTMP_MODULE_SO);
                if (!b1) {
                    MyLog.error(TAG, "changeCodeToBuild -> 移动 V8 RTMP_MODULE_SO 文件失败！");
                    return false;
                }
            }
            boolean b2 = MyFileUtils.moveFile(RTMP_MODULE_JAVA_FILE_PATH, mSDKInfo.TEMP_SAVE + RTMP_MODULE_JAVA_FILE);
            if (!b2) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 RTMP_MODULE_JAVA_FILE 文件失败！");
                return false;
            }

            boolean b3 = MyFileUtils.modifyFileContent(mSDKInfo.tttRtcEngineImplFilePath, new MyFileUtils.FileContentListener() {

                private boolean startReplace;
                private boolean addContent;

                @Override
                public String lineTextContent(String line) {
                    if (line.contains(RTMP_MODULE_FUNC_FLAG_ONE)) {
                        startReplace = true;
                    } else if (line.contains(RTMP_MODULE_FLAG_ONE)) {
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
                MyLog.error(TAG, "changeCodeToBuild -> 处理代码失败！");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean restoreCode(VersionSelect.VersionBean versionBean) {
        if (!versionBean.rtmpModule) {
            restoreV7MoveFile(RTMP_MODULE_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(RTMP_MODULE_SO);
            }
            MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + RTMP_MODULE_JAVA_FILE, RTMP_MODULE_JAVA_FILE_PATH);
        }
        return true;
    }
}

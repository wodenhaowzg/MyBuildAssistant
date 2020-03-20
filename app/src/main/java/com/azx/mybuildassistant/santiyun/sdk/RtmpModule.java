package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

class RtmpModule extends BaseModule {

    private static final String TAG = RtmpModule.class.getSimpleName();

    private static final String RTMP_MODULE_SO = "/librtmp.so";
    private static final String RTMP_MODULE_JAVA_FILE = "/TTTRtmpModule.java";
    private static final String RTMP_MODULE_JAVA_FILE_PATH = WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/internal" + RTMP_MODULE_JAVA_FILE;
    private static final String RTMP_MODULE_FLAG_ONE = "RTMP_MODULE_changeCodeToBuild";
    private static final String RTMP_MODULE_FUNC_FLAG = "Object changeCodeToBuild(";

    @Override
    boolean changeCodeToBuild(VersionSelect.VersionBean versionBean) {
        if (!versionBean.rtmpModule) {
            boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + RTMP_MODULE_SO, TEMP_SAVE + LIB_ARMEABI_V7 + RTMP_MODULE_SO);
            if (!b) {
                MyLog.e(TAG, "changeCodeToBuild -> 移动 RTMP_MODULE_SO 文件失败！");
                return false;
            }
            if (versionBean.v8Module) {
                boolean b1 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + RTMP_MODULE_SO, TEMP_SAVE + LIB_ARM64_V8 + RTMP_MODULE_SO);
                if (!b1) {
                    MyLog.e(TAG, "changeCodeToBuild -> 移动 V8 RTMP_MODULE_SO 文件失败！");
                    return false;
                }
            }
            boolean b2 = MyFileUtils.moveFile(RTMP_MODULE_JAVA_FILE_PATH, TEMP_SAVE + RTMP_MODULE_JAVA_FILE);
            if (!b2) {
                MyLog.e(TAG, "changeCodeToBuild -> 移动 RTMP_MODULE_JAVA_FILE 文件失败！");
                return false;
            }

            boolean b3 = MyFileUtils.modifyFileContent(tttRtcEngineImplFilePath, new MyFileUtils.FileContentListener() {

                private boolean startReplace;
                private boolean addContent;

                @Override
                public String lineTextContent(String line) {
                    if (line.contains(RTMP_MODULE_FUNC_FLAG)) {
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
                MyLog.e(TAG, "changeCodeToBuild -> 处理代码失败！");
                return false;
            }
        }
        return true;
    }

    @Override
    boolean restoreCode(VersionSelect.VersionBean versionBean) {
        if (!versionBean.rtmpModule) {
            restoreV7MoveFile(RTMP_MODULE_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(RTMP_MODULE_SO);
            }
            MyFileUtils.moveFile(TEMP_SAVE + RTMP_MODULE_JAVA_FILE, RTMP_MODULE_JAVA_FILE_PATH);
        }
        return true;
    }
}

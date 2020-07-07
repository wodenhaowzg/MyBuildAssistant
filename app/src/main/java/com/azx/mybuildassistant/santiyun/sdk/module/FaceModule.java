package com.azx.mybuildassistant.santiyun.sdk.module;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;


public class FaceModule extends BaseModule {

    private static final String TAG = "FaceModule";

    private static final String FACE_MODULE_JAR = File.separator + "/versalibrary.jar";
    private static final String FACE_MODULE_JAR2 = File.separator + "/okio-2.2.2.jar";
    private static final String FACE_MODULE_SO1 = File.separator + "/libclcheck.so";
    private static final String FACE_MODULE_SO2 = File.separator + "/libversa_recognize_jni.so";

    private static final String FACE_MODULE_JAVA_FILE_NAME = File.separator + "VersaManager.java";
    private static final String FACE_MODULE_JAVA_FILE_PATH = MYVIDEO_MODULE_PATH + "/src/main/java/com/wushuangtech/videocore/imageprocessing/versa" + FACE_MODULE_JAVA_FILE_NAME;

    private static final String FACE_FLAG_VAR_OBJ = "FACE_MODULE_TAG_OBJECT";
    private static final String FACE_FLAG_FUNC_HANDLE = "FACE_MODULE_TAG_HANDLE";
    private static final String FACE_FLAG_FUNC_TWO = "FACE_MODULE_FUNC_ONE";
    private static final String FACE_FLAG_PACKAGE = "com.wushuangtech.videocore.imageprocessing.versa.VersaManager";
    private static final String FACE_FILE_FLAG_ENABLE = "mVersaModuleEnabled";

    // TTTRtcEngineImpl.java
    private static final String FACE_FLAG_TWO = "VersaManager.OnVersaInitListener";
    private static final String FACE_FLAG_THREE = "VersaManager.init(mContext, this);";

    @Override
    public boolean changeCodeToBuild(VersionSelect.VersionBean bean) {
        if (!bean.faceModule) {
            boolean b = moveLibs(bean);
            if (!b) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 moveLibs 文件夹失败！");
                return false;
            }

            boolean b1 = MyFileUtils.moveFileDir(MYVIDEO_ASSETS_PATH, TEMP_SAVE + MYVIDEO_ASSETS_NAME);
            if (!b1) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 MYVIDEO_ASSETS_NAME 文件夹失败！");
                return false;
            }

            boolean b3 = MyFileUtils.moveFile(FACE_MODULE_JAVA_FILE_PATH, TEMP_SAVE + FACE_MODULE_JAVA_FILE_NAME);
            if (!b3) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE_MODULE_JAVA_FILE_NAME 文件失败！");
                return false;
            }

            boolean b4 = MyFileUtils.copyFile(MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH, TEMP_SAVE + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME);
            if (!b4) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME 文件失败！");
                return false;
            }

            boolean b2 = MyFileUtils.modifyFileContent(MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH, line -> {
                if (line.contains(FACE_FLAG_VAR_OBJ) || line.contains(FACE_FLAG_PACKAGE)) {
                    return "";
                }

                if (line.contains(FACE_FLAG_FUNC_HANDLE)) {
                    return "return null;";
                }
                return line;
            });

            if (!b2) {
                MyLog.error(TAG, "changeCodeToBuild -> 处理 MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH 文件代码失败！");
                return false;
            }

            boolean b5 = MyFileUtils.modifyFileContent(tttRtcEngineImplFilePath, line -> {
                if (line.contains(FACE_FLAG_PACKAGE) || line.contains(FACE_FLAG_THREE) || line.contains(FACE_FLAG_FUNC_TWO)) {
                    return "";
                }

                if (line.contains(FACE_FLAG_TWO)) {
                    return "{";
                }
                return line;
            });

            if (!b5) {
                MyLog.error(TAG, "changeCodeToBuild -> 处理 tttRtcEngineImplFilePath 文件代码失败！");
                return false;
            }
        } else {
            boolean b6 = MyFileUtils.modifyFileContent(globalConfigFilePath, line -> {
                if (line.contains(FACE_FILE_FLAG_ENABLE)) {
                    return "public static boolean mVersaModuleEnabled = true;";
                }
                return line;
            });

            if (!b6) {
                MyLog.error(TAG, "changeCodeToBuild -> 处理 globalConfigFilePath 文件代码失败！");
                return false;
            }

            if (!bean.v8Module) {
                return moveSO(false);
            }
        }
        return true;
    }

    private boolean moveLibs(VersionSelect.VersionBean bean) {
        boolean moveResult = moveSO(true);
        if (!moveResult) {
            return false;
        }

        boolean b = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + FACE_MODULE_JAR, TEMP_SAVE + FACE_MODULE_JAR);
        if (!b) {
            MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE_MODULE_JAR 文件失败！");
            return false;
        }

        boolean b2 = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + FACE_MODULE_JAR2, TEMP_SAVE + FACE_MODULE_JAR2);
        if (!b2) {
            MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE_MODULE_JAR2 文件失败！");
            return false;
        }
        return true;
    }

    private boolean moveSO(boolean v7Module) {
        if (v7Module) {
            boolean b = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + LIB_ARMEABI_V7 + FACE_MODULE_SO1, TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARMEABI_V7 + FACE_MODULE_SO1);
            if (!b) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE MODULE V7 SO1 文件失败！");
                return false;
            }

            boolean b1 = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + LIB_ARMEABI_V7 + FACE_MODULE_SO2, TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARMEABI_V7 + FACE_MODULE_SO2);
            if (!b1) {
                MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE MODULE V7 SO2 文件失败！");
                return false;
            }
        }

        boolean b2 = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO1, TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO1);
        if (!b2) {
            MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE MODULE V8 SO1 文件失败！");
            return false;
        }

        boolean b3 = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO2, TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO2);
        if (!b3) {
            MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE MODULE V8 SO2 文件失败！");
            return false;
        }
        return true;
    }

    @Override
    public boolean restoreCode(VersionSelect.VersionBean bean) {
        if (!bean.faceModule) {
            MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARMEABI_V7 + FACE_MODULE_SO1, MYVIDEO_LIB_PATH + LIB_ARMEABI_V7 + FACE_MODULE_SO1);
            MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARMEABI_V7 + FACE_MODULE_SO2, MYVIDEO_LIB_PATH + LIB_ARMEABI_V7 + FACE_MODULE_SO2);
            MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO1, MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO1);
            MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO2, MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO2);
            MyFileUtils.moveFile(TEMP_SAVE + FACE_MODULE_JAR, MYVIDEO_LIB_PATH + FACE_MODULE_JAR);
            MyFileUtils.moveFile(TEMP_SAVE + FACE_MODULE_JAR2, MYVIDEO_LIB_PATH + FACE_MODULE_JAR2);
            MyFileUtils.moveFileDir(TEMP_SAVE + MYVIDEO_ASSETS_NAME, MYVIDEO_ASSETS_PATH);
            MyFileUtils.moveFile(TEMP_SAVE + FACE_MODULE_JAVA_FILE_NAME, FACE_MODULE_JAVA_FILE_PATH);
            MyFileUtils.moveFile(TEMP_SAVE + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME, MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH);
        } else {
            if (!bean.v8Module) {
                MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO1, MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO1);
                MyFileUtils.moveFile(TEMP_SAVE + File.separator + MYVIDEO_MODULE_NAME + LIB_ARM64_V8 + FACE_MODULE_SO2, MYVIDEO_LIB_PATH + LIB_ARM64_V8 + FACE_MODULE_SO2);
            }
        }
        return true;
    }
}

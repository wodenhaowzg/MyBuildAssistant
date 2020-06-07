package com.azx.mybuildassistant.santiyun.sdk.module;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;


public class FaceModule extends BaseModule {

    private static final String TAG = "FaceModule";

    private static final String FACE_MODULE_AAR = File.separator + "versalibrary-release.aar";
    private static final String FACE_MODULE_JAVA_FILE_NAME = File.separator + "VersaManager.java";
    private static final String FACE_MODULE_JAVA_FILE_PATH = MYVIDEO_MODULE_PATH + "/src/main/java/com/wushuangtech/videocore/imageprocessing/versa" + FACE_MODULE_JAVA_FILE_NAME;

    private static final String FACE_FLAG_VAR_OBJ = "FACE_MODULE_TAG_OBJECT";
    private static final String FACE_FLAG_FUNC_HANDLE = "FACE_MODULE_TAG_HANDLE";
    private static final String FACE_FLAG_FUNC_TWO = "FACE_MODULE_FUNC_ONE";
    private static final String FACE_FLAG_PACKAGE = "com.wushuangtech.videocore.imageprocessing.versa.VersaManager";

    // TTTRtcEngineImpl.java
    private static final String FACE_FLAG_TWO = "VersaManager.OnVersaInitListener";
    private static final String FACE_FLAG_THREE = "VersaManager.init(mContext, this);";

    @Override
    public boolean changeCodeToBuild(VersionSelect.VersionBean bean) {
        boolean b = MyFileUtils.moveFile(MYVIDEO_LIB_PATH + FACE_MODULE_AAR, TEMP_SAVE + FACE_MODULE_AAR);
        if (!b) {
            MyLog.error(TAG, "changeCodeToBuild -> 移动 FACE_MODULE_AAR 文件失败！");
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
        return true;
    }

    @Override
    public boolean restoreCode(VersionSelect.VersionBean bean) {
        MyFileUtils.moveFile(TEMP_SAVE + FACE_MODULE_AAR, MYVIDEO_LIB_PATH + FACE_MODULE_AAR);
        MyFileUtils.moveFileDir(TEMP_SAVE + MYVIDEO_ASSETS_NAME, MYVIDEO_ASSETS_PATH);
        MyFileUtils.moveFile(TEMP_SAVE + FACE_MODULE_JAVA_FILE_NAME, FACE_MODULE_JAVA_FILE_PATH);
        MyFileUtils.moveFile(TEMP_SAVE + MYVIDEO_CAMERA_INPUT_JAVA_FILE_NAME, MYVIDEO_CAMERA_INPUT_JAVA_FILE_PATH);
        return true;
    }
}

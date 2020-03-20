package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.MyFileUtils;

abstract class BaseModule extends BuildBaseTask {

    abstract boolean changeCodeToBuild(VersionSelect.VersionBean versionBean);

    abstract boolean restoreCode(VersionSelect.VersionBean versionBean);

    void restoreV7MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + fileName, LIB_ARMEABI_V7_PATH + fileName);
    }

    void restoreV8MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + fileName, LIB_ARM64_V8_PATH + fileName);
    }
}

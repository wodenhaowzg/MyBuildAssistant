package com.azx.mybuildassistant.sdk.module;

import com.azx.mybuildassistant.Task;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.sdk.SDKInfo;
import com.azx.mybuildassistant.utils.MyFileUtils;

public abstract class BaseModule implements Task {

    protected SDKInfo mSDKInfo;

    public BaseModule(SDKInfo sdkInfo) {
        this.mSDKInfo = sdkInfo;
    }

    public abstract boolean changeCodeToBuild(VersionSelect.VersionBean versionBean);

    public abstract boolean restoreCode(VersionSelect.VersionBean versionBean);

    void restoreV7MoveFile(String fileName) {
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + fileName, mSDKInfo.LIB_ARMEABI_V7_PATH + fileName);
    }

    void restoreV8MoveFile(String fileName) {
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + fileName, mSDKInfo.LIB_ARM64_V8_PATH + fileName);
    }

    @Override
    public int start() {
        return 0;
    }
}

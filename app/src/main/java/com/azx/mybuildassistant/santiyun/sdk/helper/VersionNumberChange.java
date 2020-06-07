package com.azx.mybuildassistant.santiyun.sdk.helper;

import com.azx.mybuildassistant.santiyun.sdk.BuildBaseTask;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

public class VersionNumberChange {

    private static final String TAG = VersionNumberChange.class.getSimpleName();

    private String[] buildFiles = new String[]{
            BuildBaseTask.LIVE_PROJECT_BUILD_GRADLE_FILE,
            BuildBaseTask.VIDEO_CHAT_PROJECT_BUILD_GRADLE_FILE,
            BuildBaseTask.AUDIO_CHAT_BUILD_GRADLE_FILE,
            BuildBaseTask.STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE,
            BuildBaseTask.TEST_SDK_PROJECT_BUILD_GRADLE_FILE,
    };

    public boolean changeVersionNumber() {
        // 获取版本号，格式为x.x.x，比如2.9.0
        String sdk_version_number = MyFileUtils.getStrFromVariable(BuildBaseTask.globalConfigFilePath, "SDK_VERSION_NUMBER");
        if (sdk_version_number == null) {
            return false;
        }
        MyLog.d(TAG, "打包的版本号 : " + sdk_version_number);
        // 修改版本号
        for (String buildFile : buildFiles) {
            MyLog.d(TAG, "Iterator build.gradle : " + buildFile);
            MyFileUtils.modifyFileContent(buildFile, line -> {
                if (line.contains("versionName")) {
                    String target = line.trim();
                    String[] strAaray = target.split(" ");
                    return line.replace(strAaray[1], "\"" + sdk_version_number + "\"");
                }
                return line;
            });
        }
        return true;
    }
}

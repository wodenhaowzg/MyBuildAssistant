package com.azx.mybuildassistant;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;

class ApkBuilder {

    private static final String STAND_SDK_PROJECT_PATH = "/Users/zanewang/Downloads/WorkSpace/Company/Santiyun/Code/SDK/Demo/android/WS_ANDROID_MOMODemo";
    private static final String APK_OUTPUT = STAND_SDK_PROJECT_PATH + "/app/build/outputs/apk/debug";
    private static final String GRADLE = "/Users/zanewang/.gradle/wrapper/dists/gradle-6.5-bin/6nifqtx7604sqp1q6g8wikw7p/gradle-6.5/bin/gradle";
    private static final String TAG = "ApkBuilder";

    public void start() {
//        CmdExecuteHelper executer = new CmdExecuteHelper();
//        CmdBean[] cmd = new CmdBean[]{
//                new CmdBean("cd " + STAND_SDK_PROJECT_PATH),
//                new CmdBean("adb uninstall com.tttrtclive.test"),
//                new CmdBean("adb install " + apk),
//        };
//        int i2 = mCmdExecuteHelper2.executeCmdAdv(cmd2);
//        if (i2 != 0) {
//            MyLog.e(TAG, "安装测试apk失败！");
//        }
        buildApk();
    }

    private void buildApk() {
        // 打包apk
        CmdExecuter cmdExecuter = new CmdExecuter();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/app"),
                new CmdBean(GRADLE + " clean "),
                new CmdBean(GRADLE + " assembleDebug"),
        };
        int i = cmdExecuter.executeCmdAdv(cmd);
        if (i != 0) {
            MyLog.e(TAG, "打包测试apk失败！");
        }
        // 改名并移动至download文件夹
        File tempSaveFile = new File(apkPath);
        MyFileUtils.moveFile()
    }
}

package com.azx.mybuildassistant;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyLog;

class ApkBuilder {

    private static final String STAND_SDK_PROJECT_PATH = "/Users/zanewang/Downloads/WorkSpace/Company/Santiyun/Code/SDK/Demo/android/WS_ANDROID_MOMODemo";

    public void start(){
        CmdExecuteHelper executer = new CmdExecuteHelper();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + STAND_SDK_PROJECT_PATH),
                new CmdBean("adb uninstall com.tttrtclive.test"),
                new CmdBean("adb install " + apk),
        };
        int i2 = mCmdExecuteHelper2.executeCmdAdv(cmd2);
        if (i2 != 0) {
            MyLog.e(TAG, "安装测试apk失败！");
        }
    }
}

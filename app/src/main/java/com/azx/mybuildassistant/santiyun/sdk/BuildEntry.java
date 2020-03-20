package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.santiyun.BaseTask;

public class BuildEntry extends BaseTask {

    @Override
    public int start() {
//        BuildStandSdkTask task = new BuildStandSdkTask();
//        task.start();

        BuildQuanMinSDK task = new BuildQuanMinSDK();
        task.start();
        return 0;
    }
}

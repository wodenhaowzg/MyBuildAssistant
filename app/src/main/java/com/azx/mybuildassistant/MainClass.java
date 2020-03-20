package com.azx.mybuildassistant;

import com.azx.mybuildassistant.santiyun.sdk.BuildEntry;

public class MainClass {

    public static void main(String[] args) {

//        DocumentBuildTask mDocumentBuildTask = new DocumentBuildTask();
//        mDocumentBuildTask.start();

//        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
//        sdk.start();

//        BuildStandSdkTask sdk = new BuildStandSdkTask();
//        sdk.start();

//        ExtractExposedObjectTask task = new ExtractExposedObjectTask();
//        task.start();

        BuildEntry task = new BuildEntry();
        task.start();
    }
}

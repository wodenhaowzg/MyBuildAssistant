package com.azx.mybuildassistant;

import com.azx.mybuildassistant.santiyun.BuildPublishDemoApkTask;

public class MainClass {

    public static void main(String[] args) {

//        DocumentBuildTask mDocumentBuildTask = new DocumentBuildTask();
//        mDocumentBuildTask.start();

        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
        sdk.start();

//        BuildStandSdkTask sdk = new BuildStandSdkTask();
//        sdk.start();

//        ExtractExposedObjectTask task = new ExtractExposedObjectTask();
//        task.start();

//        CompileClientKit task = new CompileClientKit();
//        task.start();
    }
}

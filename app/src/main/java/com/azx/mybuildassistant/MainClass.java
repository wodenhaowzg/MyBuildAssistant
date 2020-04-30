package com.azx.mybuildassistant;

import com.azx.mybuildassistant.santiyun.BuildPublishDemoApkTask;

public class MainClass {

    public static void main(String[] args) {

//        DocumentBuildTask mDocumentBuildTask = new DocumentBuildTask();
//        mDocumentBuildTask.start();

//        ExtractExposedObjectTask task = new ExtractExposedObjectTask();
//        task.start();

//        BuildEntry task = new BuildEntry();
//        task.start();
//
//        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
//        sdk.start();

        exePublish();
    }

    private static void exePublish() {
//        BuildStandSdkTask task = new BuildStandSdkTask();
//        task.start(new int[]{VersionSelect.STAND_FULL_V7_SDK, VersionSelect.STAND_VOICE_V7_SDK, VersionSelect.STAND_FULL_V8_SDK
//                , VersionSelect.STAND_VOICE_V8_SDK});

        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
//        BuildPublishDemoApkTask.SDK_CACHE = BuildStandSdkTask.SDK_CACHE_FILE;
//        BuildPublishDemoApkTask.SDK_VOICE_CACHE = BuildStandSdkTask.SDK_CACHE_VOICE_FILE;
        BuildPublishDemoApkTask.SDK_CACHE = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V2.9.0_Full_2020_04_30.aar";
        BuildPublishDemoApkTask.SDK_VOICE_CACHE = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V2.9.0_Voice_2020_04_30.aar";
        sdk.start();
    }
}

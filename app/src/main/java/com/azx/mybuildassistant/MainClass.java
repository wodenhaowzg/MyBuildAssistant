package com.azx.mybuildassistant;

import com.azx.mybuildassistant.santiyun.BuildPublishDemoApkTask;
import com.azx.mybuildassistant.santiyun.sdk.BuildStandSdkTask;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;

public class MainClass {

    public static void main(String[] args) {

//        DocumentBuildTask mDocumentBuildTask = new DocumentBuildTask();
//        mDocumentBuildTask.start();

//        ExtractExposedObjectTask task = new ExtractExposedObjectTask();
//        task.start();

        BuildStandSdkTask task = new BuildStandSdkTask();
        task.start();
//
//        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
//        sdk.start();

//        exePublish();
//
//        AdbConnecter adbConnecter = new AdbConnecter();
//        adbConnecter.startConnect();
    }

    private static void exePublish() {
        BuildStandSdkTask task = new BuildStandSdkTask();
        int[] versions = new int[]{
                VersionSelect.STAND_FULL_V7_SDK,
                VersionSelect.STAND_FULL_V8_SDK,
                VersionSelect.STAND_VOICE_V7_SDK,
                VersionSelect.STAND_VOICE_V8_SDK
        };
        task.start(versions);

//        task.restoreForFailed();
//
        BuildPublishDemoApkTask sdk = new BuildPublishDemoApkTask();
        BuildPublishDemoApkTask.SDK_CACHE = BuildStandSdkTask.SDK_CACHE_FILE;
        BuildPublishDemoApkTask.SDK_VOICE_CACHE = BuildStandSdkTask.SDK_CACHE_VOICE_FILE;
//        BuildPublishDemoApkTask.SDK_CACHE = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V3.0.0_Full_2020_06_09.aar";
//        BuildPublishDemoApkTask.SDK_VOICE_CACHE = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V3.0.0_Voice_2020_06_09.aar";
        sdk.setBuildApks(new int[]{
                BuildPublishDemoApkTask.DEMO_STAND_TEST,
                BuildPublishDemoApkTask.DEMO_ENTER_ROOM_TEST,
                BuildPublishDemoApkTask.DEMO_LIVE_CHAT,
                BuildPublishDemoApkTask.DEMO_AUDIO_CHAT,
                BuildPublishDemoApkTask.DEMO_VIDEO_CHAT,
        });
        sdk.start();
    }
}

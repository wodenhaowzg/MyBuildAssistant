package com.azx.mybuildassistant.santiyun;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.santiyun.bean.PublishDemoBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BuildPublishDemoApkTask extends SanTiYunBaseTask {

    private static final String TAG = BuildPublishDemoApkTask.class.getSimpleName();

    private static final String COMMON_PATH = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit";

    private static final String APK_OUTPUT_PATH = COMMON_PATH + "/Publish";
    public static String SDK_CACHE = MACHINE_PATH + "/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/" +
            "TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V2.8.0_Full_2020_04_20.aar";
    public static String SDK_VOICE_CACHE = MACHINE_PATH + "/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/" +
            "TTTRtcEngine_AndroidKit/SDK_CACHE/3T_Native_SDK_for_Android_V2.8.0_Full_2020_04_20.aar";
    private static final String SDK_NEW_NAME = "/TTT_SDK.aar";


    private static final String APK_SRC_NAME = File.separator + "app-debug.apk";
    private static final String APK_SRC_PATH = File.separator + "build" + File.separator + "outputs" + File.separator + "apk"
            + File.separator + "debug" + APK_SRC_NAME;

    private static final String LIVE_OUTPUT_APK_PATH = LIVE_PROJECT_PATH + APK_SRC_PATH;
    private static final String LIVE_APK_TARGET_NAME = File.separator + "连麦直播.apk";

    private static final String VIDEO_CHAT_OUTPUT_APK_PATH = VIDEO_CHAT_PROJECT_PATH + APK_SRC_PATH;
    private static final String VIDEO_CHAT_APK_TARGET_NAME = File.separator + "视频通话.apk";

    private static final String AUDIO_CHAT_OUTPUT_APK_PATH = AUDIO_CHAT_PROJECT_PATH + APK_SRC_PATH;
    private static final String AIDEO_CHAT_APK_TARGET_NAME = File.separator + "音频通话.apk";

    @Override
    public int start() {
        // Delete old sdk
        String live_des_dir = LIVE_PROJECT_PATH + "/libs";
        if (!deleteOldSdkFile(live_des_dir)) return 0;
        String video_des_dir = VIDEO_CHAT_PROJECT_PATH + "/libs";
        if (!deleteOldSdkFile(video_des_dir)) return 0;
        String audio_des_dir = AUDIO_CHAT_PROJECT_PATH + "/libs";
        if (!deleteOldSdkFile(audio_des_dir)) return 0;

        // Copy new sdk
        if (!copySdkFile(SDK_CACHE, live_des_dir)) return 0;
        if (!copySdkFile(SDK_CACHE, video_des_dir)) return 0;
        if (!copySdkFile(SDK_VOICE_CACHE, audio_des_dir)) return 0;

        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        CmdBean[] cmd0 = new CmdBean[]{
                new CmdBean("rm -rf " + APK_OUTPUT_PATH),
                new CmdBean("mkdir " + APK_OUTPUT_PATH),
        };
        mCmdExecuteHelper.executeCmdAdv(cmd0);

        mCmdExecuteHelper = new CmdExecuteHelper();
        PublishDemoBean live_bean = new PublishDemoBean(LIVE_PROJECT_PATH, LIVE_OUTPUT_APK_PATH, APK_SRC_NAME, LIVE_APK_TARGET_NAME);
        CmdBean[] cmdBeans = buildCmd(live_bean);
        mCmdExecuteHelper.executeCmdAdv(cmdBeans);

        mCmdExecuteHelper = new CmdExecuteHelper();
        PublishDemoBean video_chat_bean = new PublishDemoBean(VIDEO_CHAT_PROJECT_PATH, VIDEO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, VIDEO_CHAT_APK_TARGET_NAME);
        CmdBean[] cmdBeans2 = buildCmd(video_chat_bean);
        mCmdExecuteHelper.executeCmdAdv(cmdBeans2);

        mCmdExecuteHelper = new CmdExecuteHelper();
        PublishDemoBean audio_chat_bean = new PublishDemoBean(AUDIO_CHAT_PROJECT_PATH, AUDIO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, AIDEO_CHAT_APK_TARGET_NAME);
        CmdBean[] cmdBeans3 = buildCmd(audio_chat_bean);
        mCmdExecuteHelper.executeCmdAdv(cmdBeans3);

        mCmdExecuteHelper = new CmdExecuteHelper();
        CmdBean[] cmd4 = new CmdBean[]{
                new CmdBean("open " + APK_OUTPUT_PATH),
        };
        mCmdExecuteHelper.executeCmdAdv(cmd4);
        return 0;
    }

    private boolean deleteOldSdkFile(String dirPath) {
        File desDir = new File(dirPath);
        if (!desDir.exists() && !desDir.isDirectory()) {
            MyLog.error(TAG, "Des dir has not exist! copy failed!");
            return false;
        }

        File[] files = desDir.listFiles();
        if (files == null || files.length <= 0) {
            MyLog.d(TAG, "Des dir has not an old sdk file, skip this");
            return false;
        }

        for (File file : files) {
            if (file.getName().endsWith("aar")) {
                boolean delete = file.delete();
                if (delete) {
                    MyLog.d(TAG, "Old sdk file delete successfully!");
                    break;
                }
            }
        }
        return true;
    }

    private CmdBean[] buildCmd(PublishDemoBean bean) {
        return new CmdBean[]{
                new CmdBean("cd " + bean.project_path),
                new CmdBean(GRADLE + " clean"),
                new CmdBean(GRADLE + " assembleDebug"),
                new CmdBean("cp " + bean.output_apk_path + " " + APK_OUTPUT_PATH),
                new CmdBean("mv " + APK_OUTPUT_PATH + bean.src_apk_name + " " + APK_OUTPUT_PATH + bean.target_apk_name),
        };
    }

    private boolean copySdkFile(String sdkFilePath, String targetFileDirPath) {
        File sdkFile = new File(sdkFilePath);
        if (!sdkFile.exists() && !sdkFile.isFile()) {
            MyLog.error(TAG, "SDK file not exist! copy failed!");
            return false;
        }

        if (!sdkFile.getName().endsWith("aar")) {
            MyLog.error(TAG, "SDK file not aar! copy failed!");
            return false;
        }

        File desFile = new File(targetFileDirPath + SDK_NEW_NAME);
        try {
            Files.copy(sdkFile.toPath(), desFile.toPath());
        } catch (IOException e) {
            MyLog.error(TAG, "SDK file copy failed! exception : " + e.getLocalizedMessage());
            return false;
        }
        MyLog.d(TAG, "Copy sdk file Successfully!");
        return true;
    }
}

package com.azx.mybuildassistant.santiyun;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.santiyun.bean.PublishDemoBean;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BuildPublishDemoApkTask extends SanTiYunBaseTask {

    private static final String TAG = BuildPublishDemoApkTask.class.getSimpleName();

    public static final int DEMO_STAND_TEST = 1;
    public static final int DEMO_ENTER_ROOM_TEST = 2;
    public static final int DEMO_LIVE_CHAT = 3;
    public static final int DEMO_AUDIO_CHAT = 4;
    public static final int DEMO_VIDEO_CHAT = 5;

    private static final String COMMON_PATH = BaseTask.MACHINE_PATH + File.separator + "Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit"; // FIXME 硬编路径

    private static final String APK_OUTPUT_PATH = COMMON_PATH + File.separator + "Publish";
    public static String SDK_CACHE, SDK_VOICE_CACHE;
    private static final String SDK_NEW_NAME = File.separator + "TTT_SDK.aar";

    private static final String APK_SRC_NAME = File.separator + "app-debug.apk";
    private static final String APK_SRC_PATH = File.separator + "build" + File.separator + "outputs" + File.separator + "apk" + File.separator + "debug" + APK_SRC_NAME;

    private static final String LIVE_OUTPUT_APK_PATH = LIVE_PROJECT_PATH + APK_SRC_PATH;
    private static final String LIVE_APK_TARGET_NAME = File.separator + "连麦直播.apk";

    private static final String VIDEO_CHAT_OUTPUT_APK_PATH = VIDEO_CHAT_PROJECT_PATH + APK_SRC_PATH;
    private static final String VIDEO_CHAT_APK_TARGET_NAME = File.separator + "视频通话.apk";

    private static final String AUDIO_CHAT_OUTPUT_APK_PATH = AUDIO_CHAT_PROJECT_PATH + APK_SRC_PATH;
    private static final String AIDEO_CHAT_APK_TARGET_NAME = File.separator + "音频通话.apk";

    private static final String TEST_SDK_PROJECT_OUTPUT_APK_PATH = TEST_SDK_PROJECT_PATH + APK_SRC_PATH;
    private static final String TEST_SDK_PROJECT_APK_TARGET_NAME = File.separator + "测试入会Demo.apk";

    private static final String STAND_SDK_APP_PROJECT_OUTPUT_APK_PATH = STAND_SDK_APP_PROJECT_PATH + APK_SRC_PATH;
    private static final String STAND_SDK_APP_PROJECT_APK_TARGET_NAME = File.separator + "测试连麦Demo.apk";

    private int[] buildApks;

    public void setBuildApks(int[] apks) {
        buildApks = apks;
    }

    @Override
    public int start() {
        for (int buildApk : buildApks) {
            CmdExecuter cmdExecuter;
            switch (buildApk) {
                case DEMO_STAND_TEST:
                    cmdExecuter = new CmdExecuter();
                    PublishDemoBean stand_sdk_bean = new PublishDemoBean(STAND_SDK_APP_PROJECT_PATH, STAND_SDK_APP_PROJECT_OUTPUT_APK_PATH, APK_SRC_NAME, STAND_SDK_APP_PROJECT_APK_TARGET_NAME);
                    CmdBean[] cmdBeans5 = buildCmd(stand_sdk_bean);
                    cmdExecuter.executeCmdAdv(cmdBeans5);
                    break;
                case DEMO_ENTER_ROOM_TEST:
                    cmdExecuter = new CmdExecuter();
                    PublishDemoBean test_sdk_bean = new PublishDemoBean(TEST_SDK_PROJECT_PATH, TEST_SDK_PROJECT_OUTPUT_APK_PATH, APK_SRC_NAME, TEST_SDK_PROJECT_APK_TARGET_NAME);
                    CmdBean[] cmdBeans4 = buildCmd(test_sdk_bean);
                    cmdExecuter.executeCmdAdv(cmdBeans4);
                    break;
                case DEMO_LIVE_CHAT:
                    String live_des_dir = LIVE_PROJECT_PATH + "/libs";
                    if (!deleteOldSdkFile(live_des_dir)) return 0;
                    if (!copySdkFile(SDK_CACHE, live_des_dir)) return 0;

                    cmdExecuter = new CmdExecuter();
                    PublishDemoBean live_bean = new PublishDemoBean(LIVE_PROJECT_PATH, LIVE_OUTPUT_APK_PATH, APK_SRC_NAME, LIVE_APK_TARGET_NAME);
                    CmdBean[] cmdBeans = buildCmd(live_bean);
                    cmdExecuter.executeCmdAdv(cmdBeans);
                    break;
                case DEMO_VIDEO_CHAT:
                    String video_des_dir = VIDEO_CHAT_PROJECT_PATH + "/libs";
                    if (!deleteOldSdkFile(video_des_dir)) return 0;
                    if (!copySdkFile(SDK_CACHE, video_des_dir)) return 0;

                    cmdExecuter = new CmdExecuter();
                    PublishDemoBean video_chat_bean = new PublishDemoBean(VIDEO_CHAT_PROJECT_PATH, VIDEO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, VIDEO_CHAT_APK_TARGET_NAME);
                    CmdBean[] cmdBeans2 = buildCmd(video_chat_bean);
                    cmdExecuter.executeCmdAdv(cmdBeans2);
                    break;
                case DEMO_AUDIO_CHAT:
                    String audio_des_dir = AUDIO_CHAT_PROJECT_PATH + "/libs";
                    if (!deleteOldSdkFile(audio_des_dir)) return 0;
                    if (!copySdkFile(SDK_VOICE_CACHE, audio_des_dir)) return 0;

                    cmdExecuter = new CmdExecuter();
                    PublishDemoBean audio_chat_bean = new PublishDemoBean(AUDIO_CHAT_PROJECT_PATH, AUDIO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, AIDEO_CHAT_APK_TARGET_NAME);
                    CmdBean[] cmdBeans3 = buildCmd(audio_chat_bean);
                    cmdExecuter.executeCmdAdv(cmdBeans3);
                    break;
            }
        }

//        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
//        CmdBean[] cmd4 = new CmdBean[]{
//                new CmdBean("open " + APK_OUTPUT_PATH),
//        };
//        mCmdExecuteHelper.executeCmdAdv(cmd4);
//
//        // Delete old sdk
//        String live_des_dir = LIVE_PROJECT_PATH + "/libs";
//        if (!deleteOldSdkFile(live_des_dir)) return 0;
//        String video_des_dir = VIDEO_CHAT_PROJECT_PATH + "/libs";
//        if (!deleteOldSdkFile(video_des_dir)) return 0;
//        String audio_des_dir = AUDIO_CHAT_PROJECT_PATH + "/libs";
//        if (!deleteOldSdkFile(audio_des_dir)) return 0;
//
//        // Copy new sdk
//        if (!copySdkFile(SDK_CACHE, live_des_dir)) return 0;
//        if (!copySdkFile(SDK_CACHE, video_des_dir)) return 0;
//        if (!copySdkFile(SDK_VOICE_CACHE, audio_des_dir)) return 0;
//
//        mCmdExecuteHelper = new CmdExecuteHelper();
//        CmdBean[] cmd0 = new CmdBean[]{
//                new CmdBean("rm -rf " + APK_OUTPUT_PATH),
//                new CmdBean("mkdir " + APK_OUTPUT_PATH),
//        };
//        mCmdExecuteHelper.executeCmdAdv(cmd0);
//
//        mCmdExecuteHelper = new CmdExecuteHelper();
//        PublishDemoBean live_bean = new PublishDemoBean(LIVE_PROJECT_PATH, LIVE_OUTPUT_APK_PATH, APK_SRC_NAME, LIVE_APK_TARGET_NAME);
//        CmdBean[] cmdBeans = buildCmd(live_bean);
//        mCmdExecuteHelper.executeCmdAdv(cmdBeans);
//
//        mCmdExecuteHelper = new CmdExecuteHelper();
//        PublishDemoBean video_chat_bean = new PublishDemoBean(VIDEO_CHAT_PROJECT_PATH, VIDEO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, VIDEO_CHAT_APK_TARGET_NAME);
//        CmdBean[] cmdBeans2 = buildCmd(video_chat_bean);
//        mCmdExecuteHelper.executeCmdAdv(cmdBeans2);
//
//        mCmdExecuteHelper = new CmdExecuteHelper();
//        PublishDemoBean audio_chat_bean = new PublishDemoBean(AUDIO_CHAT_PROJECT_PATH, AUDIO_CHAT_OUTPUT_APK_PATH, APK_SRC_NAME, AIDEO_CHAT_APK_TARGET_NAME);
//        CmdBean[] cmdBeans3 = buildCmd(audio_chat_bean);
//        mCmdExecuteHelper.executeCmdAdv(cmdBeans3);
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

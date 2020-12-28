package com.azx.mybuildassistant;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.ftp.SFTPUtilHelper;
import com.azx.mybuildassistant.santiyun.sdk.BuildStandSdkTask;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;
import com.azx.mybuildassistant.utils.MyTextUtils;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;

import static com.azx.mybuildassistant.Constants.GRADLE;
import static com.azx.mybuildassistant.Constants.MACHINE_PATH;

class ApkBuilder {
    private static final String TAG = "ApkBuilder";

    private static final String TEMP_DIR = Constants.OWNER_PROJECT_PATH + "/apkBuilder_temp";

    private static final String APK_SAVE_DIR_FULL_PATH = MACHINE_PATH + "/Downloads/WorkSpace/Company/TAL/ApkCenter";
    private static final String STAND_SDK_PROJECT_PATH = MACHINE_PATH + "/Downloads/WorkSpace/Company/Santiyun/Code/SDK/Demo/android/WS_ANDROID_MOMODemo";
    private static final String STAND_SDK_PROJECT_DEMO_SAVE_PATH = "/TestFullDemo";
    private static final String STAND_SDK_PROJECT_DEMO_NAME = "CoreRTC_SDK_Android-Demo";
    private static final String ADAPTER_SDK_PROJECT_PATH = MACHINE_PATH + "/Downloads/WorkSpace/Company/TAL/Code/Demo/xrtc_publish_android_release";
    private static final String ADAPTER_SDK_PROJECT_DEMO_SAVE_PATH = "/TestFullAdapterDemo";
    private static final String ADAPTER_SDK_PROJECT_DEMO_NAME = "TalRTC_SDK_Android-Demo";
    // debug
    private static final String APK_DEBUG_OUTPUT_DIR = "/app/build/outputs/apk/debug";
    private static final String APK_DEBUG_NAME = "/app-debug.apk";
    private static final String APK_DEBUG_FULL_PATH = APK_DEBUG_OUTPUT_DIR + APK_DEBUG_NAME;
    // release
    private static final String APK_RELEASE_OUTPUT_DIR = "/app/build/outputs/apk/release";
    private static final String APK_RELEASE_NAME = "/app-release.apk";
    private static final String APK_RELEASE_FULL_PATH = APK_RELEASE_OUTPUT_DIR + APK_RELEASE_NAME;

    private static final String SFTP_SERVER_ADDRESS = "101.133.236.102";
    private static final int SFTP_SERVER_PORT = 52300;
    private static final String SFTP_SERVER_USERNAME = "admin_android";
    private static final String SFTP_SERVER_PASSWORD = "Qe0Vbx81mkaS";
    private static final String SFTP_SERVER_BASE_DES_DIR_PATH = "/admin_android/android";
    private static final String SFTP_SERVER_DIR_PATH = "version_update";

    //        private static final String SFTP_SERVER_DIR_PATH = "version_update_test";
    private static final String SFTP_SERVER_FILE_NAME = "测试-连麦直播(25).apk";

    public void start() {
        packTestFullDemo();
//        packTestFullAdapterDemo();
    }


    private void packTestFullDemo(){
        String configFilePath = "/app/src/main/java/com/tttrtclive/LocalConfig.java";
        // 修改代码
        modifyCode(configFilePath);
        // 执行打包
        packTestFullApk(STAND_SDK_PROJECT_PATH, STAND_SDK_PROJECT_DEMO_SAVE_PATH, STAND_SDK_PROJECT_DEMO_NAME, true, true);
        // 还原代码
        resetCode(configFilePath);
    }

    private void packTestFullAdapterDemo(){
        // 执行打包sdk
        BuildStandSdkTask task = new BuildStandSdkTask();
        task.start();
        // 执行打包apk
        packTestFullApk(ADAPTER_SDK_PROJECT_PATH, ADAPTER_SDK_PROJECT_DEMO_SAVE_PATH, ADAPTER_SDK_PROJECT_DEMO_NAME, false, false);
    }

    /**
     * @param projectPath    项目的绝对路径
     * @param apkSaveDirName apk最终存放的绝对路径
     * @param apkName        apk最终的名称
     * @param upload         是否上传至ftp服务器
     */
    private void packTestFullApk(String projectPath, String apkSaveDirName, String apkName, boolean upload, boolean releaseVersion) {
        String apkSrcFullPath;
        if (releaseVersion) {
            apkSrcFullPath = projectPath + APK_RELEASE_OUTPUT_DIR + APK_RELEASE_NAME;
        } else {
            apkSrcFullPath = projectPath + APK_DEBUG_OUTPUT_DIR + APK_DEBUG_NAME;
        }
        String apkSaveDirFullPath = APK_SAVE_DIR_FULL_PATH + apkSaveDirName;

        // 打包
        boolean buildReslut = buildApk(projectPath, apkSrcFullPath, releaseVersion);
        if (!buildReslut) {
            return;
        }

        if (upload) {
            // 上传
            uploadApk(apkSrcFullPath);
        }
        // 将安装包移动到指定位置存放
        moveApk(apkSrcFullPath, apkSaveDirFullPath, apkName);
    }

    private void modifyCode(String configFilePath) {
        boolean moveFile = MyFileUtils.copyFile(STAND_SDK_PROJECT_PATH + configFilePath, TEMP_DIR + configFilePath);
        if (!moveFile) {
            return;
        }

        MyFileUtils.modifyFileContent(STAND_SDK_PROJECT_PATH + configFilePath, line -> {
            if (line.contains("mPublishVersion")) {
                return "public static boolean mPublishVersion = true;";
            }
            return line;
        });
    }

    private void resetCode(String configFilePath) {
        boolean moveFile = MyFileUtils.moveFile(TEMP_DIR + configFilePath, STAND_SDK_PROJECT_PATH + configFilePath);
        if (!moveFile) {
            return;
        }
    }

    private boolean buildApk(String projectPath, String apkSrcFullPath, boolean releaseVersion) {
        File apkFile = new File(apkSrcFullPath);
        if (apkFile.exists()) {
            return true;
        }

        String cmdStr = "assembleDebug";
        if (releaseVersion) {
            cmdStr = "assembleRelease";
        }

        // 打包apk
        CmdExecuter cmdExecuter = new CmdExecuter();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + projectPath + "/app"),
                new CmdBean(GRADLE + " clean "),
                new CmdBean(GRADLE + " " + cmdStr),
        };
        int buildReslut = cmdExecuter.executeCmdAdv(cmd);
        if (buildReslut != 0) {
            MyLog.e(TAG, "打包测试apk失败！");
            return false;
        }
        return true;
    }

    private void uploadApk(String apkSrcFullPath) {
        SFTPUtilHelper sftpUtilHelper = new SFTPUtilHelper();
        int login = sftpUtilHelper.login(SFTP_SERVER_ADDRESS, SFTP_SERVER_PORT, SFTP_SERVER_USERNAME, SFTP_SERVER_PASSWORD);
        if (login != 0) {
            MyLog.e(TAG, "登陆SFTP服务器失败，错误码：" + login);
            return;
        }

        // 先删除老的文件
        try {
            sftpUtilHelper.deleteDir(SFTP_SERVER_BASE_DES_DIR_PATH + "/" + SFTP_SERVER_DIR_PATH);
        } catch (SftpException e) {
            MyLog.w(TAG, "删除server上存放apk文件的目录失败 : " + e.getLocalizedMessage());
        }

        try {
            sftpUtilHelper.upload(SFTP_SERVER_BASE_DES_DIR_PATH, SFTP_SERVER_DIR_PATH, SFTP_SERVER_FILE_NAME, apkSrcFullPath, new SftpProgressMonitor() {

                private long mApkSize;
                private long mUploadSize;

                @Override
                public void init(int op, String src, String dest, long max) {
                    System.out.println("op : " + op + " | src : " + src + " | dest : " + dest + " | max : " + max);
                    mApkSize = max;
                }

                @Override
                public boolean count(long count) {  // 这里返回false的话就停止上传了
                    mUploadSize += count;
                    float progress = (float) mUploadSize / mApkSize;
                    System.out.println("上传进度 : " + progress * 100 + "%");
                    return true;
                }

                @Override
                public void end() {
                    System.out.println("上传完成");
                }
            });
        } catch (SftpException e) {
            e.printStackTrace();
            MyLog.e(TAG, "上传apk出现错误 : " + e.getLocalizedMessage());
        }

    }

    private void moveApk(String apkSrcFullPath, String apkSaveDirFullPath, String apkName) {
        String versionName = getVersionName();
        if (MyTextUtils.isEmpty(versionName)) {
            MyLog.e(TAG, "版本号获取失败");
            return;
        }

        String apkFullName = "/" + apkName + "_V" + versionName + "_Full.apk";
        MyFileUtils.moveFile(apkSrcFullPath, apkSaveDirFullPath + apkFullName);
    }

    private String getVersionName() {
        String globalConfigFilePath = STAND_SDK_PROJECT_PATH + "/enterconfapi/src/main/java/com/wushuangtech/library/GlobalConfig.java";
        return MyFileUtils.getStrFromVariable(globalConfigFilePath, "SDK_VERSION_NUMBER");
    }
}

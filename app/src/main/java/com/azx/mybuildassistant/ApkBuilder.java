package com.azx.mybuildassistant;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.ftp.SFTPUtilHelper;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyLog;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class ApkBuilder {
    private static final String STAND_SDK_PROJECT_PATH = "/Users/zanewang/Downloads/WorkSpace/Company/Santiyun/Code/SDK/Demo/android/WS_ANDROID_MOMODemo";
    private static final String APK_OUTPUT_DIR = STAND_SDK_PROJECT_PATH + "/app/build/outputs/apk/debug";
    private static final String APK_NAME = "/app-debug.apk";
    private static final String GRADLE = "/Users/zanewang/.gradle/wrapper/dists/gradle-6.5-bin/6nifqtx7604sqp1q6g8wikw7p/gradle-6.5/bin/gradle";
    private static final String TAG = "ApkBuilder";

    private static final String SFTP_SERVER_ADDRESS = "101.133.236.102";
    private static final int SFTP_SERVER_PORT = 52300;
    private static final String SFTP_SERVER_USERNAME = "admin_android";
    private static final String SFTP_SERVER_PASSWORD = "Qe0Vbx81mkaS";
    private static final String SFTP_SERVER_BASE_DES_DIR_PATH = "/admin_android/android";
        private static final String SFTP_SERVER_DIR_PATH = "version_update";
//    private static final String SFTP_SERVER_DIR_PATH = "version_update_test";
    private static final String SFTP_SERVER_FILE_NAME = "测试-连麦直播(11).apk";

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
        File apkFile = new File(APK_OUTPUT_DIR + APK_NAME);
        if (!apkFile.exists()) {
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
                return;
            }
        }
        uploadApk();
    }

    private void uploadApk() {
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
            sftpUtilHelper.upload(SFTP_SERVER_BASE_DES_DIR_PATH, SFTP_SERVER_DIR_PATH, SFTP_SERVER_FILE_NAME, APK_OUTPUT_DIR + APK_NAME, new SftpProgressMonitor() {

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
}

package com.azx.mybuildassistant.tal;

import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;
import com.azx.mybuildassistant.utils.MyZipUtils;

import java.io.File;
import java.io.IOException;

class AARTask extends BaseTaskImpl {

    private static final String TAG = "AARTask";
    private static final String[] SO_FILES = new String[]{"libAudioDecoder.so", "libavrecoder.so", "libclientcore.so", "libcodec_ttt.so", "libDenoise.so", "libmyaudio_so.so", "libyuv_ttt.so"};
    private final String SO_V7_DIR = File.separator + "armeabi-v7a";
    private final String SO_V8_DIR = File.separator + "arm64-v8a";
    private final String JAR_FILE_NAME = File.separator + "classes.jar";

    private final String mAARFilePath;
    private final String mSrcDirPath;
    private final String mDesDirPath;  // FIXME HARD CODE

    public AARTask(String path) {
        mAARFilePath = path;
        mSrcDirPath = Constants.TAL_WORKSPACE + File.separator + "TTTRtcEngine_AndroidKit" + File.separator + "temp";
        mDesDirPath = Constants.TAL_WORKSPACE + "/xrtc_publish_android_release/sdk_folder/core";
    }

    @Override
    public int start() {
        unZipSDKFile(mAARFilePath);
        // 移动目标文件
        moveTargetSDKFile();
        return 0;
    }

    public void unZipSDKFile(String filePath) {
        MyLog.d(TAG, "AAR path : " + filePath);
        File file = new File(filePath);
        int count = 5;
        while (!file.exists()) {
            if (count == 0) {
                break;
            }

            MyLog.d(TAG, "目标文件不存在，等待创建中... : " + filePath);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count--;
        }

        if (!file.exists()) {
            return;
        }

        String parent = file.getParent();
        String unzipSaveDirPath = parent + File.separator + "temp";
        File desDir = new File(unzipSaveDirPath);
        MyLog.d(TAG, "The path of storage for unzip aar : " + unzipSaveDirPath);
        if (desDir.exists()) {
            boolean delete = MyFileUtils.deleteFileDir(desDir);
            if (!delete) {
                return;
            }
        }

        String desFilePath = unzipSaveDirPath + File.separator + file.getName();
        MyLog.d(TAG, "Src file path : " + filePath);
        MyLog.d(TAG, "Des file path : " + desFilePath);
        boolean b = MyFileUtils.copyFile(filePath, desFilePath);
        if (!b) {
            return;
        }

        File srcFile = new File(desFilePath);
        File zipFile = new File(unzipSaveDirPath, "temp.zip");
        boolean renameTo = srcFile.renameTo(zipFile);
        if (!renameTo) {
            return;
        }

        try {
            MyZipUtils.unZipFiles(zipFile.getAbsolutePath(), unzipSaveDirPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zipFile.delete();
    }

    private void moveTargetSDKFile() {
        MyFileUtils.moveFile(mSrcDirPath + JAR_FILE_NAME, mDesDirPath + JAR_FILE_NAME);
        for (String soFileName : SO_FILES) {
            MyFileUtils.moveFile(mSrcDirPath + File.separator + "jni" + SO_V7_DIR + File.separator + soFileName, mDesDirPath + SO_V7_DIR + File.separator + soFileName);
            MyFileUtils.moveFile(mSrcDirPath + File.separator + "jni" + SO_V8_DIR + File.separator + soFileName, mDesDirPath + SO_V8_DIR + File.separator + soFileName);
        }
    }
}

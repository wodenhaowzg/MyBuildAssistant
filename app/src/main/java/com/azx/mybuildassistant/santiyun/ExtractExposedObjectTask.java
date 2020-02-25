package com.azx.mybuildassistant.santiyun;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtractExposedObjectTask extends SanTiYunBaseTask {

    public final String mNewSdkPath = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/发布版/ExposedClass_Newest";
    public final String mOldSdkPath = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/发布版/ExposedClass_Old";

    private final String mEnterConfApiClassPrefix = ENTERCONFAPI_MODULE_PATH + JAVA_PATH + File.separator + "com" +
            File.separator + "wushuangtech" + File.separator + "expansion" + File.separator + "bean";

    private final String mWstechapiClassPrefix = WSTECHAPI_MODULE_PATH + JAVA_PATH + File.separator + "com" + File.separator +
            "wushuangtech" + File.separator + "wstechapi";

    private String[] mEnterConfApiClass = new String[]{mEnterConfApiClassPrefix + File.separator + "LocalAudioStats.java",
            mEnterConfApiClassPrefix + File.separator + "LocalVideoStats.java",
            mEnterConfApiClassPrefix + File.separator + "RemoteAudioStats.java",
            mEnterConfApiClassPrefix + File.separator + "RemoteVideoStats.java",
            mEnterConfApiClassPrefix + File.separator + "RtcStats.java",
            mEnterConfApiClassPrefix + File.separator + "ScreenRecordConfig.java",
            mEnterConfApiClassPrefix + File.separator + "TTTVideoCanvas.java",
            mEnterConfApiClassPrefix + File.separator + "VideoCompositingLayout.java"};

    private String[] mWstecnapiClass = new String[]{
            mWstechapiClassPrefix + File.separator + "model" + File.separator + "VideoCanvas.java",
            mWstechapiClassPrefix + File.separator + "model" + File.separator + "PublisherConfiguration.java",
            mWstechapiClassPrefix + File.separator + "TTTRtcEngine.java",
            mWstechapiClassPrefix + File.separator + "TTTRtcEngineEventHandler.java",
    };

    private List<String> mDatas;

    public ExtractExposedObjectTask() {
        mDatas = new ArrayList<>();
        mDatas.addAll(Arrays.asList(mEnterConfApiClass));
        mDatas.addAll(Arrays.asList(mWstecnapiClass));
    }

    @Override
    public int start() {
        task();
        return 0;
    }

    private void task() {
        int extractFilesResult = extractFiles(mNewSdkPath);
        if (extractFilesResult < 0) {
            return;
        }
        // 打开目标窗口
        executeCmd("open " + mNewSdkPath);
        // 打印路径，用比较器比较
        System.out.println(mNewSdkPath);
        System.out.println(mOldSdkPath);
    }

    private void task2(){
        extractFiles(mOldSdkPath);
        // 打印路径，用比较器比较
        System.out.println(mNewSdkPath);
        System.out.println(mOldSdkPath);
    }

    private int extractFiles(String desPath) {
        File desDir = new File(desPath);
        if (desDir.exists()) {
            int deleteResult = deleteDir(desDir);
            if (deleteResult < 0) {
                System.out.println("删除目录文件夹失败 " + desDir.getAbsolutePath());
                return -1;
            }
        }

        boolean mkdirs = desDir.mkdirs();
        if (!mkdirs) {
            System.out.println("创建目录文件夹失败 " + desDir.getAbsolutePath());
            return -2;
        }

        // 检查文件都是否存在
        for (String mData : mDatas) {
            File temp = new File(mData);
            if (!temp.exists()) {
                System.out.println("文件不存在 " + temp.getAbsolutePath());
                break;
            }
        }
        System.out.println("文件检查完毕，都存在");
        // 拷贝文件到目的路径
        for (String mData : mDatas) {
            File temp = new File(mData);
            System.out.println("拷贝文件 : " + temp.getName());
            File desFile = new File(desPath + File.separator + temp.getName());
            try {
                Files.copy(temp.toPath(), desFile.toPath());
            } catch (IOException e) {
                System.out.println("文件拷贝出错 : " + e.getLocalizedMessage());
                break;
            }
        }
        return 0;
    }

    private int deleteDir(File mDir) {
        File[] childFiles = mDir.listFiles();
        if (childFiles != null && childFiles.length > 0) {
            for (File childFile : childFiles) {
                if (childFile.isDirectory()) {
                    deleteDir(childFile);
                } else {
                    boolean delete = childFile.delete();
                    if (!delete) {
                        System.out.println("删除文件失败 " + childFile.getAbsolutePath());
                        return -1;
                    }
                }
            }
        }

        boolean delete = mDir.delete();
        if (delete) {
            return 0;
        } else {
            return -2;
        }
    }
}

package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionNumberChange;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.santiyun.sdk.module.BaseModule;
import com.azx.mybuildassistant.santiyun.sdk.module.FaceModule;
import com.azx.mybuildassistant.santiyun.sdk.module.IjkModule;
import com.azx.mybuildassistant.santiyun.sdk.module.RtmpModule;
import com.azx.mybuildassistant.santiyun.sdk.module.VideoModule;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;
import com.azx.mybuildassistant.utils.MyTextUtils;
import com.azx.mybuildassistant.utils.MyZipUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 打包 aar
 */
public class BuildStandSdkTask extends BuildBaseTaskImpl {

    private static final String AAR_SAVE_PATH = SANTIYUN_PATH + "/Code/TTTRtcEngine_AndroidKit";
    private static final String SDK_CACHE_PATH = SANTIYUN_PATH + "/Code/TTTRtcEngine_AndroidKit/SDK_CACHE";

    public static String SDK_CACHE_FILE;
    public static String SDK_CACHE_VOICE_FILE;

    private static final String AAR_OUTPUT_PATH = WSTECHAPI_MODULE_PATH + "/build/outputs/aar" + AAR_SRC;
    private static final String TAG = "BuildStandSdkTask";

    // UNITY
    private static final String UNITY_MODULE_SO = "/libTTTRtcEngine.so";
    // AUDIO_EFFECT
    private static final String AUDIO_EFFECT_MODULE_SO = "/libAudioEffect.so";

    private static final String GLOBAL_BRANCH_TAG = "int mBranch =";
    private List<BaseModule> moduleList;

    @Override
    public int start() {
        super.start();
//        VersionSelect buildStandSdkVersionSelect = new VersionSelect();
//        buildStandSdkVersionSelect.selectVersion(VersionSelect.XIAOYUN_SDK);
//        boolean b6 = buildStandSdkVersionSelect.changeBranchTag(globalConfigFilePath, GLOBAL_BRANCH_TAG);
//        if (!b6) {
//            MyLog.error(TAG, "startBuild -> changeBranchTag failed!");
//        }

        buildPublishSdk(new int[]{VersionSelect.STAND_HWL});
//        executeTestApk("3T_Native_SDK_for_Android_V2.9.6_Full_2020_05_15.aar");

//         出异常情况恢复代码
//        restoreForFailed();
        return 0;
    }

    // 每次发布标准SDK的时候，调用此方法进行多个版本的打包，版本号自+1
    public void start(int[] versions) {
        // 版本号+1
        VersionNumberChange versionNumberChange = new VersionNumberChange();
        boolean changeResult = versionNumberChange.changeVersionNumber();
        if (!changeResult) {
            MyLog.d(TAG, "版本修改失败！");
            return;
        }
        // 开始打包
        buildPublishSdk(versions);
    }

    public void restoreForFailed() {
        // 打包失败时，调用下面的代码，恢复被修改的文件内容
        moduleList = new ArrayList<>();
        moduleList.add(new VideoModule());
        moduleList.add(new RtmpModule());
        moduleList.add(new IjkModule());
        moduleList.add(new FaceModule());
        VersionSelect buildStandSdkVersionSelect = new VersionSelect();
        VersionSelect.VersionBean versionBean = buildStandSdkVersionSelect.selectVersion(VersionSelect.STAND_VOICE_V7_SDK);
        restoreStatus(versionBean);
    }

    private void buildPublishSdk(int[] versions) {
        moduleList = new ArrayList<>();
        moduleList.add(new VideoModule());
        moduleList.add(new RtmpModule());
        moduleList.add(new IjkModule());
        moduleList.add(new FaceModule());
        MyLog.d(TAG, "添加所有模块完毕...");

        for (int version : versions) {
            VersionSelect buildStandSdkVersionSelect = new VersionSelect();
            VersionSelect.VersionBean versionBean = buildStandSdkVersionSelect.selectVersion(version);
            if (version == VersionSelect.STAND_VOICE_V7_SDK || version == VersionSelect.STAND_VOICE_V8_SDK) {
                versionBean.voiceSdk = true;
            }

            String targetAarFileName = buildTargetAarName(versionBean);
            if (MyTextUtils.isEmpty(targetAarFileName)) {
                MyLog.error(this.getClass().getSimpleName(), "变量 targetAarFileName 构建失败！");
                System.exit(0);
            }

            File desAarFile = checkDesFileDirAar(AAR_SAVE_PATH, targetAarFileName);
            if (desAarFile == null) {
                MyLog.error(this.getClass().getSimpleName(), "变量 desAarFile 构建失败！");
                System.exit(0);
            }

            MyLog.d(TAG, "目的 SDK 开发包的文件名称为 : " + targetAarFileName);
            MyLog.d(TAG, "目的 SDK 开发包存放路径为 : " + desAarFile.getAbsolutePath());
            MyLog.d(TAG, "目的 SDK 开发包版本 : " + version);
            boolean build = startBuild(buildStandSdkVersionSelect, versionBean);
            if (!build) {
                MyLog.error(TAG, "startBuild -> Buile Task check failed!");
                restoreStatus(versionBean);
                return;
            }

            String finallyFilePath = desAarFile.getAbsolutePath();
            executeBuild(finallyFilePath, buildStandSdkVersionSelect, versionBean);
            restoreStatus(versionBean);
        }
    }

    private void unZipFile(String finallyFilePath) {
        new Thread(() -> {
            MyLog.d(TAG, "finallyFilePath : " + finallyFilePath);
            File file = new File(finallyFilePath);
            int count = 5;
            while (!file.exists()) {
                if (count == 0) {
                    break;
                }

                MyLog.d(TAG, "目标文件不存在，等待创建中... : " + finallyFilePath);
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
            String desPath = parent + File.separator + "temp";
            File desDir = new File(desPath);
            MyLog.d(TAG, "desDir : " + desDir.getAbsolutePath());
            if (desDir.exists()) {
                boolean delete = MyFileUtils.deleteFileDir(desDir);
                if (!delete) {
                    return;
                }
            }

            String desFile = desPath + File.separator + file.getName();
            MyLog.d(TAG, "finallyFilePath : " + finallyFilePath);
            MyLog.d(TAG, "desPath : " + desPath);
            boolean b = MyFileUtils.copyFile(finallyFilePath, desFile);
            if (!b) {
                return;
            }

            File srcFile = new File(desFile);
            File zipFile = new File(desPath, "temp.zip");
            boolean renameTo = srcFile.renameTo(zipFile);
            if (!renameTo) {
                return;
            }

            try {
                MyZipUtils.unZipFiles(zipFile.getAbsolutePath(), desPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            zipFile.delete();
        }).start();
    }

    private String buildTargetAarName(VersionSelect.VersionBean bean) {
        String name;
        String sdk_version_number = MyFileUtils.getStrFromVariable(globalConfigFilePath, "SDK_VERSION_NUMBER");
        if (sdk_version_number == null) {
            MyLog.error(TAG, "在GlobalConfig文件中，未找到变量 SDK_VERSION_NUMBER，请检查！");
            return null;
        }

        String sdk_version_date = MyFileUtils.getStrFromVariable(globalConfigFilePath, "SDK_VERSION_DATE");
        if (sdk_version_date == null) {
            MyLog.error(TAG, "在GlobalConfig文件中，未找到变量 SDK_VERSION_DATE，请检查！");
            return null;
        }

        String date = sdk_version_date.replaceAll("\\(", "").replaceAll("\\)", "");
        if (bean.voiceSdk) {
            if (bean.v8Module) {
                name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Voice_V8_" + date + ".aar";
            } else {
                name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Voice_" + date + ".aar";
            }
        } else {
            if (bean.v8Module) {
                name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Full_V8_" + date + ".aar";
            } else {
                name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Full_" + date + ".aar";
            }
        }
        return name;
    }

    private File checkDesFileDirAar(String saveAarPath, String targetAarFileName) {
        if (MyTextUtils.isEmpty(saveAarPath)) {
            MyLog.error(this.getClass().getSimpleName(), "checkDesFileDirAar -> 从 getAarSavePath 方法中获取保存路径失败！");
            return null;
        }

        String des_file = saveAarPath + File.separator + targetAarFileName;
        File file = new File(des_file);
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                MyLog.error(this.getClass().getSimpleName(), "checkDesFileDirAar -> 删除老的文件失败！");
                return null;
            }
        }
        return file;
    }

    private boolean startBuild(VersionSelect versionSelect, VersionSelect.VersionBean versionBean) {
        if (versionBean.voiceSdk) {
            versionBean.videoModule = false;
            versionBean.rtmpModule = false;
            versionBean.ijkModule = false;
        }

        File tempSave = new File(TEMP_SAVE);
        if (tempSave.exists()) {
            boolean b = MyFileUtils.deleteFileDir(tempSave);
            if (!b) {
                return false;
            }
        }

        boolean commonFiles = handleCommonFile(versionSelect);
        if (!commonFiles) {
            MyLog.error(TAG, "startBuild -> handleCommonFile failed!");
            return false;
        } else {
            MyLog.d(TAG, "成功备份所有要修改的文件...");
        }

        boolean v8Module = handleV8Module(versionBean);
        if (!v8Module) {
            MyLog.error(TAG, "startBuild -> handleV8Module failed!");
            return false;
        }

        for (BaseModule baseModule : moduleList) {
            boolean build = baseModule.changeCodeToBuild(versionBean);
            if (!build) {
                MyLog.error(TAG, "startBuild -> changeCodeToBuild failed!");
                return false;
            }
        }

        boolean otherModule = handleOtherModule(versionBean);
        if (!otherModule) {
            MyLog.error(TAG, "startBuild -> handleOtherModule failed!");
            return false;
        }

        boolean b5 = MyFileUtils.modifyFileContent(wstechBuildGradlePath, line -> {
            String newline = replaceDependencies(line);
            if (newline.contains(wstech_embed_ijk_java) ||
                    newline.contains(wstech_embed_ijk_exo)) {
                return newline.replaceAll("//", "");
            }
            return newline;
        });

        if (!b5) {
            MyLog.error(TAG, "startBuild -> 处理 wstechBuildGradlePath 文件代码失败!");
            return false;
        }
        return true;
    }

    private void executeBuild(String finallyFilePath, VersionSelect versionSelect, VersionSelect.VersionBean versionBean) {
        CmdExecuter cmdExecuter = new CmdExecuter();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/wstechapi"),
                new CmdBean(GRADLE + " clean "),
                new CmdBean(GRADLE + " assembleRelease"),
        };
        int i = cmdExecuter.executeCmdAdv(cmd);
        if (i != 0) {
            return;
        }

        boolean b = MyFileUtils.moveFile(AAR_OUTPUT_PATH, finallyFilePath);
        if (!b) {
            MyLog.error(TAG, "executeBuild -> 移动并修改产出aar名称失败");
        }

        if (versionSelect.version == VersionSelect.STAND_FULL_V7_SDK || versionSelect.version == VersionSelect.STAND_VOICE_V7_SDK) {
            File f = new File(finallyFilePath);
            String targetFile = SDK_CACHE_PATH + File.separator + f.getName();
            boolean move = MyFileUtils.copyFile(finallyFilePath, targetFile);
            if (!move) {
                throw new RuntimeException("executeBuild moveFile failed! finallyFilePath : " + finallyFilePath + " | targetFile : " + targetFile);
            } else {
                if (versionSelect.version == VersionSelect.STAND_FULL_V7_SDK) {
                    SDK_CACHE_FILE = targetFile;
                } else {
                    SDK_CACHE_VOICE_FILE = targetFile;
                }
            }
        }
    }

    private boolean handleCommonFile(VersionSelect buildStandSdkVersionSelect) {
        boolean b = MyFileUtils.copyFile(tttRtcEngineFilePath, TEMP_SAVE + tttRtcEngineFileName);
        if (!b) {
            MyLog.error(TAG, "startBuild -> copyFile tttRtcEngineFileName failed!");
            return false;
        }
        boolean b1 = MyFileUtils.copyFile(tttRtcEngineImplFilePath, TEMP_SAVE + tttRtcEngineImplFileName);
        if (!b1) {
            MyLog.error(TAG, "startBuild -> copyFile tttRtcEngineImplFileName failed!");
            return false;
        }
        boolean b2 = MyFileUtils.copyFile(wstechBuildGradlePath, TEMP_SAVE + wstechBuildGradleName);
        if (!b2) {
            MyLog.error(TAG, "startBuild -> copyFile wstechBuildGradleName failed!");
            return false;
        }
        boolean b3 = MyFileUtils.copyFile(globalConfigFilePath, TEMP_SAVE + globalConfigFileName);
        if (!b3) {
            MyLog.error(TAG, "startBuild -> copyFile globalConfigFileName failed!");
            return false;
        }
        boolean b4 = MyFileUtils.copyFile(unityFilePath, TEMP_SAVE + unityFileName);
        if (!b4) {
            MyLog.error(TAG, "startBuild -> copyFile unityFileName failed!");
            return false;
        }
        boolean b6 = buildStandSdkVersionSelect.changeBranchTag(globalConfigFilePath, GLOBAL_BRANCH_TAG);
        if (!b6) {
            MyLog.error(TAG, "startBuild -> changeBranchTag failed!");
            return false;
        }
        return true;
    }

    private boolean handleV8Module(VersionSelect.VersionBean versionBean) {
        if (!versionBean.v8Module) {
            return MyFileUtils.moveFileDir(LIB_ARM64_V8_PATH, TEMP_SAVE + LIB_ARM64_V8);
        }
        return true;
    }

    private boolean handleOtherModule(VersionSelect.VersionBean versionBean) {
        if (!versionBean.unityModule) {
            boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + UNITY_MODULE_SO, TEMP_SAVE + LIB_ARMEABI_V7 + UNITY_MODULE_SO);
            if (!b) {
                MyLog.error(TAG, "handleOtherModule -> 移动 UNITY_MODULE_SO 文件失败！");
                return false;
            }
        }

        if (!versionBean.audioEffect) {
            boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + AUDIO_EFFECT_MODULE_SO, TEMP_SAVE + LIB_ARMEABI_V7 + AUDIO_EFFECT_MODULE_SO);
            if (!b) {
                MyLog.error(TAG, "handleOtherModule -> 移动 AUDIO_EFFECT_MODULE_SO 文件失败！");
                return false;
            }
            if (versionBean.v8Module) {
                boolean b1 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + AUDIO_EFFECT_MODULE_SO, TEMP_SAVE + LIB_ARM64_V8 + AUDIO_EFFECT_MODULE_SO);
                if (!b1) {
                    MyLog.error(TAG, "handleOtherModule -> 移动 V8 AUDIO_EFFECT_MODULE_SO 文件失败！");
                    return false;
                }
            }
        }
        return true;
    }

    private void restoreStatus(VersionSelect.VersionBean versionBean) {
        if (!versionBean.unityModule) {
            MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + UNITY_MODULE_SO, LIB_ARMEABI_V7_PATH + UNITY_MODULE_SO);
        }

        if (!versionBean.audioEffect) {
            restoreV7MoveFile(AUDIO_EFFECT_MODULE_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(AUDIO_EFFECT_MODULE_SO);
            }
        }

        if (!versionBean.v8Module) {
            MyFileUtils.moveFileDir(TEMP_SAVE + LIB_ARM64_V8, LIB_ARM64_V8_PATH);
        }

        for (BaseModule baseModule : moduleList) {
            boolean build = baseModule.restoreCode(versionBean);
            if (!build) {
                MyLog.error(TAG, "restoreStatus -> restoreCode failed! " + baseModule);
            }
        }

        MyFileUtils.moveFile(TEMP_SAVE + tttRtcEngineFileName, tttRtcEngineFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + tttRtcEngineImplFileName, tttRtcEngineImplFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + wstechBuildGradleName, wstechBuildGradlePath);
        MyFileUtils.moveFile(TEMP_SAVE + globalConfigFileName, globalConfigFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + unityFileName, unityFilePath);
    }

    private void executeTestApk(String srcFileName){
        String srcPath = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/" + srcFileName;
        String desPath = STAND_SDK_PROJECT_PATH + "/app/libs/" + srcFileName;
        String buildFile = "build.gradle";
        startTestApk(srcPath, desPath, buildFile);
        MyFileUtils.moveFile(TEMP_SAVE + buildFile, STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE);
    }

    private void startTestApk(String srcPath, String desPath, String buildFile) {
        boolean backup = MyFileUtils.copyFile(STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE, TEMP_SAVE + buildFile);
        if (!backup) {
            MyLog.e(TAG, "备份build.gradle失败");
            return ;
        }

        // 拷贝sdk
        boolean copy = MyFileUtils.copyFile(srcPath, desPath);
        if (!copy) {
            MyLog.e(TAG, "拷贝aar失败！");
            return ;
        }

        // 修改build.gradle
        boolean modify = MyFileUtils.modifyFileContent(STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE, line -> {
            if (line.contains("fileTree")) {
                return "implementation fileTree(include: ['*.*'], dir: 'libs')";
            } else if(line.contains("wstechapi")){
                return "";
            }
            return line;
        });

        if (!modify) {
            MyLog.e(TAG, "修改build.gradle文件失败！");
            return ;
        }

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
            return ;
        }

        String apk = STAND_SDK_PROJECT_PATH + "/app/build/outputs/apk/debug/app-debug.apk";
        // 执行apk
        CmdExecuter cmdExecuter2 = new CmdExecuter();
        CmdBean[] cmd2 = new CmdBean[]{
                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/app"),
                new CmdBean("adb uninstall com.tttrtclive.test"),
                new CmdBean("adb install " + apk),
        };
        int i2 = cmdExecuter2.executeCmdAdv(cmd2);
        if (i2 != 0) {
            MyLog.e(TAG, "安装测试apk失败！");
        }
    }

    private void restoreV7MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + fileName, LIB_ARMEABI_V7_PATH + fileName);
    }

    private void restoreV8MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + fileName, LIB_ARM64_V8_PATH + fileName);
    }
}

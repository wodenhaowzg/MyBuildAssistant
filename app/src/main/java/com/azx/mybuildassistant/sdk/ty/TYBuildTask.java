package com.azx.mybuildassistant.sdk.ty;

import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.Task;
import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.sdk.SDKInfo;
import com.azx.mybuildassistant.sdk.module.BaseModule;
import com.azx.mybuildassistant.sdk.module.FaceModule;
import com.azx.mybuildassistant.sdk.module.IjkModule;
import com.azx.mybuildassistant.sdk.module.RtmpModule;
import com.azx.mybuildassistant.sdk.module.VideoModule;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;
import com.azx.mybuildassistant.utils.MyTextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TYBuildTask implements Task {

    private static final String TAG = TYBuildTask.class.getSimpleName();
    private static final String AAR_SAVE_PATH = Constants.TAL_WORKSPACE + "/TTTRtcEngine_AndroidKit";
    private static final String SDK_CACHE_PATH = Constants.TAL_WORKSPACE + "/TTTRtcEngine_AndroidKit/SDK_CACHE";
    private String AAR_OUTPUT_PATH;

    // AUDIO_EFFECT
    private static final String AUDIO_EFFECT_MODULE_SO = "/libAudioEffect.so";
    /**
     * 更改代码分支版本
     */
    private static final String GLOBAL_BRANCH_TAG = "int mBranch =";

    private List<BaseModule> mModuleList;
    private SDKInfo mSDKInfo;

    @Override
    public int start() {
        mSDKInfo = new SDKInfo("/Users/zanewang/Downloads/WorkSpace/MyGithubs/Android/AV/TTT_AVSDK");
        AAR_OUTPUT_PATH = mSDKInfo.WSTECHAPI_MODULE_PATH + "/build/outputs/aar" + mSDKInfo.AAR_SRC;

        boolean buildResult = startBuild(VersionSelect.CUSTOM_TY);
        if (!buildResult) {
            return -1;
        }

//         出异常情况恢复代码
//        restoreForFailed();
        return 0;
    }

    private boolean startBuild(int version) {
        mModuleList = new ArrayList<>();
        mModuleList.add(new VideoModule(mSDKInfo));
        mModuleList.add(new RtmpModule(mSDKInfo));
        mModuleList.add(new IjkModule(mSDKInfo));
        mModuleList.add(new FaceModule(mSDKInfo));
        MyLog.d(TAG, "添加所有模块完毕...");
        // 遍历要打包的所有版本
        VersionSelect buildStandSdkVersionSelect = new VersionSelect();
        VersionSelect.VersionBean versionBean = buildStandSdkVersionSelect.selectVersion(version);
        if (version == VersionSelect.STAND_VOICE_V7_SDK || version == VersionSelect.STAND_VOICE_V8_SDK) {
            versionBean.voiceSdk = true;
        }

        String targetAarFileName = buildTargetAarName(versionBean);
        if (MyTextUtils.isEmpty(targetAarFileName)) {
            MyLog.error(TAG, "变量 targetAarFileName 构建失败！");
            return false;
        }

        File desAarFile = checkDesFileDirAar(AAR_SAVE_PATH, targetAarFileName);
        if (desAarFile == null) {
            MyLog.error(TAG, "变量 desAarFile 构建失败！");
            return false;
        }

        String finallyFilePath = desAarFile.getAbsolutePath();
        MyLog.d(TAG, "目的 SDK 开发包的文件名称为 : " + targetAarFileName);
        MyLog.d(TAG, "目的 SDK 开发包存放路径为 : " + finallyFilePath);
        MyLog.d(TAG, "目的 SDK 开发包版本 : " + version);
        boolean build = prepareBuild(buildStandSdkVersionSelect, versionBean);
        if (!build) {
            MyLog.error(TAG, "Prepare build task failed!");
            restoreStatus(versionBean);
            return false;
        }
        executeBuild(finallyFilePath, buildStandSdkVersionSelect, versionBean);
        restoreStatus(versionBean);
        return true;
    }

    public void restoreForFailed() {
        // 打包失败时，调用下面的代码，恢复被修改的文件内容
        mModuleList = new ArrayList<>();
        mModuleList.add(new VideoModule(mSDKInfo));
        mModuleList.add(new RtmpModule(mSDKInfo));
        mModuleList.add(new IjkModule(mSDKInfo));
        mModuleList.add(new FaceModule(mSDKInfo));
        VersionSelect buildStandSdkVersionSelect = new VersionSelect();
        VersionSelect.VersionBean versionBean = buildStandSdkVersionSelect.selectVersion(VersionSelect.STAND_VOICE_V7_SDK);
        restoreStatus(versionBean);
    }

    /**
     * Setup 1: 构建产物 AAR 的文件名
     */
    private String buildTargetAarName(VersionSelect.VersionBean bean) {
        String name;
        String sdk_version_number = MyFileUtils.getStrFromVariable(mSDKInfo.globalConfigFilePath, "SDK_VERSION_NUMBER");
        if (sdk_version_number == null) {
            MyLog.error(TAG, "在GlobalConfig文件中，未找到变量 SDK_VERSION_NUMBER，请检查！" + mSDKInfo.globalConfigFilePath);
            return null;
        }

        String sdk_version_date = MyFileUtils.getStrFromVariable(mSDKInfo.globalConfigFilePath, "SDK_VERSION_DATE");
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

    /**
     * Setup 2: 检查构建产物存放目录中，是否已经存在相同名称的 AAR 文件，如果有删除老的
     *
     * @param saveAarPath       构建产物存放目录的绝对路径
     * @param targetAarFileName 构建产物 AAR 的文件名
     */
    private File checkDesFileDirAar(String saveAarPath, String targetAarFileName) {
        if (MyTextUtils.isEmpty(saveAarPath)) {
            MyLog.error(TAG, "saveAarPath 为空");
            return null;
        }

        String desFilePath = saveAarPath + File.separator + targetAarFileName;
        File file = new File(desFilePath);
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                MyLog.error(TAG, "删除老的AAR文件失败!");
                return null;
            }
        }
        return file;
    }

    /**
     * Setup 3: 开始构建的准备工作，即修改相应的代码，达到构建标准
     *
     * @param versionSelect 构建所需要的版本信息
     * @param versionBean   构建所需要的模块信息
     */
    private boolean prepareBuild(VersionSelect versionSelect, VersionSelect.VersionBean versionBean) {
        if (versionBean.voiceSdk) {
            versionBean.videoModule = false;
            versionBean.rtmpModule = false;
            versionBean.ijkModule = false;
        }

        File tempSave = new File(mSDKInfo.TEMP_SAVE);
        if (tempSave.exists()) {
            boolean b = MyFileUtils.deleteFileDir(tempSave);
            if (!b) {
                return false;
            }
        }

        boolean commonFiles = handleCommonFile(versionSelect);
        if (!commonFiles) {
            MyLog.error(TAG, "handleCommonFile failed!");
            return false;
        } else {
            MyLog.d(TAG, "成功备份所有要修改的文件...");
        }

        boolean v8Module = handleV8Module(versionBean);
        if (!v8Module) {
            MyLog.error(TAG, "handleV8Module failed!");
            return false;
        }

        for (BaseModule baseModule : mModuleList) {
            boolean build = baseModule.changeCodeToBuild(versionBean);
            if (!build) {
                MyLog.error(TAG, "changeCodeToBuild failed!");
                return false;
            }
        }

        boolean otherModule = handleOtherModule(versionBean);
        if (!otherModule) {
            MyLog.error(TAG, "handleOtherModule failed!");
            return false;
        }

        boolean b5 = MyFileUtils.modifyFileContent(mSDKInfo.wstechBuildGradlePath, line -> {
            String newline = mSDKInfo.replaceDependencies(line);
            if (newline.contains(mSDKInfo.wstech_embed_ijk_java) ||
                    newline.contains(mSDKInfo.wstech_embed_ijk_exo)) {
                return newline.replaceAll("//", "");
            }
            return newline;
        });

        if (!b5) {
            MyLog.error(TAG, "处理 wstechBuildGradlePath 文件代码失败!");
            return false;
        }
        return true;
    }

    /**
     * Setup 4: 开始构建
     *
     * @param finallyFilePath 构建产物 AAR 文件的绝对路径
     * @param versionSelect   构建所需要的版本信息
     * @param versionBean     构建所需要的模块信息
     */
    private void executeBuild(String finallyFilePath, VersionSelect versionSelect, VersionSelect.VersionBean versionBean) {
        CmdExecuter cmdExecuter = new CmdExecuter();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + mSDKInfo.STAND_SDK_PROJECT_PATH + "/wstechapi"),
                new CmdBean(Constants.GRADLE + " clean "),
                new CmdBean(Constants.GRADLE + " assembleDebug"),
        };
        int i = cmdExecuter.executeCmdAdv(cmd);
        if (i != 0) {
            MyLog.error(TAG, "assembleDebug 命令执行失败...");
            return;
        }

        boolean b = MyFileUtils.moveFile(AAR_OUTPUT_PATH, finallyFilePath);
        if (!b) {
            MyLog.error(TAG, "移动并修改产出aar名称失败");
            return;
        }

        if (versionSelect.version == VersionSelect.STAND_FULL_V7_SDK || versionSelect.version == VersionSelect.STAND_VOICE_V7_SDK) {
            File f = new File(finallyFilePath);
            String targetFile = SDK_CACHE_PATH + File.separator + f.getName();
            boolean move = MyFileUtils.copyFile(finallyFilePath, targetFile);
            if (!move) {
                MyLog.error(TAG, "Move file failed! finallyFilePath : " + finallyFilePath + " | targetFile : " + targetFile);
            }
        }
    }

    private boolean handleCommonFile(VersionSelect buildStandSdkVersionSelect) {
        boolean b = MyFileUtils.copyFile(mSDKInfo.tttRtcEngineFilePath, mSDKInfo.TEMP_SAVE + mSDKInfo.tttRtcEngineFileName);
        if (!b) {
            MyLog.error(TAG, "copyFile tttRtcEngineFileName failed!");
            return false;
        }
        boolean b1 = MyFileUtils.copyFile(mSDKInfo.tttRtcEngineImplFilePath, mSDKInfo.TEMP_SAVE + mSDKInfo.tttRtcEngineImplFileName);
        if (!b1) {
            MyLog.error(TAG, "copyFile tttRtcEngineImplFileName failed!");
            return false;
        }
        boolean b2 = MyFileUtils.copyFile(mSDKInfo.wstechBuildGradlePath, mSDKInfo.TEMP_SAVE + mSDKInfo.wstechBuildGradleName);
        if (!b2) {
            MyLog.error(TAG, "copyFile wstechBuildGradleName failed!");
            return false;
        }
        boolean b3 = MyFileUtils.copyFile(mSDKInfo.globalConfigFilePath, mSDKInfo.TEMP_SAVE + mSDKInfo.globalConfigFileName);
        if (!b3) {
            MyLog.error(TAG, "copyFile globalConfigFileName failed!");
            return false;
        }
        boolean b4 = MyFileUtils.copyFile(mSDKInfo.unityFilePath, mSDKInfo.TEMP_SAVE + mSDKInfo.unityFileName);
        if (!b4) {
            MyLog.error(TAG, "copyFile unityFileName failed!");
            return false;
        }
        boolean b6 = buildStandSdkVersionSelect.changeBranchTag(mSDKInfo.globalConfigFilePath, GLOBAL_BRANCH_TAG);
        if (!b6) {
            MyLog.error(TAG, "changeBranchTag failed!");
            return false;
        }
        return true;
    }

    private boolean handleV8Module(VersionSelect.VersionBean versionBean) {
        if (!versionBean.v8Module) {
            return MyFileUtils.moveFileDir(mSDKInfo.LIB_ARM64_V8_PATH, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8);
        }
        return true;
    }

    private boolean handleOtherModule(VersionSelect.VersionBean versionBean) {
        if (!versionBean.audioEffect) {
            boolean b = MyFileUtils.moveFile(mSDKInfo.LIB_ARMEABI_V7_PATH + AUDIO_EFFECT_MODULE_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + AUDIO_EFFECT_MODULE_SO);
            if (!b) {
                MyLog.error(TAG, "handleOtherModule -> 移动 AUDIO_EFFECT_MODULE_SO 文件失败！");
                return false;
            }
            if (versionBean.v8Module) {
                boolean b1 = MyFileUtils.moveFile(mSDKInfo.LIB_ARM64_V8_PATH + AUDIO_EFFECT_MODULE_SO, mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + AUDIO_EFFECT_MODULE_SO);
                if (!b1) {
                    MyLog.error(TAG, "handleOtherModule -> 移动 V8 AUDIO_EFFECT_MODULE_SO 文件失败！");
                    return false;
                }
            }
        }
        return true;
    }

    private void restoreStatus(VersionSelect.VersionBean versionBean) {
        if (!versionBean.audioEffect) {
            restoreV7MoveFile(AUDIO_EFFECT_MODULE_SO);
            if (versionBean.v8Module) {
                restoreV8MoveFile(AUDIO_EFFECT_MODULE_SO);
            }
        }

        if (!versionBean.v8Module) {
            MyFileUtils.moveFileDir(mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8, mSDKInfo.LIB_ARM64_V8_PATH);
        }

        for (BaseModule baseModule : mModuleList) {
            boolean build = baseModule.restoreCode(versionBean);
            if (!build) {
                MyLog.error(TAG, "restoreStatus -> restoreCode failed! " + baseModule);
            }
        }

        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.tttRtcEngineFileName, mSDKInfo.tttRtcEngineFilePath);
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.tttRtcEngineImplFileName, mSDKInfo.tttRtcEngineImplFilePath);
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.wstechBuildGradleName, mSDKInfo.wstechBuildGradlePath);
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.globalConfigFileName, mSDKInfo.globalConfigFilePath);
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.unityFileName, mSDKInfo.unityFilePath);
    }

    private void restoreV7MoveFile(String fileName) {
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARMEABI_V7 + fileName, mSDKInfo.LIB_ARMEABI_V7_PATH + fileName);
    }

    private void restoreV8MoveFile(String fileName) {
        MyFileUtils.moveFile(mSDKInfo.TEMP_SAVE + mSDKInfo.LIB_ARM64_V8 + fileName, mSDKInfo.LIB_ARM64_V8_PATH + fileName);
    }

//    private void executeTestApk(String srcFileName) {
//        String srcPath = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/" + srcFileName; // FIXME hard code
//        String desPath = STAND_SDK_PROJECT_PATH + "/app/libs/" + srcFileName;
//        String buildFile = "build.gradle";
//        startTestApk(srcPath, desPath, buildFile);
//        MyFileUtils.moveFile(TEMP_SAVE + buildFile, STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE);
//    }
//
//    private void startTestApk(String srcPath, String desPath, String buildFile) {
//        boolean backup = MyFileUtils.copyFile(STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE, TEMP_SAVE + buildFile);
//        if (!backup) {
//            MyLog.e(TAG, "备份build.gradle失败");
//            return;
//        }
//
//        // 拷贝sdk
//        boolean copy = MyFileUtils.copyFile(srcPath, desPath);
//        if (!copy) {
//            MyLog.e(TAG, "拷贝aar失败！");
//            return;
//        }
//
//        // 修改build.gradle
//        boolean modify = MyFileUtils.modifyFileContent(STAND_SDK_APP_PROJECT_BUILD_GRADLE_FILE, line -> {
//            if (line.contains("fileTree")) {
//                return "implementation fileTree(include: ['*.*'], dir: 'libs')";
//            } else if (line.contains("wstechapi")) {
//                return "";
//            }
//            return line;
//        });
//
//        if (!modify) {
//            MyLog.e(TAG, "修改build.gradle文件失败！");
//            return;
//        }
//
//        // 打包apk
//        CmdExecuter cmdExecuter = new CmdExecuter();
//        CmdBean[] cmd = new CmdBean[]{
//                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/app"),
//                new CmdBean(Constants.GRADLE + " clean "),
//                new CmdBean(Constants.GRADLE + " assembleDebug"),
//        };
//        int i = cmdExecuter.executeCmdAdv(cmd);
//        if (i != 0) {
//            MyLog.e(TAG, "打包测试apk失败！");
//            return;
//        }
//
//        String apk = STAND_SDK_PROJECT_PATH + "/app/build/outputs/apk/debug/app-debug.apk";
//        // 执行apk
//        cmdExecuter = new CmdExecuter();
//        CmdBean[] cmd2 = new CmdBean[]{
//                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/app"),
//                new CmdBean("adb uninstall com.tttrtclive.test"),
//                new CmdBean("adb install " + apk),
//        };
//        int i2 = cmdExecuter.executeCmdAdv(cmd2);
//        if (i2 != 0) {
//            MyLog.e(TAG, "安装测试apk失败！");
//        }
//    }
}

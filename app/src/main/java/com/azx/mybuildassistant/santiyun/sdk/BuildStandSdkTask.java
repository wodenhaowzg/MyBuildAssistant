package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.santiyun.sdk.helper.VersionSelect;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 打包 aar
 */
public class BuildStandSdkTask extends BuildBaseTaskImpl {

    private static final String AAR_SAVE_PATH = MACHINE_PATH + "/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit";
    private static final String AAR_OUTPUT_PATH = WSTECHAPI_MODULE_PATH + "/build/outputs/aar" + AAR_SRC;
    private static final String TAG = "BuildStandSdkTask";

    // UNITY
    private static final String UNITY_MODULE_SO = "/libTTTRtcEngine.so";
    // AUDIO_EFFECT
    private static final String AUDIO_EFFECT_MODULE_SO = "/libAudioEffect.so";

    private static final String GLOBAL_BRANCH_TAG = "int mBranch =";
    private VersionSelect.VersionBean versionBean;
    private VersionSelect buildStandSdkVersionSelect;
    private List<BaseModule> moduleList;

    private static final int BRANCH_VERSION = VersionSelect.CUSTOM_YQ;

    @Override
    public int start() {
        buildStandSdkVersionSelect = new VersionSelect();
        versionBean = buildStandSdkVersionSelect.selectVersion(BRANCH_VERSION);
        MyLog.d(TAG, "开始构建 SDK 开发包...");
        super.start();
        System.out.println("目的 SDK 开发包的文件名称为 : " + targetAarFileName);
        System.out.println("目的 SDK 开发包存放路径为 : " + desAarFile.getAbsolutePath());
        moduleList = new ArrayList<>();
        moduleList.add(new VideoModule());
        moduleList.add(new RtmpModule());
        moduleList.add(new IjkModule());
        moduleList.add(new FaceModule());
        System.out.println("添加所有模块完毕...");

        boolean build = startBuild();
        if (!build) {
            MyLog.e(TAG, "startBuild -> Buile Task check failed!");
            restoreStatus();
            return 0;
        }
        executeBuild();
        restoreStatus();
        System.out.println(TEMP_SAVE);
        return 0;
    }

    @Override
    protected String buildTargetAarName() {
        String name;
        String sdk_version_number = MyFileUtils.getStrFromVariable(globalConfigFilePath, "SDK_VERSION_NUMBER");
        if (sdk_version_number == null) {
            MyLog.e(TAG, "在GlobalConfig文件中，未找到变量 SDK_VERSION_NUMBER，请检查！");
            return null;
        }

        String sdk_version_date = MyFileUtils.getStrFromVariable(globalConfigFilePath, "SDK_VERSION_DATE");
        if (sdk_version_date == null) {
            MyLog.e(TAG, "在GlobalConfig文件中，未找到变量 SDK_VERSION_DATE，请检查！");
            return null;
        }

        String date = sdk_version_date.replaceAll("\\(", "").replaceAll("\\)", "");
        if (versionBean.voiceSdk) {
            name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Voice_" + date + ".aar";
        } else {
            name = "3T_Native_SDK_for_Android_V" + sdk_version_number + "_Full_" + date + ".aar";
        }
        return name;
    }

    @Override
    protected String getAarSavePath() {
        return AAR_SAVE_PATH;
    }

    private boolean startBuild() {
        if (versionBean.voiceSdk) {
            versionBean.videoModule = false;
            versionBean.rtmpModule = false;
            versionBean.ijkModule = false;
        }

        boolean commonFiles = handleCommonFile();
        if (!commonFiles) {
            MyLog.e(TAG, "startBuild -> handleCommonFile failed!");
            return false;
        } else {
            System.out.println("成功备份所有要修改的文件...");
        }

        boolean v8Module = handleV8Module();
        if (!v8Module) {
            MyLog.e(TAG, "startBuild -> handleV8Module failed!");
            return false;
        }

        for (BaseModule baseModule : moduleList) {
            boolean build = baseModule.changeCodeToBuild(versionBean);
            if (!build) {
                MyLog.e(TAG, "startBuild -> changeCodeToBuild failed!");
                return false;
            }
        }

        boolean otherModule = handleOtherModule();
        if (!otherModule) {
            MyLog.e(TAG, "startBuild -> handleOtherModule failed!");
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
            MyLog.e(TAG, "startBuild -> 处理 wstechBuildGradlePath 文件代码失败!");
            return false;
        }
        return true;
    }

    private void executeBuild() {
        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        CmdBean[] cmd = new CmdBean[]{
                new CmdBean("cd " + STAND_SDK_PROJECT_PATH + "/wstechapi", CMD_STOP_FLAG),
                new CmdBean(GRADLE + " clean ", CMD_STOP_FLAG),
                new CmdBean(GRADLE + " assembleRelease", CMD_STOP_FLAG),
                new CmdBean("cp " + AAR_OUTPUT_PATH + " " + AAR_SAVE_PATH, CMD_STOP_FLAG),
                new CmdBean("mv " + AAR_SAVE_PATH + AAR_SRC + " " + desAarFile, CMD_STOP_FLAG),
                new CmdBean("open " + AAR_SAVE_PATH, CMD_STOP_FLAG),
        };
        mCmdExecuteHelper.executeCmdAdv(cmd);
    }

    private boolean handleCommonFile() {
        boolean b = MyFileUtils.copyFile(tttRtcEngineFilePath, TEMP_SAVE + tttRtcEngineFileName);
        if (!b) {
            MyLog.e(TAG, "startBuild -> copyFile tttRtcEngineFileName failed!");
            return false;
        }
        boolean b1 = MyFileUtils.copyFile(tttRtcEngineImplFilePath, TEMP_SAVE + tttRtcEngineImplFileName);
        if (!b1) {
            MyLog.e(TAG, "startBuild -> copyFile tttRtcEngineImplFileName failed!");
            return false;
        }
        boolean b2 = MyFileUtils.copyFile(wstechBuildGradlePath, TEMP_SAVE + wstechBuildGradleName);
        if (!b2) {
            MyLog.e(TAG, "startBuild -> copyFile wstechBuildGradleName failed!");
            return false;
        }
        boolean b3 = MyFileUtils.copyFile(globalConfigFilePath, TEMP_SAVE + globalConfigFileName);
        if (!b3) {
            MyLog.e(TAG, "startBuild -> copyFile globalConfigFileName failed!");
            return false;
        }
        boolean b4 = MyFileUtils.copyFile(unityFilePath, TEMP_SAVE + unityFileName);
        if (!b4) {
            MyLog.e(TAG, "startBuild -> copyFile unityFileName failed!");
            return false;
        }
        boolean b6 = buildStandSdkVersionSelect.changeBranchTag(globalConfigFilePath, GLOBAL_BRANCH_TAG);
        if (!b6) {
            MyLog.e(TAG, "startBuild -> changeBranchTag failed!");
            return false;
        }
        return true;
    }

    private boolean handleV8Module() {
        if (!versionBean.v8Module) {
            return MyFileUtils.moveFileDir(LIB_ARM64_V8_PATH, TEMP_SAVE + LIB_ARM64_V8);
        }
        return true;
    }

    private boolean handleOtherModule() {
        if (!versionBean.unityModule) {
            boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + UNITY_MODULE_SO, TEMP_SAVE + LIB_ARMEABI_V7 + UNITY_MODULE_SO);
            if (!b) {
                MyLog.e(TAG, "handleOtherModule -> 移动 UNITY_MODULE_SO 文件失败！");
                return false;
            }
        }

        if (!versionBean.audioEffect) {
            boolean b = MyFileUtils.moveFile(LIB_ARMEABI_V7_PATH + AUDIO_EFFECT_MODULE_SO, TEMP_SAVE + LIB_ARMEABI_V7 + AUDIO_EFFECT_MODULE_SO);
            if (!b) {
                MyLog.e(TAG, "handleOtherModule -> 移动 AUDIO_EFFECT_MODULE_SO 文件失败！");
                return false;
            }
            if (versionBean.v8Module) {
                boolean b1 = MyFileUtils.moveFile(LIB_ARM64_V8_PATH + AUDIO_EFFECT_MODULE_SO, TEMP_SAVE + LIB_ARM64_V8 + AUDIO_EFFECT_MODULE_SO);
                if (!b1) {
                    MyLog.e(TAG, "handleOtherModule -> 移动 V8 AUDIO_EFFECT_MODULE_SO 文件失败！");
                    return false;
                }
            }
        }
        return true;
    }

    private void restoreStatus() {
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
                MyLog.e(TAG, "restoreStatus -> restoreCode failed!");
            }
        }

        MyFileUtils.moveFile(TEMP_SAVE + tttRtcEngineFileName, tttRtcEngineFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + tttRtcEngineImplFileName, tttRtcEngineImplFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + wstechBuildGradleName, wstechBuildGradlePath);
        MyFileUtils.moveFile(TEMP_SAVE + globalConfigFileName, globalConfigFilePath);
        MyFileUtils.moveFile(TEMP_SAVE + unityFileName, unityFilePath);
    }

    private void restoreV7MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARMEABI_V7 + fileName, LIB_ARMEABI_V7_PATH + fileName);
    }

    private void restoreV8MoveFile(String fileName) {
        MyFileUtils.moveFile(TEMP_SAVE + LIB_ARM64_V8 + fileName, LIB_ARM64_V8_PATH + fileName);
    }
}

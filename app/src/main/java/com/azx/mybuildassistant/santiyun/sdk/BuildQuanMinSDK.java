package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyFileUtils;

import java.io.File;

public class BuildQuanMinSDK extends BuildBaseTaskImpl {

    private String wstech_embed_clientso = "embed project(path: ':myclientso', configuration:'default')";
    private String wstech_link_clientso = "api project(':myclientso')";

    private String QM_STECH_MODULE_GRADLE_NAME = File.separator + "build.gradle";
    private String QM_STECH_MODULE_GRADLE_PATH = QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi" + QM_STECH_MODULE_GRADLE_NAME;

    private static final String AAR_OUTPUT_PATH = QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi/build/outputs/aar" + AAR_SRC;
    private static final String AAR_SAVE_PATH = MACHINE_PATH + "/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngineQM_AndroidKitWrap/TTTRtcEngineQM_AndroidKit";

    @Override
    public int start() {
        branch = Branch.QUANMIN;
        super.start();
        MyFileUtils.copyFile(QM_STECH_MODULE_GRADLE_PATH, TEMP_SAVE + QM_STECH_MODULE_GRADLE_NAME);

        MyFileUtils.modifyFileContent(QM_STECH_MODULE_GRADLE_PATH, line -> {
            String content = replaceDependencies(line);
            if (content.contains(wstech_link_clientso)) {
                return "//" + wstech_link_clientso;
            } else if (content.contains(wstech_embed_clientso)) {
                return content.replaceAll("//", "");
            }
            return content;
        });

        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        CmdBean[] cmd0 = new CmdBean[]{
                new CmdBean("cd " + QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi", CMD_STOP_FLAG),
                new CmdBean(GRADLE + " clean ", null),
                new CmdBean(GRADLE + " assembleRelease", null),
                new CmdBean("cp " + AAR_OUTPUT_PATH + " " + AAR_SAVE_PATH, CMD_STOP_FLAG),
                new CmdBean("mv " + AAR_SAVE_PATH + AAR_SRC + " " + desAarFile, CMD_STOP_FLAG),
                new CmdBean("open " + AAR_SAVE_PATH, CMD_STOP_FLAG),
        };
        mCmdExecuteHelper.executeCmdAdv(cmd0);

        MyFileUtils.moveFile(TEMP_SAVE + QM_STECH_MODULE_GRADLE_NAME, QM_STECH_MODULE_GRADLE_PATH);
        return 0;
    }

    @Override
    protected String buildTargetAarName() {
        return File.separator + "TTTRtcEngineQM_Android_" + format.format(System.currentTimeMillis()) + ".aar";
    }

    @Override
    protected String getAarSavePath() {
        return AAR_SAVE_PATH;
    }

}

package com.azx.mybuildassistant.santiyun;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyFileUtils;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * 打包 aar
 */
public class BuildStandSdkTask extends SanTiYunBaseTask {

    private SimpleDateFormat format = new SimpleDateFormat("YYYY_MM_dd");

    private String QM_STECH_MODULE_GRADLE = QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi/build.gradle";
    private static final String AAR_SRC = "/wstechapi-release.aar";
    private static final String AAR_OUTPUT_PATH = QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi/build/outputs/aar" + AAR_SRC;
    private static final String AAR_SAVE_PATH = MACHINE_PATH + "/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngineQM_AndroidKitWrap/TTTRtcEngineQM_AndroidKit";

    private String wstech_link_enterconfapi = "api project(':enterconfapi')";
    private String wstech_link_myaudio = "api project(':myaudio')";
    private String wstech_link_myvideo = "api project(':myvideo')";
    private String wstech_link_screen = "api project(':myandroidscreenrecordandcrop')";
    private String wstech_link_clientso = "api project(':myclientso')";
    private String wstech_embed_enterconfapi = "embed project(path: ':enterconfapi', configuration:'default')";
    private String wstech_embed_myaudio = "embed project(path: ':myaudio', configuration:'default')";
    private String wstech_embed_myvideo = "embed project(path: ':myvideo', configuration:'default')";
    private String wstech_embed_screen = "embed project(path: ':myandroidscreenrecordandcrop', configuration:'default')";
    private String wstech_embed_clientso = "embed project(path: ':myclientso', configuration:'default')";

    @Override
    public int start() {
        buildQM_SDK();
        return 0;
    }

    private void buildQM_SDK() {
        String AAR_DES = "/TTTRtcEngineQM_Android_" + format.format(System.currentTimeMillis());
        MyFileUtils.modifyFileContent(QM_STECH_MODULE_GRADLE, new MyFileUtils.FileContentListener() {
            @Override
            public String lineTextContent(String line) {
                if (line.contains("//apply plugin: 'com.kezong.fat-aar'")) {
                    return line.replaceAll("//", "");
                } else if (line.contains(wstech_link_enterconfapi)) {
                    return "//" + wstech_link_enterconfapi;
                } else if (line.contains(wstech_link_myaudio)) {
                    return "//" + wstech_link_myaudio;
                } else if (line.contains(wstech_link_myvideo)) {
                    return "//" + wstech_link_myvideo;
                } else if (line.contains(wstech_link_screen)) {
                    return "//" + wstech_link_screen;
                } else if (line.contains(wstech_link_clientso)) {
                    return "//" + wstech_link_clientso;
                } else if (line.contains(wstech_embed_enterconfapi) ||
                        line.contains(wstech_embed_myaudio) ||
                        line.contains(wstech_embed_myvideo) ||
                        line.contains(wstech_embed_screen) ||
                        line.contains(wstech_embed_clientso)) {
                    return line.replaceAll("//", "");
                }
                return line;
            }
        });

        String des_file = AAR_SAVE_PATH + AAR_DES + ".aar";
        File file = new File(des_file);
        if (file.exists()) {
            file.delete();
        }

        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        CmdBean[] cmd0 = new CmdBean[]{
                new CmdBean("cd " + QUANMIN_SDK_PROJECT_PATH + "/wsapi/wstechapi", CMD_STOP_FLAG),
                new CmdBean(GRADLE + " clean ", null),
                new CmdBean(GRADLE + " assembleRelease", null),
                new CmdBean("cp " + AAR_OUTPUT_PATH + " " + AAR_SAVE_PATH, CMD_STOP_FLAG),
                new CmdBean("mv " + AAR_SAVE_PATH + AAR_SRC + " " + des_file, CMD_STOP_FLAG),
                new CmdBean("open " + AAR_SAVE_PATH, CMD_STOP_FLAG),
        };
        mCmdExecuteHelper.executeCmdAdv(cmd0);
    }
}

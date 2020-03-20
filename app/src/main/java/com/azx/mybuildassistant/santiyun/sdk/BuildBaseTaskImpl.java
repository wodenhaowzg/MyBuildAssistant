package com.azx.mybuildassistant.santiyun.sdk;

import com.azx.mybuildassistant.utils.MyLog;
import com.azx.mybuildassistant.utils.MyTextUtils;

import java.io.File;

public abstract class BuildBaseTaskImpl extends BuildBaseTask {

    protected abstract String buildTargetAarName();

    protected abstract String getAarSavePath();

    @Override
    public int start() {
        targetAarFileName = buildTargetAarName();
        if (MyTextUtils.isEmpty(targetAarFileName)) {
            MyLog.e(this.getClass().getSimpleName(), "变量 targetAarFileName 构建失败！");
            System.exit(0);
        }
        desAarFile = checkDesFileDirAar();
        if (desAarFile == null) {
            MyLog.e(this.getClass().getSimpleName(), "变量 desAarFile 构建失败！");
            System.exit(0);
        }
        return 0;
    }

    String replaceDependencies(String line) {
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
        } else if (line.contains(wstech_ijk_java)) {
            return "//" + wstech_ijk_java;
        } else if (line.contains(wstech_ijk_exo)) {
            return "//" + wstech_ijk_exo;
        } else if (line.contains("api fileTree(")) {
            return "api fileTree(include: ['*.jar'], dir: 'libs')";
        } else if (line.contains(wstech_embed_enterconfapi) ||
                line.contains(wstech_embed_myaudio) ||
                line.contains(wstech_embed_myvideo) ||
                line.contains(wstech_embed_screen)) {
            return line.replaceAll("//", "");
        }
        return line;
    }

    private File checkDesFileDirAar() {
        String saveAarPath = getAarSavePath();
        if (MyTextUtils.isEmpty(saveAarPath)) {
            MyLog.e(this.getClass().getSimpleName(), "checkDesFileDirAar -> 从 getAarSavePath 方法中获取保存路径失败！");
            return null;
        }

        String des_file = saveAarPath + File.separator + targetAarFileName;
        File file = new File(des_file);
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                MyLog.e(this.getClass().getSimpleName(), "checkDesFileDirAar -> 删除老的文件失败！");
                return null;
            }
        }
        return file;
    }
}

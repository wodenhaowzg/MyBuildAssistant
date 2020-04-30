package com.azx.mybuildassistant.santiyun.sdk;

abstract class BuildBaseTaskImpl extends BuildBaseTask {

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
}

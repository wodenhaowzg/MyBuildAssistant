package com.azx.mybuildassistant.santiyun.sdk.helper;

import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

import static com.azx.mybuildassistant.santiyun.sdk.BuildBaseTask.screenMainifestFilePath;

public class VersionSelect {

    private static final String TAG = VersionSelect.class.getSimpleName();

    public static final int STAND_FULL_V8_SDK = 1;
    public static final int STAND_FULL_V7_SDK = 2;
    public static final int STAND_VOICE_V8_SDK = 3;
    public static final int STAND_VOICE_V7_SDK = 4;
    public static final int STAND_SDK_REMOVE_AUDIO_EFFECT_IJK = 5;
    public static final int STAND_SDK_REMOVE_AUDIO_EFFECT = 6;

    //    public static final int NORMAL_FULL_V7_SDK = 50;
    public static final int XIAOYUN_SDK = 100; // 淆云
    public static final int CUSTOM_TY = 202; // 太岳
//    public static final int CUSTOM_YQ = 200; // 椰趣
//    public static final int CUSTOM_TC = 201; // 同创
//    public static final int CUSTOM_LY = 203; // 龙羽
//    public static final int CUSTOM_HZ = 204; // 好赞

    public int version;

    public VersionBean selectVersion(int version) {
        this.version = version;
        VersionBean bean = new VersionBean();
        switch (version) {
            case STAND_FULL_V8_SDK:
                bean.v8Module = true;
                bean.videoModule = true;
                bean.rtmpModule = true;
                bean.ijkModule = true;
                bean.audioEffect = true;
                break;
            case STAND_FULL_V7_SDK:
                bean.videoModule = true;
                bean.rtmpModule = true;
                bean.ijkModule = true;
                bean.audioEffect = true;
                break;
            case STAND_VOICE_V8_SDK:
                bean.v8Module = true;
                bean.audioEffect = true;
                break;
            case STAND_VOICE_V7_SDK:
                bean.audioEffect = true;
                break;
            case STAND_SDK_REMOVE_AUDIO_EFFECT_IJK: // TY, HZ
            case CUSTOM_TY:
                bean.videoModule = true;
                bean.rtmpModule = true;
                break;
            case XIAOYUN_SDK:
                bean.videoModule = true;
                bean.rtmpModule = true;
                bean.ijkModule = true;
                break;
        }
        return bean;
    }

    public boolean changeBranchTag(String filePath, String branchTag) {
        boolean b6 = MyFileUtils.modifyFileContent(filePath, line -> {
            if (line.contains(branchTag)) {
                if (version == XIAOYUN_SDK || version == CUSTOM_TY) {
                    modifyScreenMainifest();
                }
            }
            return line;
        });

        if (!b6) {
            MyLog.error(TAG, "changeBranchTag -> 处理 globalConfigFilePath 文件代码失败!");
            return false;
        }
        return true;
    }

    private int count;
    private String str = "<uses-permission android:name=\"android.permission.FOREGROUND_SERVICE\" />\n" +
            "\t<application>\n" +
            "\t\t<service\n" +
            "\t\t\tandroid:name=\".ScreenCaptureService\"\n" +
            "\t\t\tandroid:foregroundServiceType=\"mediaProjection\" />\n" +
            "\t</application>";

    private void modifyScreenMainifest() {
        MyFileUtils.modifyFileContent(screenMainifestFilePath, line -> {
            if (line.contains("FOREGROUND_SERVICE")) {
                count = 5;
                return "";
            } else {
                if (count > 0) {
                    count--;
                    if (count == 0) {
                        return str;
                    } else {
                        return "";
                    }
                }
            }
            return line;
        });
    }


    public static class VersionBean {

        public boolean unityModule;
        public boolean voiceSdk;
        public boolean v8Module;
        public boolean videoModule;
        public boolean rtmpModule;
        public boolean ijkModule;
        public boolean audioEffect;
        public boolean faceModule;

        @Override
        public String toString() {
            return "VersionBean{" +
                    "unityModule=" + unityModule +
                    ", voiceSdk=" + voiceSdk +
                    ", v8Module=" + v8Module +
                    ", videoModule=" + videoModule +
                    ", rtmpModule=" + rtmpModule +
                    ", ijkModule=" + ijkModule +
                    ", audioEffect=" + audioEffect +
                    ", faceModule=" + faceModule +
                    '}';
        }
    }
}

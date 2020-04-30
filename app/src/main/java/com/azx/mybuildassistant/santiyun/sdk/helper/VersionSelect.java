package com.azx.mybuildassistant.santiyun.sdk.helper;

import com.azx.mybuildassistant.utils.MyFileUtils;
import com.azx.mybuildassistant.utils.MyLog;

public class VersionSelect {

    private static final String TAG = VersionSelect.class.getSimpleName();

    public static final int STAND_FULL_V8_SDK = 1;
    public static final int STAND_FULL_V7_SDK = 2;
    public static final int STAND_VOICE_V8_SDK = 3;
    public static final int STAND_VOICE_V7_SDK = 4;

    public static final int NORMAL_FULL_V7_SDK = 50;

    public static final int XIAOYUN_SDK = 100; // 淆云
    public static final int CUSTOM_YQ = 200; // 椰趣
    public static final int CUSTOM_TC = 201; // 同创
    public static final int CUSTOM_TY = 202; // 太岳
    public static final int CUSTOM_LY = 203; // 龙羽
    public static final int CUSTOM_HZ = 204; // 好赞

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
            case NORMAL_FULL_V7_SDK:
            case XIAOYUN_SDK:
            case CUSTOM_YQ:
            case CUSTOM_TC:
                bean.videoModule = true;
                bean.rtmpModule = true;
                bean.ijkModule = true;
                break;
            case CUSTOM_TY:
            case CUSTOM_HZ:
                bean.videoModule = true;
                bean.rtmpModule = true;
                break;
            case CUSTOM_LY:
                bean.voiceSdk = true;
                break;
        }
        return bean;
    }

    public boolean changeBranchTag(String filePath, String branchTag) {
        boolean b6 = MyFileUtils.modifyFileContent(filePath, new MyFileUtils.FileContentListener() {

            @Override
            public String lineTextContent(String line) {
                if (line.contains(branchTag)) {
                    if (version == CUSTOM_YQ) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_YQ;";
                    } else if (version == CUSTOM_TC) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_TC;";
                    } else if (version == CUSTOM_TY) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_TY;";
                    } else if (version == CUSTOM_LY) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_LY;";
                    } else if (version == CUSTOM_HZ) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_HZ;";
                    } else if (version == XIAOYUN_SDK) {
                        return "public static int mBranch = LocalSDKConstants.BRANCH_CLIENT_XIAOYUN;";
                    }
                }
                return line;
            }
        });

        if (!b6) {
            MyLog.error(TAG, "changeBranchTag -> 处理 globalConfigFilePath 文件代码失败!");
            return false;
        }
        return true;
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

package com.azx.mybuildassistant.santiyun;

/**
 * 打包 aar
 */
public class BuildStandSdkTask extends BaseTask {

    @Override
    public int start() {
        executeCmd("/Users/wangzhiguo/Desktop/sh_warehouse/MoMo_Package/dev分支打包.sh");
        return 0;
    }
}

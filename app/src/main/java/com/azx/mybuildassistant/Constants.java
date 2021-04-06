package com.azx.mybuildassistant;

import java.io.File;

public class Constants {

    public static final String MACHINE_PATH = File.separator +
            "Users" + File.separator +
            "zanewang"; // zanewang or wangzhiguo


    public static final String GITHUB_PATH = MACHINE_PATH + File.separator +
            "Downloads" + File.separator +
            "WorkSpace" + File.separator +
            "MyGithubs";

    public static final String GITHUB_ANDROID_PATH = MACHINE_PATH + File.separator +
            "Downloads" + File.separator +
            "WorkSpace" + File.separator +
            "MyGithubs" + File.separator +
            "Android";

    public static final String PROJECT_NAME = File.separator + "MyBuildAssistant";

    public static final String OWNER_PROJECT_PATH = GITHUB_ANDROID_PATH + PROJECT_NAME;

    /**
     * Gradle 命令所在文件夹的绝对路径
     */
    public static final String GRADLE = MACHINE_PATH + File.separator +
            ".gradle" + File.separator +
            "wrapper" + File.separator +
            "dists" + File.separator +
            "gradle-6.5-bin" + File.separator +
            "6nifqtx7604sqp1q6g8wikw7p" + File.separator +
            "gradle-6.5" + File.separator +
            "bin" + File.separator +
            "gradle";
}

package com.azx.mybuildassistant.santiyun;

import java.io.File;
import java.text.SimpleDateFormat;

public abstract class BaseTask {

    protected static final String MACHINE_PATH = File.separator + "Users" + File.separator + "zanewang";

    protected static final String GITHUB_PATH = MACHINE_PATH + File.separator + "WorkSpace" + File.separator + "MyGithub" ;

    protected static final String PROJECT_PATH = File.separator + "MyBuildAssistant";

    protected final String TEMP_SAVE = GITHUB_PATH + PROJECT_PATH + File.separator + "temp_save";

    static final String JAVA_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java";

//    protected static final String GRADLE = MACHINE_PATH + File.separator + ".gradle" + File.separator + "wrapper" + File.separator +
//            "dists" + File.separator + "gradle-5.4.1-all" + File.separator + "3221gyojl5jsh0helicew7rwx" + File.separator +
//            "gradle-5.4.1" + File.separator + "bin" + File.separator + "gradle";

//    protected static final String GRADLE = "/Users/wangzhiguo/.gradle/wrapper/dists/gradle-5.6.4-all/ankdp27end7byghfw1q2sw75f/gradle-5.6.4/bin/gradle";
    protected static final String GRADLE = MACHINE_PATH + "/.gradle/wrapper/dists/gradle-6.5-bin/6nifqtx7604sqp1q6g8wikw7p/gradle-6.5/bin/gradle";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    protected boolean DEBUG = true;

    public abstract int start();

    void log(String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(format + " " + msg);
    }
}

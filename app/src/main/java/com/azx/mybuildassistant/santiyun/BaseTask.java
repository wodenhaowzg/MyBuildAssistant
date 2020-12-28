package com.azx.mybuildassistant.santiyun;

import com.azx.mybuildassistant.Constants;

import java.io.File;
import java.text.SimpleDateFormat;

public abstract class BaseTask {

    protected static final String MACHINE_PATH = Constants.MACHINE_PATH;

    protected static final String GITHUB_PATH = Constants.GITHUB_PATH;

    protected static final String PROJECT_NAME = Constants.PROJECT_NAME;

    protected final String TEMP_SAVE = GITHUB_PATH + PROJECT_NAME + File.separator + "temp_save";

    static final String JAVA_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java";

    protected static final String GRADLE = Constants.GRADLE;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    protected boolean DEBUG = true;

    public abstract int start();

    void log(String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(format + " " + msg);
    }
}

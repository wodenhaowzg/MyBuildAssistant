package com.azx.mybuildassistant;

import com.azx.mybuildassistant.Constants;

import java.io.File;
import java.text.SimpleDateFormat;

public abstract class BaseTask implements Task{

    protected final String TEMP_SAVE = Constants.GITHUB_PATH + Constants.PROJECT_NAME + File.separator + "temp_save";

    protected static final String JAVA_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java";

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    @Override
    public int start() {
        return 0;
    }

    void log(String msg) {
        String format = mDateFormat.format(System.currentTimeMillis());
        System.out.println(format + " " + msg);
    }
}

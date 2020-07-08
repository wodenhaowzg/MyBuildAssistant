package com.azx.mybuildassistant.git;

import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckCommit implements CmdExecuteHelper.OnProcessOutputContentWithTag {

    private static final String GIT_NEWEST = "Your branch is up to date";
    private static final String TAG = CheckCommit.class.getSimpleName();
    private String[] mGitPaths = new String[]{
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyAlgorithm",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyAndroidFourComponents",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyAndroidTest",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyBuildAssistant",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyFirstGroovyProject",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyHttpTest",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyRxJava",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyUIProject",
    };
    private String mCurrentGitPath;
    private List<String> mNoNeedCommit = new ArrayList<>();

    public void start() {
        CmdExecuteHelper cmdExecuteHelper = new CmdExecuteHelper();
        cmdExecuteHelper.setOnProcessOutputContentWithTag(this);
        for (String path : mGitPaths) {
            mCurrentGitPath = path;
            CmdBean[] cmd = new CmdBean[]{
                    new CmdBean("cd " + path),
                    new CmdBean("git commit"),
            };
            cmdExecuteHelper.setTag(path);
                    cmdExecuteHelper.executeCmdAdv(cmd);
        }

        List<String> needCommit = new ArrayList<>();
        for (String gitPath : mGitPaths) {
            if (!mNoNeedCommit.contains(gitPath)) {
                needCommit.add(gitPath);
            }
        }

        for (String gitPath : mNoNeedCommit) {
            MyLog.e(TAG, "no need commit : " + gitPath);
        }

        for (String gitPath : needCommit) {
            MyLog.e(TAG, "need commit: " + gitPath);
        }
    }

    @Override
    public void outputNormalContent(Object tag, String content) {
        MyLog.e(TAG, "content : " + content + " | " + tag);
        if (content.contains(GIT_NEWEST)) {
            mNoNeedCommit.add(mCurrentGitPath);
        }
    }

    public static void main(String[] args) {
        CheckCommit checkCommit = new CheckCommit();
        checkCommit.start();
    }
}

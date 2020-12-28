package com.azx.mybuildassistant.git;

import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuter;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckCommit implements CmdExecuter.OnProcessOutputContentWithTag {

    private static final String TAG = CheckCommit.class.getSimpleName();
    private static final String GIT_NEWEST = "Your branch is up to date";
    private static final String[] PROJECT_PATH = new String[]{
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyAlgorithm",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyAndroidFourComponents",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyAndroidTest",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyBuildAssistant",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyFirstGroovyProject",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyHttpTest",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyRxJava",
            Constants.GITHUB_PATH + File.separator + "Android" + File.separator + "MyUIProject",
    };
    private String mCurrentGitPath;
    private List<String> mNoNeedCommit = new ArrayList<>();

    public void start() {
        CmdExecuter cmdExecuter = new CmdExecuter();
        cmdExecuter.setOnProcessOutputContentWithTag(this);
        for (String path : PROJECT_PATH) {
            mCurrentGitPath = path;
            CmdBean[] cmd = new CmdBean[]{
                    new CmdBean("cd " + path),
                    new CmdBean("git commit"),
            };
            cmdExecuter.setTag(path);
                    cmdExecuter.executeCmdAdv(cmd);
        }

        List<String> needCommit = new ArrayList<>();
        for (String gitPath : PROJECT_PATH) {
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

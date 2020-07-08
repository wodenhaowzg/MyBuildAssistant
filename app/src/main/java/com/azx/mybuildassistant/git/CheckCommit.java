package com.azx.mybuildassistant.git;

import com.azx.mybuildassistant.Constants;
import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckCommit implements CmdExecuteHelper.OnProcessOutputContent {

    private static final String GIT_NEWEST = "Your branch is up to date";
    private static final String TAG = CheckCommit.class.getSimpleName();
    private String[] mGitPaths = new String[]{
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyHttpTest",
            Constants.MYGITHUB_FILE_DIR_PATH + File.separator + "Android" + File.separator + "MyUIProject",
    };
    private String mCurrentGitPath;
    private List<String> mNeedCommit = new ArrayList<>();

    public void start() {
        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        mCmdExecuteHelper.setOnProcessOutputContent(this);
        for (String path : mGitPaths) {
            mCurrentGitPath = path;
            CmdBean[] cmd = new CmdBean[]{
                    new CmdBean("cd " + path),
                    new CmdBean("git commit"),
            };
            mCmdExecuteHelper.executeCmdAdv(cmd);
        }

        for (String gitPath : mNeedCommit) {
            MyLog.e(TAG, "gitPath : " + gitPath);
        }
    }

    @Override
    public void outputNormalContent(String content) {
        MyLog.e(TAG, "content : " + content);
        if (!content.contains(GIT_NEWEST)) {
            mNeedCommit.add(mCurrentGitPath);
        }
    }

    public static void main(String[] args) {
        CheckCommit checkCommit = new CheckCommit();
        checkCommit.start();
    }
}

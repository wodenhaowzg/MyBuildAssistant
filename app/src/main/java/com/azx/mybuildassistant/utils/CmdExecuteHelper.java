package com.azx.mybuildassistant.utils;

import com.azx.mybuildassistant.bean.CmdBean;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CmdExecuteHelper implements ProcExecHandler.ProcessOutputListener {

    private static final String TAG = CmdExecuteHelper.class.getSimpleName();
    private final Object lock = new Object();
    private CmdBean current_cmd;
    private String last_cmd;

    public int executeCmdAdv(CmdBean[] cmds) {
        Process process;
        try {
            process = Runtime.getRuntime().exec("/bin/bash", null);
            if (process == null) {
                return -1;
            }

            new Thread(() -> {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
                synchronized (lock) {
                    for (int i = 0; i < cmds.length; i++) {
                        CmdBean cmd = cmds[i];
                        if (last_cmd != null && last_cmd.contains("gradle")) {
                            MyLog.log(TAG, "Sleep and execute next gradle cmd");
                            try {
                                Thread.sleep(1000);  // FIXME 执行多个gradle命令时需要暂停一下，否则执行下一个命令有可能卡住不动，具体原因未知。
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        MyLog.log(TAG, "Start execute cmd : " + cmd.cmd + " | " + cmd.stopFlag);
                        current_cmd = cmd;
                        out.println(cmd.cmd);
                        if (cmd.stopFlag != null) {
                            out.println("echo " + cmd.stopFlag);
                        }
                        last_cmd = cmd.cmd;
                        if (i != cmds.length - 1) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                MyLog.log(TAG, "Process destory : " + process);
                try {
                    Thread.sleep(1000);  // FIXME 执行多个gradle命令时需要暂停一下，否则执行下一个命令有可能卡住不动，具体原因未知。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                process.destroy();
            }).start();

            ProcExecHandler mProcExecHandler = new ProcExecHandler(process);
            mProcExecHandler.setmProcessOutputListener(this);
            mProcExecHandler.start();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    @Override
    public void processOutput(int execute, Process process, String content) {
        MyLog.log("processOutput : " + content);
        synchronized (lock) {
            if (content.equals(current_cmd.stopFlag) || content.contains("BUILD SUCCESSFUL")) {
                current_cmd = null;
                MyLog.log("NotifyAll : " + content);
                lock.notifyAll();
            }
        }
    }
}

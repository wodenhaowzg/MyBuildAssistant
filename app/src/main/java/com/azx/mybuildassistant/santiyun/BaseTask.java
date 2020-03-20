package com.azx.mybuildassistant.santiyun;

import com.azx.mybuildassistant.utils.ProcExecHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public abstract class BaseTask {

    protected static final String MACHINE_PATH = File.separator + "Users" + File.separator + "wangzhiguo";

    protected static final String JAVA_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java";

    protected static final String GRADLE = MACHINE_PATH + File.separator + ".gradle" + File.separator + "wrapper" + File.separator +
            "dists" + File.separator + "gradle-5.4.1-all" + File.separator + "3221gyojl5jsh0helicew7rwx" + File.separator +
            "gradle-5.4.1" + File.separator + "bin" + File.separator + "gradle";

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");

    protected boolean DEBUG = true;

    public abstract int start();

    protected int executeCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
//            receiveProcessInput(process);
        } catch (Exception e) {
            System.out.println("执行命令异常[" + cmd + "]: " + e.getLocalizedMessage());
        }
        return 0;
    }

    int executeCmd(String[] cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
//            receiveProcessInput(process);
        } catch (Exception e) {
            System.out.println("执行命令异常[" + cmd + "]: " + e.getLocalizedMessage());
        }
        return 0;
    }

    void executeCmdAdv(String[] cmds, ProcExecHandler.ProcessOutputListener listener) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("/bin/bash", null);
            if (process == null) {
                if (listener != null) {
                    listener.processOutput(-1, null, "");
                }
                return;
            }

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
            for (int i = 0; i < cmds.length; i++) {
                String cmd = cmds[i];
                out.println(cmd);
                log(cmd);
            }
            ProcExecHandler mProcExecHandler = new ProcExecHandler(process);
            mProcExecHandler.setmProcessOutputListener(listener);
            mProcExecHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.processOutput(-2, process, "");
            }
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }).start();
    }

    protected void log(String msg) {
        String format = dateFormat.format(System.currentTimeMillis());
        System.out.println(format + " " + msg);
    }
}

package com.azx.mybuildassistant.utils;

import com.azx.mybuildassistant.bean.CmdBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CmdExecuteHelper {

    public static final String CMD_STOP_FLAG = "FINISH";
    private static final String TAG = CmdExecuteHelper.class.getSimpleName();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public int executeCmdAdv(CmdBean[] cmds) {
        String[] realCmds = new String[cmds.length];
        int index = 0;
        for (CmdBean cmd : cmds) {
            realCmds[index] = cmd.cmd;
            index++;
        }

        String sh;
        try {
            File file = new File("/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap/TTTRtcEngine_AndroidKit/temp/execute.sh");
            if (file.exists()) {
                file.delete();
            }
            BufferedWriter buw = new BufferedWriter(new FileWriter(file));
            for (String realCmd : realCmds) {
                buw.write(realCmd);
                buw.newLine();
                buw.flush();
            }
            buw.close();

            //解决脚本没有执行权限
            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", file.getAbsolutePath());
            Process process = builder.start();
            MyLog.d(TAG, "创建获取shell文件权限执行命令进程。" + process);
            executeProcess(process);
            sh = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        try {
            Process process = Runtime.getRuntime().exec(sh, null);
            MyLog.d(TAG, "创建shell文件执行命令进程。" + process);
            executeProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
        return 0;
    }

    private void executeProcess(Process process) {
        InputStream inputStream = process.getInputStream();
        InputStreamReaderRunnable inputRunnable = new InputStreamReaderRunnable(inputStream, "正常");
        executorService.execute(inputRunnable);
        InputStream errorStream = process.getErrorStream();
        InputStreamReaderRunnable errorRunnable = new InputStreamReaderRunnable(errorStream, "错误");
        executorService.execute(errorRunnable);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        process.destroy();
        inputRunnable.stop = true;
        errorRunnable.stop = true;
    }

    static class InputStreamReaderRunnable implements Runnable {
        private InputStream ins;
        private boolean stop;
        private String tag;

        InputStreamReaderRunnable(InputStream ins, String tag) {
            this.ins = ins;
            this.tag = tag;
        }

        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            try {
                MyLog.d(TAG, tag + "输出流线程启动! " + Thread.currentThread().getName());
                while (!stop) {
                    String line = reader.readLine();
                    if (line != null && line.length() > 0) {
                        processOutputString(line);
                    } else {
                        try {
                            MyLog.d(TAG, tag + "输出流程等待数据中..." + Thread.currentThread().getName());
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            MyLog.d(TAG, tag + "输出流线程结束! " + Thread.currentThread().getName());
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                MyLog.d(TAG, tag + "输出流线程结束! " + Thread.currentThread().getName());
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void processOutputString(String content) {
            MyLog.d(TAG, tag + "输出 : " + content);
        }
    }

//    static class CmdExecuteRunnable implements Runnable {
//
//        Process process;
//        private CmdBean[] cmds;
//        private String currentStopFlag;
//        private volatile boolean isWaitting;
//
//        CmdExecuteRunnable(Process process, CmdBean[] cmds) {
//            this.cmds = cmds;
//            this.process = process;
//        }
//
//        @Override
//        public void run() {
//            MyLog.d(TAG, "命令执行线程启动 : " + Thread.currentThread().getName());
//            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
//            for (CmdBean cmd : cmds) {
//                MyLog.d(TAG, "开始执行命令: " + cmd.cmd + " | " + cmd.stopFlag);
//                if (cmd.stopFlag == null) {
//                    cmd.stopFlag = CMD_STOP_FLAG;
//                }
//                currentStopFlag = cmd.stopFlag;
//                out.println(cmd.cmd);
////                out.println("echo " + currentStopFlag);
//                isWaitting = true;
//                while (isWaitting) {
//                    try {
////                        MyLog.d(TAG, "命令执行线程等待中...");
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            MyLog.d(TAG, "命令执行完毕，销毁进程。" + process);
//            out.close();
//            process.destroy();
//            process = null;
//        }
//    }
}

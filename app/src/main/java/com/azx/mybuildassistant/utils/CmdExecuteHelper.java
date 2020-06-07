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

    private static final String TEMP_FILE = "/Users/wangzhiguo/Desktop/Temporary-Files/SDK_Kit/TTTRtcEngine_AndroidKitWrap" +
            "/TTTRtcEngine_AndroidKit/temp/execute.sh";
    private static final String TAG = CmdExecuteHelper.class.getSimpleName();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private OnProcessOutputContent onProcessOutputContent;

    public void setOnProcessOutputContent(OnProcessOutputContent onProcessOutputContent) {
        this.onProcessOutputContent = onProcessOutputContent;
    }

    public int executeCmdAdv(CmdBean[] cmds) {
        String[] realCmds = new String[cmds.length];
        int index = 0;
        for (CmdBean cmd : cmds) {
            realCmds[index] = cmd.cmd;
            index++;
        }

        String sh;
        try {
            File file = new File(TEMP_FILE);
            if (file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    return -1;
                }
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
            return -2;
        }

        try {
            Process process = Runtime.getRuntime().exec(sh, null);
            MyLog.d(TAG, "创建shell文件执行命令进程。" + process);
            executeProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
        return 0;
    }

    private void executeProcess(Process process) {
        InputStream inputStream = process.getInputStream();
        InputStreamReaderRunnable inputRunnable = new InputStreamReaderRunnable(inputStream, "正常");
        if (onProcessOutputContent != null) {
            inputRunnable.setOnProcessOutputContent(onProcessOutputContent);
        }
        executorService.execute(inputRunnable);
        InputStream errorStream = process.getErrorStream();
        InputStreamReaderRunnable errorRunnable = new InputStreamReaderRunnable(errorStream, "错误");
        executorService.execute(errorRunnable);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyLog.d(TAG, "进程执行完毕! " + process);
        inputRunnable.stop = true;
        errorRunnable.stop = true;

        while (!inputRunnable.finished){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!errorRunnable.finished){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        process.destroy();
    }

    static class InputStreamReaderRunnable implements Runnable {
        private InputStream ins;
        private volatile boolean stop, finished;
        private String tag;
        private OnProcessOutputContent onProcessOutputContent;

        InputStreamReaderRunnable(InputStream ins, String tag) {
            this.ins = ins;
            this.tag = tag;
        }

        void setOnProcessOutputContent(OnProcessOutputContent onProcessOutputContent) {
            this.onProcessOutputContent = onProcessOutputContent;
        }

        @Override
        public void run() {
            InputStreamReader inputStreamReader = new InputStreamReader(ins);
            BufferedReader reader = new BufferedReader(inputStreamReader);
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

                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (ins != null) {
                    try {
                        ins.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ins = null;
                }
                finished = true;
            }
        }

        void processOutputString(String content) {
            MyLog.d(TAG, tag + "输出 : " + content);
            if (tag.equals("正常") && onProcessOutputContent != null) {
                onProcessOutputContent.outputNormalContent(content);
            }
        }
    }

    public interface OnProcessOutputContent{

        void outputNormalContent(String content);
    }
}

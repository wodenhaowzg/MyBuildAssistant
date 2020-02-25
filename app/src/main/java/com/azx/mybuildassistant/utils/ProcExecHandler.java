package com.azx.mybuildassistant.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcExecHandler {

    private static final String TAG = ProcExecHandler.class.getSimpleName();
    private Thread inputThread, errorThread;
    private Process process;

    public void setmProcessOutputListener(ProcessOutputListener mProcessOutputListener) {
        this.mProcessOutputListener = mProcessOutputListener;
    }

    private ProcessOutputListener mProcessOutputListener;

    public ProcExecHandler(Process process) {
        this.process = process;
        InputStream inputStream = process.getInputStream();
        InputStreamReaderRunnable inputRunnable = new InputStreamReaderRunnable(inputStream);
        inputThread = new Thread(inputRunnable);
        InputStream errorStream = process.getErrorStream();
        InputStreamReaderRunnable errorRunnable = new InputStreamReaderRunnable(errorStream);
        errorThread = new Thread(errorRunnable);
    }

    public void start() {
        MyLog.log(TAG, "start invoed!");
        inputThread.start();
        errorThread.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class InputStreamReaderRunnable implements Runnable {
        private BufferedReader reader;

        InputStreamReaderRunnable(InputStream ins) {
            this.reader = new BufferedReader(new InputStreamReader(ins));
        }

        @Override
        public void run() {
            try {
                MyLog.log(TAG, "Thread start running! " + Thread.currentThread().getName());
                String line;
                while ((line = reader.readLine()) != null) {
                    if (mProcessOutputListener != null) {
                        mProcessOutputListener.processOutput(0, process, line);
                    }
                }
                MyLog.log(TAG, "Thread start over! " + Thread.currentThread().getName());
            } catch (Exception e) {
                MyLog.log("Exception : " + e.getLocalizedMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    MyLog.log("Exception : " + e.getLocalizedMessage());
                }
            }
        }
    }

    public interface ProcessOutputListener {

        void processOutput(int execute, Process process, String content);
    }
}

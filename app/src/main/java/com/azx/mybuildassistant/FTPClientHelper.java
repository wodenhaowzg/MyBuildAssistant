/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azx.mybuildassistant;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example program demonstrating how to use the FTPClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * See below for further options.
 */
public final class FTPClientHelper {

    public static void main(String[] args) {
//        String resultMsg = FTPClientHelper.getInstance().connectFtpServer("39.107.116.40", 21);
//        System.out.println(resultMsg);
//        String loginResult = FTPClientHelper.getInstance().loginFtpServer("admin_android", "android");
//        System.out.println(loginResult);
//        FTPClientHelper.getInstance().disconnectFtpServer();
//        String resultMsg2 = FTPClientHelper.getInstance().connectFtpServer("39.107.116.40", 21);
//        System.out.println(resultMsg2);
//        String loginResult2 = FTPClientHelper.getInstance().loginFtpServer("admin_android", "android");
//        System.out.println(loginResult2);

        FTPClientHelper ftpClientHelper = FTPClientHelper.getInstance();
        ftpClientHelper.connectAndLoginServer("39.107.116.40", 21, "admin_android", "android");
        List<String> fileList = ftpClientHelper.getFileList("testApkUpdate");
        if (fileList == null || fileList.size() <= 0) {
//            loge("Get file failed");
            System.err.println("Get file failed");
            return;
        }

        String name = fileList.get(0);
        boolean downloadFile = ftpClientHelper.downloadFile("testApkUpdate/" + name, "/Users/wangzhiguo/Desktop/test55555.apk");
        if (!downloadFile) {
            System.err.println("Download file failed");
        }
    }

    private static final String TAG = "FTPClientHelper";

    private static final String FTP_CONNECT_SUCCESS = "CONNECT_SUCCESS";
    private static final String FTP_LOGIN_SUCCESS = "LOGIN_SUCCESS";

    private volatile static FTPClientHelper holder;
    private FTPClient mFTPClient = new FTPClient();

    public static FTPClientHelper getInstance() {
        if (holder == null) {
            synchronized (FTPClientHelper.class) {
                if (holder == null) {
                    holder = new FTPClientHelper();
                }
            }
        }
        return holder;
    }

    private boolean mLogined;

    public synchronized boolean connectAndLoginServer(String server, int port, String userName, String password) {
        String connectResult = connectFtpServer(server, port);
        if (!connectResult.equals(FTP_CONNECT_SUCCESS)) {
            return false;
        }

        String loginResult = loginFtpServer(userName, password);
        if (!loginResult.equals(FTP_LOGIN_SUCCESS)) {
            return false;
        }
        mLogined = true;
        return true;
    }

    public List<String> getFileList(String dirPath) {
        if (!mLogined) {
            return null;
        }

        List<String> files;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = mFTPClient.listFiles(dirPath);
            if (ftpFiles == null || ftpFiles.length <= 0) {
                return null;
            }

            files = new ArrayList<>();
            for (FTPFile f : mFTPClient.listFiles(dirPath)) {
                files.add(f.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return files;
    }

    public boolean downloadFile(String srcFilePath, String desFilePath) {
        FileOutputStream fos = null;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setCopyStreamListener(createListener());
            mFTPClient.enterLocalPassiveMode();
            fos = new FileOutputStream(desFilePath);
            mFTPClient.retrieveFile(srcFilePath, fos);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private String connectFtpServer(String server, int port) {
        mFTPClient.setControlEncoding("UTF-8");
        try {
            int reply;
            if (port > 0) {
                mFTPClient.connect(server, port);
            } else {
                mFTPClient.connect(server);
            }
            log("Connected to " + server + " on " + (port > 0 ? port : mFTPClient.getDefaultPort()));

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = mFTPClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                mFTPClient.disconnect();
                return "FTP server refused connection.";
            }
        } catch (IOException e) {
            if (mFTPClient.isConnected()) {
                try {
                    mFTPClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            return "Could not connect to server.";
        }
        return FTP_CONNECT_SUCCESS;
    }

    private void disconnectFtpServer() {
        if (mFTPClient.isConnected()) {
            try {
                mFTPClient.logout();
            } catch (IOException e) {
                loge("Logout ftp server failed, exception : " + e.getLocalizedMessage());
            }

            try {
                mFTPClient.disconnect();
            } catch (IOException f) {
                // do nothing
                loge("Disconnect ftp server failed, exception : " + f.getLocalizedMessage());
            }
        }
    }

    private String loginFtpServer(String userName, String password) {
        if (!mFTPClient.isConnected()) {
            return "No connect!";
        }

        // 显示登陆中的调试信息
        mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), false));
        try {
            if (!mFTPClient.login(userName, password)) {
                mFTPClient.logout();
                return "Login failed! ";
            }
        } catch (IOException e) {
            return "Login exception : " + e.getLocalizedMessage();
        }
        return FTP_LOGIN_SUCCESS;
    }

    private CopyStreamListener createListener() {
        return new CopyStreamListener() {
            private long megsTotal = 0;

            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }

            @Override
            public void bytesTransferred(long totalBytesTransferred,
                                         int bytesTransferred, long streamSize) {
                System.out.println(totalBytesTransferred + " | " + bytesTransferred + " | " + streamSize);
            }
        };
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    private void loge(String msg) {
        System.err.println(msg);
    }
}


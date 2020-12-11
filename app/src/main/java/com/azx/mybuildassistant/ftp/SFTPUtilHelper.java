package com.azx.mybuildassistant.ftp;


import com.azx.mybuildassistant.bean.FTPFileBean;
import com.azx.mybuildassistant.utils.MyTextUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SFTPUtilHelper extends BaseFtpHelper {

    private Session mSession;
    private ChannelSftp mSFTPChannel;

    /**
     * 连接sftp服务器
     */
    @Override
    public int login(String serverAddress, int port, String userName, String password) {
        if (MyTextUtils.isEmpty(serverAddress) || MyTextUtils.isEmpty(userName) || port == 0) {
            return -1;
        }
        Session session;
        Channel channel;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(userName, serverAddress, port);
        } catch (JSchException e) {
            e.printStackTrace();
            return -2;
        }

        try {
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no"); // ?
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return -3;
        }

        try {
            channel = session.openChannel("sftp");
        } catch (JSchException e) {
            e.printStackTrace();
            return -4;
        }

        try {
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return -5;
        }

        mSFTPChannel = (ChannelSftp) channel;
        mSession = session;
        return 0;
    }

    /**
     * 关闭连接 server
     */
    @Override
    public int logout() {
        if (mSFTPChannel != null) {
            if (mSFTPChannel.isConnected()) {
                mSFTPChannel.disconnect();
            }
        }

        if (mSession != null) {
            if (mSession.isConnected()) {
                mSession.disconnect();
            }
        }
        return 0;
    }

    /**
     * 删除server上的文件夹，但如果文件夹中还包含文件夹，会出错！
     *
     * @param dirPath 文件夹路径，里面不能包含子文件夹
     * @throws SftpException 删除失败的异常
     */
    public void deleteDir(String dirPath) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = mSFTPChannel.ls(dirPath);
        for (ChannelSftp.LsEntry file : files) {
            String filename = file.getFilename();
            if (filename.equals(".") || filename.equals("..")) {
                continue;
            }
            mSFTPChannel.rm(dirPath + "/" + filename);
        }
        mSFTPChannel.rmdir(dirPath);
    }

    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param basePath     服务器的基础路径
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     */
    public void upload(String basePath, String directory, String sftpFileName, String input, SftpProgressMonitor monitor) throws SftpException {
        try {
            mSFTPChannel.cd(basePath);
            mSFTPChannel.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = basePath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) continue;
                tempPath += "/" + dir;
                try {
                    mSFTPChannel.cd(tempPath);
                } catch (SftpException ex) {
                    mSFTPChannel.mkdir(tempPath);
                    mSFTPChannel.cd(tempPath);
                }
            }
        }
        mSFTPChannel.put(input, sftpFileName, monitor);  //上传文件
    }

    /**
     * 下载文件。
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public int download(String directory, String downloadFile, String saveFile) {
        if (MyTextUtils.isEmpty(directory) || MyTextUtils.isEmpty(downloadFile) || MyTextUtils.isEmpty(saveFile)) {
            return -1;
        }
        File file = new File(saveFile);
        try {
            mSFTPChannel.cd(directory);
        } catch (SftpException e) {
            e.printStackTrace();
            return -2;
        }

        try {
            mSFTPChannel.get(downloadFile, new FileOutputStream(file));
            return 0;
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return -3;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
            mSFTPChannel.cd(directory);
            mSFTPChannel.rm(deleteFile);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    @Override
    public List<FTPFileBean> listFiles(String directory) {
        List<FTPFileBean> beans = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> entries = mSFTPChannel.ls(directory);
            for (ChannelSftp.LsEntry entry : entries) {
                String filename = entry.getFilename();
                FTPFileBean ftpFileBean = new FTPFileBean();
                ftpFileBean.mFileName = filename;
                beans.add(ftpFileBean);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public void test() {
        new Thread(() -> {
            SFTPUtilHelper sftp = new SFTPUtilHelper();
            int login = sftp.login("admin_android", 52300, "101.133.236.102", "Qe0Vbx81mkaS");
            if (login < 0) {
                System.out.println(" 登陆sftp失败");
                return;
            }

            int download = sftp.download("/admin_android/android/version_update", "app-debug.apk", "/mnt/sdcard/app-debug222.apk");
            if (download <= 0) {
                System.out.println(" 下载文件失败");
                return;
            }
            sftp.logout();
        }).start();
    }
}

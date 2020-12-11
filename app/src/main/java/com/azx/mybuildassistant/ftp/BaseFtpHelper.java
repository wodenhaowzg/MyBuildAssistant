package com.azx.mybuildassistant.ftp;


import com.azx.mybuildassistant.bean.FTPFileBean;

import java.util.List;

public abstract class BaseFtpHelper {


    public abstract int login(String serverAddress, int port, String userName, String password);

    public abstract int logout();

    public abstract List<FTPFileBean> listFiles(String directory);
}

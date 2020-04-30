package com.azx.mybuildassistant.bean;

public class CmdBean {

    public String cmd;
    public String stopFlag;

    public CmdBean(String cmd, String stopFlag) {
        this.cmd = cmd;
        this.stopFlag = stopFlag;
    }

    public CmdBean(String cmd) {
        this.cmd = cmd;
    }
}

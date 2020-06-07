package com.azx.mybuildassistant;

import com.azx.mybuildassistant.bean.CmdBean;
import com.azx.mybuildassistant.utils.CmdExecuteHelper;
import com.azx.mybuildassistant.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class AdbConnecter implements CmdExecuteHelper.OnProcessOutputContent {

    private final String TAG = "AdbConnecter";
    private static final int CMD_TYPE_DEVICES = 1;
    private volatile int curCmdType;

    private volatile List<String> connectedDevices = new ArrayList<>();

    void startConnect() {
        CmdExecuteHelper mCmdExecuteHelper = new CmdExecuteHelper();
        mCmdExecuteHelper.setOnProcessOutputContent(this);

        curCmdType = CMD_TYPE_DEVICES;
        findDevices(mCmdExecuteHelper);

        for (String connectedDevice : connectedDevices) {
            connectDevice(mCmdExecuteHelper, connectedDevice);
        }
        System.exit(0);
    }

    private void connectDevice(CmdExecuteHelper mCmdExecuteHelper, String connectedDevice) {
        String cmdStr = "adb -s " + connectedDevice + " shell ifconfig wlan0";
        MyLog.d(TAG, cmdStr);
        CmdBean[] cmd = new CmdBean[]{new CmdBean(cmdStr)};
        int i = mCmdExecuteHelper.executeCmdAdv(cmd);
        if (i != 0) {
            MyLog.e(TAG, "execute failed!");
        }
    }


    private void findDevices(CmdExecuteHelper mCmdExecuteHelper) {
        CmdBean[] cmd = new CmdBean[]{new CmdBean("adb devices")};
        int i = mCmdExecuteHelper.executeCmdAdv(cmd);
        if (i != 0) {
            MyLog.e(TAG, "execute failed!");
        }

        if (connectedDevices.size() == 0) {
            MyLog.e(TAG, "未找到任何设备");
            return;
        }

        for (String connectedDevice : connectedDevices) {
            MyLog.d(TAG, "找到设备: " + connectedDevice);
        }
    }

    @Override
    public void outputNormalContent(String content) {
        switch (curCmdType) {
            case CMD_TYPE_DEVICES:
                String devicesListstartLine = "List of devices attached";
                if (content.contains("device") && !content.equals(devicesListstartLine)) {
                    String connectedDevice = content.replace("device", "").trim();
                    connectedDevices.add(connectedDevice);
                }
                break;
        }
    }
}

package com.azx.mybuildassistant.santiyun;

public class CompileClientKit extends BaseTask{

    @Override
    public int start() {
        System.out.println("---获取Lib库当前commit标识---");
        String[] cmds = new String[]{"cd /Users/wangzhiguo/Downloads/Learns/Guo_Company_Svn/GitLab/3TClient/Lib",
            "git log -1"};
//        executeCmdAdv(cmds, new ProcExecHandler.ProcessOutputListener() {
//            @Override
//            public void processOutput(String content) {
//                System.out.println("---开始获取输出内容---");
//                System.out.println(content);
//                if (content.contains("commit")) {
//                    String commitId = content.substring("commit".length());
//                }
//            }
//        });
        return 0;
    }
}

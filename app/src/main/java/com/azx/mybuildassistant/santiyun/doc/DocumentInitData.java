package com.azx.mybuildassistant.santiyun.doc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DocumentInitData {

    List<FunCodeBlock> initInterDatas(String filePath) {
        List<FunCodeBlock> datas = init(filePath);
        if (datas == null) {
            return null;
        }

        for (FunCodeBlock data : datas) {
            // 获取接口第一行注释。
            String[] singleLineComment = data.funComment.split("<p/>");
            String srcStr = singleLineComment[0];
            data.funSimpleComment = srcStr.replaceAll("/*\\*", "").replaceAll("\\*", "").trim();
            // 获取接口简单的名词，前缀以及参数列表
            obtainFunSimpleName(data, data.funFullName);
        }
        return datas;
    }

    List<FunCodeBlock> initCallBackInterDatas(String filePath) {
        List<FunCodeBlock> datas = init(filePath);
        if (datas == null) {
            return null;
        }

        datas.remove(0);
        for (FunCodeBlock data : datas) {
            // 获取接口第一行注释。
            String[] singleLineComment = data.funComment.split("<p/>");
            String srcStr = singleLineComment[0];
            data.funSimpleComment = srcStr.replaceAll("/*\\*", "").replaceAll("\\*", "").trim();
            // 获取接口简单的名词，前缀以及参数列表
            String funFullNameSrcStr = data.funFullName.replaceAll(";", "");
            String[] funFullNameSrcStrArr = funFullNameSrcStr.replaceAll(";", "").split(" ");
            data.funPrefixName = funFullNameSrcStrArr[0];
            String funNameWithParams = funFullNameSrcStrArr[1].trim();
            int index = funNameWithParams.indexOf("(");
            if (index != -1) {
                data.funName = funNameWithParams.substring(0, index);
                data.funParams = funFullNameSrcStr.substring(index);
            }
        }
        return datas;
    }

    List<FunCodeBlock> initConstantDatas(String filePath) {
        List<FunCodeBlock> datas = init(filePath);
        if (datas == null) {
            return null;
        }

        datas.remove(0);
        for (FunCodeBlock data : datas) {
            // 获取接口第一行注释。
            data.funSimpleComment = data.funComment.replaceAll("/*\\*", "").replaceAll("\\*", "").replaceAll("/", "").trim();
            // 获取接口简单的名词，前缀以及参数列表
            String funFullNameSrcStr = data.funFullName.replaceAll(";", "");
            int index = funFullNameSrcStr.indexOf("int");
            data.funPrefixName = funFullNameSrcStr.substring(0, index + 3);
            data.funName = funFullNameSrcStr.substring(index + 3).trim();
        }
        return datas;
    }

    private List<FunCodeBlock> init(String filePath) {
        List<FunCodeBlock> mFunCommentCodeBlocks = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean isEnd = false;
            StringBuilder stringBuilder = null;
            FunType funType = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("------ 过时 API")) {
                    break;
                }

                FunType temp = checkFunType(line);
                if (temp != null) {
                    funType = checkFunType(line);
                }
                if (line.contains("/**")) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(line).append("\n");
                } else if (line.contains("*/")) {
                    if (stringBuilder != null) {
                        isEnd = true;
                        stringBuilder.append(line).append("\n");
                    }
                } else if (isEnd) {
                    if (line.contains("@Deprecated")) {
                        continue;
                    }

                    String funComment = stringBuilder.toString();
                    if (line.contains("volatile")) {
                        isEnd = false;
                        stringBuilder.delete(0, funComment.length());
                        stringBuilder = null;
                        continue;
                    }

                    if (line.contains("class")) {
                        isEnd = false;
                        stringBuilder.delete(0, funComment.length());
                        stringBuilder = null;
                        continue;
                    }
                    String funFullName = line.replaceAll(" \\{", "").trim();
                    FunCodeBlock mFunCommentCodeBlock = new FunCodeBlock(funType, funComment, funFullName);
                    mFunCommentCodeBlocks.add(mFunCommentCodeBlock);
                    stringBuilder.delete(0, stringBuilder.toString().length());
                    stringBuilder = null;
                    isEnd = false;
                } else {
                    if (stringBuilder != null) {
                        stringBuilder.append(line).append("\n");
                    }
                }

            }
            return mFunCommentCodeBlocks;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private FunType checkFunType(String line) {
        if (line.contains("------ 核心方法")) {
            return FunType.CORE;
        } else if (line.contains("------ 核心音频方法")) {
            return FunType.AUDIO_CORE;
        } else if (line.contains("------ 核心视频方法")) {
            return FunType.VIDEO_CORE;
        } else if (line.contains("------ 音频路由")) {
            return FunType.AUDIO_ROUTER;
        } else if (line.contains("------ 音频录音")) {
            return FunType.AUDIO_RECORD;
        } else if (line.contains("------ 耳返控制")) {
            return FunType.EARBACK;
        } else if (line.contains("------ 音乐文件播放及混音")) {
            return FunType.AUDIO_MUSIC;
        } else if (line.contains("------ 音效文件播放管理")) {
            return FunType.AUDIO_EFFECT;
        } else if (line.contains("------ 网络检测")) {
            return FunType.INTENT_CHECK;
        } else if (line.contains("------ 原始音频数据")) {
            return FunType.ORIGINAL_AUDIO;
        } else if (line.contains("------ 音频自采集")) {
            return FunType.AUDIO_OUT_CAPTURE;
        } else if (line.contains("------ 视频自采集（仅适用于 Push 模式）")) {
            return FunType.VIDEO_OUT_CAPTURE;
        } else if (line.contains("------ 视频双流模式")) {
            return FunType.VIDEO_DUAL;
        } else if (line.contains("------ 摄像头控制")) {
            return FunType.CAMERA_CONTROL;
        } else if (line.contains("------ 屏幕共享")) {
            return FunType.SCREEN_SHARE;
        } else if (line.contains("------ CDN推流设置")) {
            return FunType.CDN_SETTING;
        } else if (line.contains("------ CDN推流")) {
            return FunType.CDN_PUSH;
        } else if (line.contains("------ CDN拉流")) {
            return FunType.CDN_PULL;
        } else if (line.contains("------ 其他方法")) {
            return FunType.OTHER;
        }

        if (line.contains("------ 核心回调")) {
            return FunType.CALLBACK_CORE;
        } else if (line.contains("------ 核心音频事件回调")) {
            return FunType.CALLBACK_AUDIO_CORE;
        } else if (line.contains("------ 核心视频事件回调")) {
            return FunType.CALLBACK_VIDEO_CORE;
        } else if (line.contains("------ 数据统计事件回调")) {
            return FunType.CALLBACK_STATISTICS;
        } else if (line.contains("------ 音频音效播放事件回调")) {
            return FunType.CALLBACK_AUDIO_MUSIC;
        } else if (line.contains("------ 其他回调")) {
            return FunType.CALLBACK_OTHER;
        }

        if (line.contains("------ 加入频道错误码")) {
            return FunType.CONSTANT_JOIN_CHANNEL;
        } else if (line.contains("------ 强制离开频道错误码")) {
            return FunType.CONSTANT_FOCUS_LEAVE;
        } else if (line.contains("------ RTMP推流错误码")) {
            return FunType.CONSTANT_RTMP;
        } else if (line.contains("------ 频道模式")) {
            return FunType.CONSTANT_CHANNEL_MODE;
        } else if (line.contains("------ 用户角色")) {
            return FunType.CONSTANT_ROLE;
        } else if (line.contains("------ 音频编码类型")) {
            return FunType.CONSTANT_AUDIO_ENCODE;
        } else if (line.contains("------ 预设视频质量")) {
            return FunType.CONSTANT_VIDEO_PROFILE;
        } else if (line.contains("------ 视频显示模式")) {
            return FunType.CONSTANT_VIDEO_SHOW_MODE;
        } else if (line.contains("------ 日志过滤等级")) {
            return FunType.CONSTANT_LOG;
        } else if (line.contains("------ 双流设置")) {
            return FunType.CONSTANT_DUAL_VIDEO;
        } else if (line.contains("------ Ijk控件显示模式")) {
            return FunType.CONSTANT_IJK;
        } else if (line.contains("------ 摄像头方向")) {
            return FunType.CONSTANT_CAMERA_LOCATION;
        } else if (line.contains("------ 网络连接状态")) {
            return FunType.CONSTANT_INTENT_STATUS;
        } else if (line.contains("------ 音频录音质量")) {
            return FunType.CONSTANT_AUDIO_RECORD;
        } else if (line.contains("------ 音频场景模式")) {
            return FunType.CONSTANT_AUDIO_SENSE;
        } else if (line.contains("------ 变声类型")) {
            return FunType.CONSTANT_CHANGE_VOICE;
        } else if (line.contains("------ 用户离开频道的原因")) {
            return FunType.CONSTANT_USER_LEAVE;
        } else if (line.contains("------ 音频路由")) {
            return FunType.CONSTANT_AUDIO_ROUTER;
        } else if (line.contains("------ 网络质量")) {
            return FunType.CONSTANT_INTENT_QUALITY;
        } else if (line.contains("------ 音乐文件播放")) {
            return FunType.CONSTANT_AUDIO_MUSIC;
        } else if (line.contains("------ 其他")) {
            return FunType.CONSTANT_OTHER;
        }
        return null;
    }

    private void obtainFunSimpleName(FunCodeBlock mFunCommentCodeBlock, String funFullName) {
        String replaceAllFirst = funFullName.replaceAll("public", "");
        String replaceAll = replaceAllFirst.replaceAll(";", "");
        String trim = replaceAll.trim();
        String[] funcSplit = trim.split(" ");

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int index;
        if (funcSplit[0].equals("static") && funcSplit[1].equals("synchronized")) {
            sb1.append(funcSplit[0]).append(" ").append(funcSplit[1]).append(" ").append(funcSplit[2]);
            index = 3;
        } else {
            sb1.append(funcSplit[0]).append(" ").append(funcSplit[1]);
            index = 2;
        }

        for (int i = index; i < funcSplit.length; i++) {
            String str = funcSplit[i];
            sb2.append(str);
            if (i != funcSplit.length - 1) {
                sb2.append(" ");
            }
        }
        mFunCommentCodeBlock.funPrefixName = sb1.toString();
        String str = sb2.toString();
        int index1 = str.indexOf("(");
        mFunCommentCodeBlock.funName = str.substring(0, index1);
        mFunCommentCodeBlock.funParams = str.substring(index1).replaceAll("//IJK_MODULE_CreateIjkRendererView", "").trim();
        mFunCommentCodeBlock.staticFun = funcSplit[0].equals("static");
    }

    class FunCodeBlock {

        FunType funType;
        String funComment;
        String funSimpleComment;
        String funFullName;
        String funPrefixName;
        String funName;
        String funParams;
        boolean staticFun;

        FunCodeBlock(FunType funType, String funComment, String funFullName) {
            this.funType = funType;
            this.funComment = funComment;
            this.funFullName = funFullName;
        }

        @Override
        public String toString() {
            return "FunCodeBlock{" +
                    "funType=" + funType +
//                    ", funComment='" + funComment + '\'' +
                    ", funSimpleComment='" + funSimpleComment + '\'' +
                    ", funFullName='" + funFullName + '\'' +
                    ", funPrefixName='" + funPrefixName + '\'' +
                    ", funName='" + funName + '\'' +
                    ", funParams='" + funParams + '\'' +
//                    ", staticFun=" + staticFun +
                    '}';
        }
    }

    enum FunType {

        CORE("核心方法"), AUDIO_CORE("核心音频方法"), VIDEO_CORE("核心视频方法"), AUDIO_ROUTER("音频路由"),
        AUDIO_RECORD("音频录音"), EARBACK("耳返控制"), AUDIO_MUSIC("音乐文件播放及混音"), AUDIO_EFFECT("音效文件播放管理"),
        INTENT_CHECK("网络检测"), ORIGINAL_AUDIO("原始音频数据"), AUDIO_OUT_CAPTURE("音频自采集"), VIDEO_OUT_CAPTURE("视频自采集（仅适用于 Push 模式）"),
        VIDEO_DUAL("视频双流模式"), CAMERA_CONTROL("摄像头控制"), SCREEN_SHARE("屏幕共享"), CDN_SETTING("CDN推流设置"),
        CDN_PUSH("CDN推流"), CDN_PULL("CDN拉流"), OTHER("其他方法"),

        CALLBACK_CORE("核心回调"), CALLBACK_AUDIO_CORE("核心音频事件回调"), CALLBACK_VIDEO_CORE("核心视频事件回调"),
        CALLBACK_STATISTICS("数据统计事件回调"), CALLBACK_AUDIO_MUSIC("音频音效播放事件回调"), CALLBACK_OTHER("其他回调"),

        CONSTANT_JOIN_CHANNEL("加入频道错误码"), CONSTANT_FOCUS_LEAVE("强制离开频道错误码"), CONSTANT_RTMP("RTMP推流错误码"),
        CONSTANT_CHANNEL_MODE("频道模式"), CONSTANT_ROLE("用户角色"), CONSTANT_AUDIO_ENCODE("音频编码类型"),
        CONSTANT_VIDEO_PROFILE("预设视频质量"), CONSTANT_VIDEO_SHOW_MODE("视频显示模式"), CONSTANT_LOG("日志过滤等级"),
        CONSTANT_DUAL_VIDEO("双流设置"), CONSTANT_IJK("Ijk控件显示模式"), CONSTANT_CAMERA_LOCATION("摄像头方向"),
        CONSTANT_INTENT_STATUS("网络连接状态"), CONSTANT_AUDIO_RECORD("音频录音质量"), CONSTANT_AUDIO_SENSE("音频场景模式"),
        CONSTANT_CHANGE_VOICE("变声类型"), CONSTANT_USER_LEAVE("用户离开频道的原因"), CONSTANT_AUDIO_ROUTER("音频路由"),
        CONSTANT_INTENT_QUALITY("网络质量"), CONSTANT_AUDIO_MUSIC("音乐文件播放"), CONSTANT_OTHER("其他");

        String funName;

        FunType(String name) {
            funName = name;
        }

        @Override
        public String toString() {
            return funName;
        }
    }
}

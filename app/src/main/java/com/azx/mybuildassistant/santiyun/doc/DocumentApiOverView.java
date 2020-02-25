package com.azx.mybuildassistant.santiyun.doc;

import java.util.List;

class DocumentApiOverView {

    private static final String WEB_LINK = "http://doc3.3ttech.cn/live/client-api/android/methods.html";
    private static final String WEB_CALLBACK_LINK = "http://doc3.3ttech.cn/live/client-api/android/callbacks.html";

    String buildDocument(List<DocumentInitData.FunCodeBlock> funCodeBlocks, List<DocumentInitData.FunCodeBlock> callBackFunCodeBlocks) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 概述\n");
        sb.append("# TTT Java API Reference for Android\n");
        sb.append("\n三体云通过全球部署的虚拟网络，提供可以灵活搭配的 API 组合，为移动端到移动端以及移动端到 Web 端提供质量可靠的实时音视频通信。\n");
        sb.append("\n## 方法\n");
        sb.append("\nTTTRtcEngineKit 是三体云SDK的入口类，提供所有可供App调用的方法。\n");
        sb.append("\n根据方法实现的功能，我们把所有方法分为以下几类。\n");
        DocumentInitData.FunType funType = null;
        for (DocumentInitData.FunCodeBlock funCodeBlock : funCodeBlocks) {
            if (funType == null || funType != funCodeBlock.funType) {
                funType = funCodeBlock.funType;
                sb.append("\n### ").append(funCodeBlock.funType.toString()).append("\n");
                sb.append("| 方法 | 描述 |").append("\n");
                sb.append("| :--------- | :--------- |").append("\n");
            }
            sb.append("| [").append(funCodeBlock.funName).append("]");
            sb.append("(").append(WEB_LINK).append("#").append(funCodeBlock.funName).append(")");
            sb.append(" | ").append(funCodeBlock.funSimpleComment).append(" |").append("\n");
        }
        sb.append("\n");
        sb.append("\n## 回调\n");
        for (DocumentInitData.FunCodeBlock callBackFunCodeBlock : callBackFunCodeBlocks) {
            if (funType == null || funType != callBackFunCodeBlock.funType) {
                funType = callBackFunCodeBlock.funType;
                sb.append("\n### ").append(callBackFunCodeBlock.funType.toString()).append("\n");
                sb.append("| 方法 | 描述 |").append("\n");
                sb.append("| :--------- | :--------- |").append("\n");
            }
            sb.append("| [").append(callBackFunCodeBlock.funName).append("]");
            sb.append("(").append(WEB_CALLBACK_LINK).append("#").append(callBackFunCodeBlock.funName).append(")");
            sb.append(" | ").append(callBackFunCodeBlock.funSimpleComment).append(" |").append("\n");
        }
        return sb.toString();
    }
}
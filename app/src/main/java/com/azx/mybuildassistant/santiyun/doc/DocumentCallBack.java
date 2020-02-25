package com.azx.mybuildassistant.santiyun.doc;

import java.util.List;

class DocumentCallBack {

    String buildDocument(List<DocumentInitData.FunCodeBlock> callBackFunCodeBlocks, List<DocumentBuildTask.ApiDetailBean> apiDetailBeans) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 回调\n");
        sb.append("| 回调 | 描述 |").append("\n");
        sb.append("| :--------- | :--------- |").append("\n");
        for (DocumentInitData.FunCodeBlock funCodeBlock : callBackFunCodeBlocks) {
            sb.append("| [").append(funCodeBlock.funName).append("]");
            sb.append("(").append("#").append(funCodeBlock.funName).append(")");
            sb.append(" | ").append(funCodeBlock.funSimpleComment).append(" |").append("\n");
        }
        sb.append("\n");
        sb.append("\n## 详细描述\n");
        for (DocumentBuildTask.ApiDetailBean apiDetailBean : apiDetailBeans) {
            sb.append(apiDetailBean.toString());
        }
        return sb.toString();
    }
}

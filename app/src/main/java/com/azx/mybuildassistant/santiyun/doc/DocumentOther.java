package com.azx.mybuildassistant.santiyun.doc;

import com.azx.mybuildassistant.santiyun.bean.DocClassBean;

import java.util.List;

class DocumentOther {

    String buildDocument(DocClassBean[] classBeans, List<DocumentInitData.FunCodeBlock> constantsCodeBlocks) {
        StringBuilder sb = new StringBuilder();
        sb.append("# 对象枚举\n");
        sb.append("| 对象 | 描述 |").append("\n");
        sb.append("| :--------- | :--------- |").append("\n");
        for (DocClassBean classBean : classBeans) {
            sb.append("| [").append(classBean.className).append("]");
            sb.append("(").append("#").append(classBean.className).append(")");
            sb.append(" | ").append(classBean.classDes).append(" |").append("\n");
        }

        sb.append("\n");
        sb.append("枚举 | 描述 |").append("\n");
        sb.append("| :--------- | :--------- |").append("\n");
        for (DocumentInitData.FunCodeBlock funCodeBlock : constantsCodeBlocks) {
            String[] funNameSplit = funCodeBlock.funName.split("=");
            String funNameRep = funCodeBlock.funName.replaceAll("_", "\\\\_");
            sb.append("| [Constants.").append(funNameRep).append("]");
            sb.append("(").append("#").append(funNameSplit[0].trim()).append(")");
            sb.append(" | ").append(funCodeBlock.funSimpleComment).append(" |");
            sb.append("\n");
        }

        sb.append("\n");
        sb.append("\n## 详细描述\n");
        for (DocClassBean classBean : classBeans) {
            sb.append("\n");
            sb.append("### <div id=").append(classBean.className).append(">").append(classBean.className).append("</div>").append("\n");
            sb.append("| 属性 | 说明 |").append("\n");
            sb.append("| :--------- | :--------- |").append("\n");
            for (DocClassBean.AttrBean attrBean : classBean.attrBeans) {
                String attri = attrBean.attribute.replaceAll("private", "")
                        .replaceAll("private", "")
                        .replaceAll("public", "")
                        .replaceAll("_", "\\\\_")
                        .replaceAll(";", " ");
                if (attri.contains("=")) {
                    attri = attri.split("=")[0].trim();
                }
                sb.append("| ").append(attri);
                sb.append(" | ").append(attrBean.attributeDes).append(" |").append("\n");
            }
        }
        DocumentInitData.FunType funType = null;
        for (DocumentInitData.FunCodeBlock funCodeBlock : constantsCodeBlocks) {
            if (funType == null || funType != funCodeBlock.funType) {
                funType = funCodeBlock.funType;
                sb.append("\n### ").append(funCodeBlock.funType.toString()).append("\n");
                sb.append("| 枚举 | 描述 |").append("\n");
                sb.append("| :--------- | :--------- |").append("\n");
            }

            String str = funCodeBlock.funName.replaceAll("_", "\\\\_");
            String[] split = funCodeBlock.funName.split("=");
            sb.append("| <div id=").append(split[0].trim()).append(">").append(str).append("</div>");
            sb.append(" | ").append(funCodeBlock.funSimpleComment).append(" |").append("\n");
        }
        return sb.toString();
    }
}

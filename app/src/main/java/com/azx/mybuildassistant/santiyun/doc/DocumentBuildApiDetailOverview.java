package com.azx.mybuildassistant.santiyun.doc;

import java.util.ArrayList;
import java.util.List;

public class DocumentBuildApiDetailOverview {

    private List<OverViewBean> overViews;
    private List<OverViewBean> staticOverViews;

    public DocumentBuildApiDetailOverview() {
        overViews = new ArrayList<>();
        staticOverViews = new ArrayList<>();
    }

    void produceOverViewBean(String funcFullName) {
//        System.out.println("funFullName : " + funFullName);
        OverViewBean bean = new OverViewBean();
        String replaceAllFirst = funcFullName.replaceAll("public", "");
        String replaceAll = replaceAllFirst.replaceAll(";", "");
        String trim = replaceAll.trim();
        String[] funcSplit = trim.split(" ");

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int index;
        if (funcSplit[0].equals("static") && funcSplit[1].equals("synchronized")) {
            sb1.append(funcSplit[0]).append(" ").append(funcSplit[1]).append(" ").append(funcSplit[2]);
            index = 3;
        } else if (funcSplit[0].equals("abstract") || funcSplit[0].equals("static")) { // abstract
            sb1.append(funcSplit[0]).append(" ").append(funcSplit[1]);
            index = 2;
        } else {
            sb1.append(funcSplit[0]);
            index = 1;
        }

        for (int i = index; i < funcSplit.length; i++) {
            String str = funcSplit[i];
            sb2.append(str);
            if (i != funcSplit.length - 1) {
                sb2.append(" ");
            }
        }
        bean.prefixName = sb1.toString();
        String str = sb2.toString();
        int index1 = str.indexOf("(");
        bean.funName = str.substring(0, index1);
        bean.funParams = str.substring(index1).replaceAll("//IJK_MODULE_CreateIjkRendererView", "").trim();
//        System.out.println(bean.toString());
        if (funcSplit[0].equals("static")) {
            staticOverViews.add(bean);
        } else {
            overViews.add(bean);
        }
    }

    String buildOverViewDoc() {
        StringBuilder sb = new StringBuilder();
        sb.append("# 方法\n");
        sb.append("<table>");
        sb.append("<tr><td colspan=\"2\"><h3>Public 成员函数</h3></td></tr>");
        for (OverViewBean overview : overViews) {
            sb.append("<tr>");
            sb.append("<td align=\"right\">").append(overview.prefixName).append("</td>");
            sb.append("<td>").append("<a href=#").append(overview.funName).append(">").append(overview.funName).append("</a>")
                    .append(overview.funParams).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("\n");
        sb.append("<table>");
        sb.append("<tr><td colspan=\"2\"><h3>静态Public 成员函数</h3></td></tr>");
        for (OverViewBean overview : staticOverViews) {
            sb.append("<tr>");
            sb.append("<td align=\"right\">").append(overview.prefixName).append("</td>");
            sb.append("<td>").append("<a href=#").append(overview.funName).append(">").append(overview.funName).append("</a>")
                    .append(overview.funParams).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    class OverViewBean {

        String prefixName;
        String funName;
        String funParams;

        @Override
        public String toString() {
            return "OverViewBean{" +
                    "prefixName='" + prefixName + '\'' +
                    ", funName='" + funName + '\'' +
                    ", funParams='" + funParams + '\'' +
                    '}';
        }
    }
}

package com.azx.mybuildassistant.santiyun.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocClassBean {

    public String className;
    public String classDes;
    public List<AttrBean> attrBeans;

    public DocClassBean(String className, String classDes, String filePath) {
        this.className = className;
        this.classDes = classDes;
        attrBeans = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean startRead = false, endRead = false, regionCheck = false, videoMixerCheck = false;
            StringBuilder stringBuilder = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("------ 过时 API")) {
                    break;
                }

                if (!regionCheck && "VideoCompositingLayout.Region".equals(className)) {
                    if (!line.contains("public static class Region")) {
                        continue;
                    } else {
                        regionCheck = true;
                    }
                }

                if (!videoMixerCheck && "PublisherConfiguration.VideoMixerParams".equals(className) && !line.contains("public static class VideoMixerParams")) {
                    videoMixerCheck = true;
                    continue;
                }

                if (line.contains("------ 属性")) {
                    startRead = true;
                    continue;
                }

                if (line.contains("------ 构造函数")) {
                    break;
                }

                if (startRead) {
                    if (line.contains("/**")) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(line).append("\n");
                    } else if (line.contains("*/")) {
                        if (stringBuilder != null) {
                            endRead = true;
                            stringBuilder.append(line).append("\n");
                        }
                    } else if (endRead) {
                        AttrBean attrBean = new AttrBean();
                        attrBean.attribute = line;
                        attrBean.attributeDes = stringBuilder.toString().replaceAll("/*\\*", "").replaceAll("\\*", "").replaceAll("/", "").trim();
                        attrBeans.add(attrBean);
                        stringBuilder.delete(0, stringBuilder.toString().length());
                        stringBuilder = null;
                        endRead = false;
                    } else {
                        if (stringBuilder != null) {
                            stringBuilder.append(line).append("\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "DocClassBean{" +
                "className='" + className + '\'' +
                ", classDes='" + classDes + '\'' +
                ", attrBean=" + Arrays.toString(attrBeans.toArray()) +
                '}';
    }

    public class AttrBean {

        public String attribute;
        public String attributeDes;

        @Override
        public String toString() {
            return "AttrBean{" +
                    "attribute='" + attribute + '\'' +
                    ", attributeDes='" + attributeDes + '\'' +
                    '}';
        }
    }
}

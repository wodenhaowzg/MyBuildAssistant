package com.azx.mybuildassistant.santiyun.bean;

public class PublishDemoBean {

    public String project_path;
    public String output_apk_path;
    public String src_apk_name;
    public String target_apk_name;

    public PublishDemoBean(String project_path, String output_apk_path, String src_apk_name, String target_apk_name) {
        this.project_path = project_path;
        this.output_apk_path = output_apk_path;
        this.src_apk_name = src_apk_name;
        this.target_apk_name = target_apk_name;
    }
}

package com.niitcoder.coursegrade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {
    //静态资源对外暴露的访问路径
    private String vpath;

    //文件上传目录
    private String serverDisk ;


    public String getVpath() {
        return vpath;
    }

    public void setVpath(String vpath) {
        this.vpath = vpath;
    }

    public String getServerDisk() {
        return serverDisk;
    }

    public void setServerDisk(String serverDisk) {
        this.serverDisk = serverDisk;
    }
}

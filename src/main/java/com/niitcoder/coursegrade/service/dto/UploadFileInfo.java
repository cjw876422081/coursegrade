package com.niitcoder.coursegrade.service.dto;

import java.io.InputStream;

public class UploadFileInfo extends FileInfo {
    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}

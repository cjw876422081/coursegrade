package com.niitcoder.coursegrade.service.dto;

import com.niitcoder.coursegrade.domain.StudentHomework;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeworkPostItem extends StudentHomework {
    List<FileInfo> files=new ArrayList<>();

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }
}

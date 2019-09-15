package com.niitcoder.coursegrade.service.dto;

import com.niitcoder.coursegrade.domain.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class NotePostItem {
    @ApiModelProperty(value = "笔记Id")
    private Long id;
    @ApiModelProperty(value = "笔记类型")
    private String noteType;
    @ApiModelProperty(value = "笔记内容")
    private String noteMemo;
    @ApiModelProperty(value = "关联课程")
    private CourseInfo course;
    @ApiModelProperty(value = "关联授课内容")
    private CoursePlan plan;
    @ApiModelProperty(value = "关联作业")
    private CourseHomework homework;
    @ApiModelProperty(value = "关联父级笔记")
    private CourseNote parentNote;

    List<FileInfo> files=new ArrayList<>();

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNoteType() {
        return noteType;
    }


    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteMemo() {
        return noteMemo;
    }

    public void setNoteMemo(String noteMemo) {
        this.noteMemo = noteMemo;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    public CoursePlan getPlan() {
        return plan;
    }

    public void setPlan(CoursePlan plan) {
        this.plan = plan;
    }

    public CourseHomework getHomework() {
        return homework;
    }

    public void setHomework(CourseHomework homework) {
        this.homework = homework;
    }

    public CourseNote getParentNote() {
        return parentNote;
    }

    public void setParentNote(CourseNote parentNote) {
        this.parentNote = parentNote;
    }
}
